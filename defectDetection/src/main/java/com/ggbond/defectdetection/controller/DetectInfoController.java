package com.ggbond.defectdetection.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.dto.ChartsDto;
import com.ggbond.defectdetection.dto.DetectResDto;
import com.ggbond.defectdetection.pojo.Defection;
import com.ggbond.defectdetection.pojo.DetectLog;
import com.ggbond.defectdetection.pojo.Manager;
import com.ggbond.defectdetection.service.DefectionService;
import com.ggbond.defectdetection.service.DetectLogService;
import com.ggbond.defectdetection.service.WorkOrderService;
import com.ggbond.defectdetection.software.common.ConfigProperties;
import com.ggbond.defectdetection.software.data.DataModule;
import com.ggbond.defectdetection.software.info.LogPoint;
import com.ggbond.defectdetection.software.info.OpEnum;
import com.ggbond.defectdetection.util.ImgUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Author: 19461
 * Date: 2024/2/23
 */
@RestController
@Slf4j
@ResponseBody
@RequestMapping("/detectInfo")
@CrossOrigin("*")
public class DetectInfoController {

    @Autowired
    DetectLogService detectLogService;

    @Autowired
    DefectionService defectionService;

    @Autowired
    WorkOrderService workOrderService;

    @Autowired
    DataModule dataModule;


    @GetMapping("/info/history")
    public Result getHistoryInfoGetHandler(int page, int pageSize,
                                        @RequestParam(required = false) Integer jobId,
                                        @RequestParam(required = false) List<LocalDateTime> dateRange){

        IPage<DetectLog> pageInfo=new Page<>(page,pageSize);
        LocalDateTime dateR=null;
        LocalDateTime dateL=null;

        if(dateRange!=null){
            dateL=dateRange.get(0);
            dateR=dateRange.get(1);
        }

        LambdaQueryWrapper<DetectLog> lqw=new LambdaQueryWrapper<>();

        lqw.eq(jobId!=null,DetectLog::getWorkOrderId,jobId);
        lqw.le(dateR!=null,DetectLog::getTime,dateR);
        lqw.ge(dateL!=null,DetectLog::getTime,dateL);

        detectLogService.page(pageInfo,lqw);
        int totalPages= Math.toIntExact(detectLogService.count(lqw));

        List<DetectLog> detectLogList=pageInfo.getRecords();
        List<DetectResDto> detectResDtoList=DetectResDto.getDtoFromEntities(detectLogList);


        if(detectResDtoList==null){
            return  Result.fail("获取失败,请稍后再试");
        }
        for (DetectResDto detectResDto : detectResDtoList) {
            detectResDto.totals =totalPages;
        }
        return Result.success("获取成功",detectResDtoList);
    }

    @PostMapping("/info/history")
    public Result getHistoryPostInfoHandler(int page, int pageSize,
                                        @RequestParam(required = false) Integer jobId,
                                        @RequestParam(required = false) List<LocalDateTime> dateRange){

        IPage<DetectLog> pageInfo=new Page<>(page,pageSize);
        LocalDateTime dateR=null;
        LocalDateTime dateL=null;

        if(dateRange!=null){
            dateL=dateRange.get(0);
            dateR=dateRange.get(1);
        }

        LambdaQueryWrapper<DetectLog> lqw=new LambdaQueryWrapper<>();

        lqw.eq(jobId!=null,DetectLog::getWorkOrderId,jobId);
        lqw.le(dateR!=null,DetectLog::getTime,dateR);
        lqw.ge(dateL!=null,DetectLog::getTime,dateL);

        detectLogService.page(pageInfo,lqw);
        int totalPages= Math.toIntExact(detectLogService.count(lqw));

        List<DetectLog> detectLogList=pageInfo.getRecords();
        List<DetectResDto> detectResDtoList=DetectResDto.getDtoFromEntities(detectLogList);


        if(detectResDtoList==null){
            return  Result.fail("获取失败,请稍后再试");
        }
        for (DetectResDto detectResDto : detectResDtoList) {
            detectResDto.totals =totalPages;
        }
        return Result.success("获取成功",detectResDtoList);
    }


    @GetMapping("/info/details")
    public Result<DetectResDto> getDetailsHandler(int id){

        DetectLog detectLog=detectLogService.getById(id);

        if(detectLog==null){
            return Result.fail("加载失败,请稍后再试");
        }

        LambdaQueryWrapper<Defection> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Defection::getDetectId,id);

