package com.ggbond.defectdetection.software.common;

import com.ggbond.defectdetection.dto.DashboardInfoDto;
import com.ggbond.defectdetection.software.data.DataModule;
import com.ggbond.defectdetection.util.SseUtil;
import com.ggbond.defectdetection.util.SystemStatusUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: 19461
 * Description: 发送个前端信息的类
 * Date: 2024/2/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class WebInfoSendThread extends Thread {

    @Autowired
    SseUtil sseUtil;

    @Override
    public void run(){
        //推送信息
        while(true){
            while(sseUtil.isEmpty()){
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int runTime= SystemStatusUtil.getContinuousWorkingSeconds();
            int defectionsSum= DataModule.getTotalDefectionsNum();
            double defectRate=DataModule.getDefectiveRate();
            String highestOccurrenceDefect=DataModule.getHighestOccurrenceDefect();
            DashboardInfoDto dashboardInfoDto=new DashboardInfoDto(runTime,defectionsSum,defectRate,highestOccurrenceDefect,null);

            sseUtil.sendMessageToAll(null,dashboardInfoDto);
        }
    }

}