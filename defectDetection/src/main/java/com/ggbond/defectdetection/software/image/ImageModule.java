package com.ggbond.defectdetection.software.image;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.config.YAMLPropertySourceFactory;
import com.ggbond.defectdetection.dto.DetectResDto;
import com.ggbond.defectdetection.pojo.*;
import com.ggbond.defectdetection.service.*;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.common.ConfigProperties;
import com.ggbond.defectdetection.software.common.Software;
import com.ggbond.defectdetection.software.common.SysStatus;
import com.ggbond.defectdetection.software.data.DataModule;
import com.ggbond.defectdetection.software.face.RealtimeInterface;
import com.ggbond.defectdetection.util.ImgUtil;
import com.ggbond.defectdetection.util.SseUtil;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 图像处理模块,包含图像预处理,模型识别,局部异常处理
 * <p>
 * Author: 19461
 * Date: 2024/2/16
 */
@Data
@Component("ImageModule")
@DependsOn("ConfigProperties")
@RestController //接受ip摄像机的图像需要
@RequestMapping("/detect")
public class ImageModule {

    @Autowired
    SseUtil sseUtil;

    @Autowired
    CheckService checkService;

    @Autowired
    DefectionService defectionService;

    @Autowired
    DetectLogService detectLogService;

    @Autowired
    DefectionCategoryService defectionCategoryService;

    @Autowired
    DataModule dataModule;

    @Autowired
    private RealtimeInterface realtimeInterface;


    private static DetectResDto res=new DetectResDto();

    public static DetectResDto getRes(){
        return res;
    }

    private DetectModel detectModel=new DetectModel();

    public ImageModule(){

    }

    //接收图像
    @PostMapping("/img")
    public void imgProcess(@RequestPart("img") MultipartFile img) throws Exception {

        if(CommonResource.getStatus() != SysStatus.WORKING){
            //同步控制暂停设备
            return;
        }

        //判断是否为图片并转为jpeg格式
        String imgBase64 = ImgUtil.convertToJPGBase64(img);

        //模型分析
        res=detectModel.detectOne(imgBase64);

        //存储新的检测信息
        //1.获取工单号
        int workOrderId=CommonResource.getCurrentWorkOder().getId();
        //2.缺陷总数
        int defectionsSum=res.getDefections().size();

        //3.存储相关信息
        //3.1 存储检测信息
        DetectLog detectLog=new DetectLog();
        String name=ImgUtil.generateRandomName();
        detectLog.setName(name);
        detectLog.setDefectionsSum(defectionsSum);
        detectLog.setWorkOrderId(workOrderId);
        detectLog.setStoragePath(ConfigProperties.properties.getModelConfig().getResStoragePath()+"\\"+workOrderId+"\\"+name);
        detectLog.setTime(LocalDateTime.now());
        ImgUtil.saveImageToFile(res.getImgBase64(),detectLog.getStoragePath());
        detectLogService.save(detectLog);

        LambdaQueryWrapper<DetectLog> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DetectLog::getName,name);

        DetectLog detectLog1=detectLogService.getOne(lqw);
        Integer detectId=detectLog1.getId();

        //3.2 存储缺陷信息
        for(Defection defection:res.getDefections()){

            String category=defection.getCategory();
            LambdaQueryWrapper<DefectionCategory> lqw1=new LambdaQueryWrapper<>();
            lqw1.eq(category!=null,DefectionCategory::getName,category);
            if(!defectionCategoryService.exists(lqw1)){
                DefectionCategory defectionCategory=new DefectionCategory(null,category, 0,LocalDateTime.now());
                defectionCategoryService.save(defectionCategory);
            }
            Integer categoryId=defectionCategoryService.getOne(lqw1).getId();
            defection.setCategoryId(categoryId);
            defection.setDetectId(detectId);
            defectionService.save(defection);

        }

        //3.2 工单进度加1,更新到新工单
        CommonResource.updateWorkOrder();

        //4. 更新表格dataMap
        DetectResDto resPlus=DetectResDto.generateFromFather(detectLog1);
        dataModule.updateDataMaps(resPlus);

        //更新图像和图表
        realtimeInterface.getDetectImagePanel().updateImageAndTable(res);
        realtimeInterface.getDetectImagePanel().updateTextLabel(workOrderId,CommonResource.getCurrentNum(),CommonResource.getDetectSum());
        realtimeInterface.updateCharts(dataModule.getDataMaps());

        //将最新结果发送至web端
        new Thread(sseUtil.sendMessageToAll(String.valueOf(Result.IMAGE_CODE),res),"发送检测结果").start();
    }


    //初始化工作:读取配置,加载模型,测试连接
    @PostConstruct
    public void init(){
        //加载模型
        try {
            res.setImgBase64(ImgUtil.imageToBase64ByPath("D:\\java.java\\defectDetection\\src\\main\\resources\\assets\\init.png"));
            res.setDefections(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        detectModel.init();
    }

    //获取当前的结果
    public DetectResDto getCurrentDetectRes(){
        return res;
    }

    //图像检测
    public DetectResDto imgDetect(String  imgBase64){

        if(imgBase64==null){
            return null;
        }

        return detectModel.detectOne(imgBase64);
    }


    public static boolean ConnectModelTest(){
        return DetectModel.testHttpConnection();
    }

    //保存为抽检信息
    public boolean saveAsCheckInfo(DetectResDto detectResDto){

        Check checkInfo=Check.convertTOCheck(detectResDto);

        return checkService.save(checkInfo);
    }
}