        List<Defection> defectionList=defectionService.list(lqw);

        DetectResDto detectResDto=DetectResDto.generateFromFather(detectLog) ;
        detectResDto.setDefections(defectionList);
        try {
            String imgBase64= ImgUtil.imageToBase64ByPath(detectLog.getStoragePath());
            detectResDto.setImgBase64(imgBase64);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("加载失败,请稍后再试");
        }
        return Result.success("加载成功",detectResDto);
    }

    @DeleteMapping("/info/delete")
    @LogPoint(value = OpEnum.Delete, mainRole = Manager.class,target = DetectLog.class)
    public Result deleteRecordHandler(HttpSession session,@RequestBody ArrayList<Integer> ids){
        log.info("要删除的ids:{}",ids);
        if(detectLogService.removeBatchByIds(ids)){
            return Result.success("删除成功");
        }else{
            return Result.fail("删除失败,请稍后再试");
        }
    }

    @GetMapping("/charts/load")
    public Result loadChartsHandler(){

        Map<String, LinkedHashMap> dataMaps = dataModule.getDataMaps();
        List<ChartsDto> chartsDtoList=new LinkedList<>();

        //图1
        ChartsDto<Map> chartsDto1=new ChartsDto<>();
        chartsDto1.setIndex(1);
        chartsDto1.setName("缺陷率变化图");
        chartsDto1.setType("折线图");
        chartsDto1.setSource(dataMaps.get("lineData"));

        chartsDtoList.add(chartsDto1);

        //图2
        ChartsDto<Map> chartsDto2=new ChartsDto<>();
        chartsDto2.setIndex(2);
        chartsDto2.setName("缺陷率变化图");
        chartsDto2.setType("柱状图");
        chartsDto2.setSource(dataMaps.get("lineData"));

        chartsDtoList.add(chartsDto2);

        //图3
        ChartsDto<Map> chartsDto3=new ChartsDto<>();
        chartsDto3.setIndex(3);
        chartsDto3.setName("缺陷占比环状图");
        chartsDto3.setType("环形图");
        chartsDto3.setSource(dataMaps.get("pieData"));

        chartsDtoList.add(chartsDto3);

        //图4
        ChartsDto<Map> chartsDto4=new ChartsDto<>();
        chartsDto4.setIndex(4);
        chartsDto4.setName("api使用次数柱状图");
        chartsDto4.setType("柱状图");
        chartsDto4.setSource(dataMaps.get("apiData"));

        chartsDtoList.add(chartsDto4);

        //图5
        ChartsDto<Map> chartsDto5=new ChartsDto<>();
        chartsDto5.setIndex(5);
        chartsDto5.setName("缺陷总分布图");
        chartsDto5.setType("柱状图");
        chartsDto5.setSource(dataMaps.get("defectionData"));

        chartsDtoList.add(chartsDto5);


        return Result.success("加载成功",chartsDtoList);
    }


    @PutMapping("/charts/set")
    public Result getDataHandler(@RequestParam(required = false,name = "workOrderNumber") Integer workOrderId,
                                 @RequestParam(required = false,name = "N") Integer granularity ){

        if(workOrderId!=null&&workOrderService.getById(workOrderId)==null){
            return Result.fail("工单号不存在");
        }

        Map<String, Map> dataMaps = dataModule.getChartsByWorkOrderId(workOrderId,granularity);

        List<ChartsDto> chartsDtoList=new LinkedList<>();

        //图1
        ChartsDto<Map> chartsDto1=new ChartsDto<>();
        chartsDto1.setIndex(1);
        chartsDto1.setName("缺陷率变化图");
        chartsDto1.setType("折线图");
        chartsDto1.setSource(dataMaps.get("lineData"));

        chartsDtoList.add(chartsDto1);

        //图2
        ChartsDto<Map> chartsDto2=new ChartsDto<>();
        chartsDto2.setIndex(2);
        chartsDto2.setName("缺陷率变化图");
        chartsDto2.setType("柱状图");
        chartsDto2.setSource(dataMaps.get("lineData"));

        chartsDtoList.add(chartsDto2);

        //图3
        ChartsDto<Map> chartsDto3=new ChartsDto<>();
        chartsDto3.setIndex(3);
        chartsDto3.setName("缺陷占比环状图图");
        chartsDto3.setType("环形图");
        chartsDto3.setSource(dataMaps.get("pieData"));

        chartsDtoList.add(chartsDto3);

        return Result.success("加载成功",chartsDtoList);
    }


}