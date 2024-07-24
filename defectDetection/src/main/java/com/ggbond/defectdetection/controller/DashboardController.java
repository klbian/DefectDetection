package com.ggbond.defectdetection.controller;

import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.dto.DashboardInfoDto;
import com.ggbond.defectdetection.dto.DetectResDto;
import com.ggbond.defectdetection.software.data.DataModule;
import com.ggbond.defectdetection.software.image.ImageModule;
import com.ggbond.defectdetection.util.SseUtil;
import com.ggbond.defectdetection.util.SystemStatusUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: 19461
 * Date: 2024/2/23
 */
@RestController
@Slf4j
@ResponseBody
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    SseUtil sseUtil;

    @GetMapping(value="/pictureInfo")
    public SseEmitter flushPictureHandler(HttpSession httpSession){

        log.info("接收到请求");
        int userId = (int) httpSession.getAttribute("user");

        SseEmitter sseEmitter = sseUtil.connect((long) userId);
        DetectResDto resDto=ImageModule.getRes();

        sseUtil.sendMessage((long)userId, String.valueOf(Result.IMAGE_CODE),resDto);

        int runTime= SystemStatusUtil.getContinuousWorkingSeconds();
        int defectionsSum= DataModule.getTotalDefectionsNum();
        double defectRate=DataModule.getDefectiveRate();
        String highestOccurrenceDefect=DataModule.getHighestOccurrenceDefect();

        DashboardInfoDto dashboardInfoDto=new DashboardInfoDto(runTime,defectionsSum,defectRate,highestOccurrenceDefect,null);

        sseUtil.sendMessage((long)userId, String.valueOf(Result.IMAGE_CODE),dashboardInfoDto);

        return sseEmitter;
    }


}