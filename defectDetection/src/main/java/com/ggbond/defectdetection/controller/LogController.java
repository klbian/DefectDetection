package com.ggbond.defectdetection.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.pojo.DetectLog;
import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: 19461
 * Date: 2024/3/3
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    SysLogService sysLogService;

    @GetMapping("/info")
    public Result loadInfoHandler(@RequestParam(required = false,defaultValue = "1") Integer page,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(required = false)List<LocalDateTime> dateRange){

        IPage<SysLog> pageInfo=new Page<>(page,pageSize);
        LocalDateTime dateR=null;
        LocalDateTime dateL=null;

        if(dateRange!=null){
            dateL=dateRange.get(0);
            dateR=dateRange.get(1);
        }

        LambdaQueryWrapper<SysLog> lqw=new LambdaQueryWrapper<>();

        lqw.lt(dateR!=null,SysLog::getOpTime,dateR);
        lqw.gt(dateL!=null,SysLog::getOpTime,dateL);

        List<SysLog> sysLogList=sysLogService.list(pageInfo,lqw);
        int count= Math.toIntExact(sysLogService.count(lqw));

        if(sysLogList!=null){
            sysLogList.forEach(sysLog -> {
                sysLog.setTotals(count);
            });
            return Result.success("获取成功",sysLogList);
        }else{
            return Result.fail("加载日志失败");
        }
    }

}