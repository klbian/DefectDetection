package com.ggbond.defectdetection.software.common;

import com.ggbond.defectdetection.software.face.StatusAndDevicesInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统信息更新线程
 *
 * <p>
 * Author: 19461
 * Date: 2024/2/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class SysInfoThread extends Thread{

    @Autowired
    StatusAndDevicesInterface statusAndDevicesInterface;

    @Override
    public void run(){

        while(true){
            //更新界面
            statusAndDevicesInterface.updateStatus();
            try {
                do{
                    Thread.sleep(500);
                }while(CommonResource.getStatus()==SysStatus.ClOSING);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}