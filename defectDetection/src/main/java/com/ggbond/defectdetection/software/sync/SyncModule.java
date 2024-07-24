package com.ggbond.defectdetection.software.sync;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 同步控制模块,负责控制硬件设备
 * <p>
 * Author: 19461
 * Date: 2024/2/16
 */
@Data
@Component
public class SyncModule {

    public SyncModule(){

    }
    //初始化,载入设备信息和配置信息
    public void init(){

    }
    //同步控制:指挥硬件设备合作完成运输样品,拍摄照片/视频
    public void syncControl(){

    }
    //更新配置:将用户上传的新配置发送至硬件设备
    public void updateProperties(){

    }

}