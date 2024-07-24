package com.ggbond.defectdetection.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.dto.DetectResDto;
import com.ggbond.defectdetection.pojo.Api;
import com.ggbond.defectdetection.pojo.DetectLog;
import com.ggbond.defectdetection.service.ApiService;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.common.SysStatus;
import com.ggbond.defectdetection.software.image.ImageModule;
import com.ggbond.defectdetection.software.info.LogPoint;
import com.ggbond.defectdetection.software.info.OpEnum;
import com.ggbond.defectdetection.util.ImgUtil;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: 19461
 * Date: 2024/3/9
 */
@Data
@RestController
@DependsOn("ImageModule")
@RequestMapping("/metalDetect")
@CrossOrigin("*")
public class thirdController {

    @Autowired
    ApiService apiService;

    @Autowired
    ImageModule imageModule;


    @GetMapping("/currentRes")
    @LogPoint(value = OpEnum.Get,mainRole = Api.class,target = DetectLog.class)
    public Result requestHandler1(HttpSession session, @RequestBody Api api){

        if(api==null||api.getApiKey()==null){
            return Result.fail("apikey为空");
        }

        boolean res=checkApi(api,1);

        if(res){
            return Result.success("获取成功", ImageModule.getRes());
        }else{
            return Result.fail("apikey不存在");
        }
    }

    @PostMapping("/detect")
    @LogPoint(value = OpEnum.Get,mainRole = Api.class,target = DetectLog.class)
    public Result requestHandler2(HttpSession session,  @RequestParam MultipartFile img){
        if(img==null){
            return Result.fail("图片为空");
        }
        String imgBase64="";
        try {
            imgBase64 = ImgUtil.convertToJPGBase64(img);
            DetectResDto detectRes = imageModule.imgDetect(imgBase64);
            imageModule.saveAsCheckInfo(detectRes);
            return Result.success("检测成功",detectRes);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("未知错误,请稍后再试");
        }

    }

    @GetMapping("/charts")
    @LogPoint(value = OpEnum.Get,mainRole = Api.class,target = DetectLog.class)
    public Result getChartsDataHandler(HttpSession session,
                                @RequestBody Api api,
                                @RequestParam(required = false) Integer workOrderId,
                                @RequestParam(required = false) Integer gra){

        if(api==null||api.getApiKey()==null){
            return Result.fail("apikey为空");
        }
        if(checkApi(api,2)){
            return Result.fail("api不存在或权限不足");
        }
        return Result.success("获取成功",imageModule.getDataModule().getChartsByWorkOrderId(workOrderId,gra));
    }

    @PostMapping("/control")
    @LogPoint(value =OpEnum.Manipulate,mainRole = Api.class,target = SysStatus.class)
    public Result controlHandler(HttpSession session,@RequestBody Api api,@RequestParam Integer opId){

        if(api==null||api.getApiKey()==null){
            return Result.fail("apikey为空");
        }
        if(checkApi(api,3)){
            return Result.fail("api不存在或权限不足");
        }
        if(opId==OpEnum.Start.ordinal()){
            if(CommonResource.getWorkOrderQueue().isEmpty()){
                return Result.fail("工单队列为空,无法开始工作");
            }else if(CommonResource.getStatus()==SysStatus.WORKING){
                return Result.fail("已经在工作状态");
            }else{
                CommonResource.setStatus(SysStatus.WORKING);
                return Result.success("开始工作");
            }
        }else if(opId==OpEnum.Pause.ordinal()){
            if(CommonResource.getStatus()==SysStatus.WORKING){
                CommonResource.setStatus(SysStatus.PAUSING);
                return Result.success("暂停工作");
            }else if(CommonResource.getStatus()==SysStatus.PAUSING){
                CommonResource.setStatus(SysStatus.WORKING);
                return Result.success("开始工作");
            }else{
                return Result.fail("未在工作中");
            }
        }else if(opId==OpEnum.Stop.ordinal()){
            CommonResource.setStatus(SysStatus.STOPPING);
            return Result.success("停止工作");
        }else{
            return Result.fail("未知指令");
        }
    }

    public boolean checkApi(Api api,int level){
        String apikeyMd5= DigestUtils.md5DigestAsHex(api.getApiKey().getBytes());

        LambdaQueryWrapper<Api> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Api::getApiKey,apikeyMd5);
        Api res=apiService.getOne(lqw);
        if(res==null){
            return false;
        }else{
            if(res.getPermissionLevel()<level){
                return false;
            }else{
                return true;
            }
        }
    }
}