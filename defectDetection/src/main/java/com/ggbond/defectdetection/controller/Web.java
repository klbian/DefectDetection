package com.ggbond.defectdetection.controller;

import com.ggbond.defectdetection.software.common.WebInfoSendThread;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理we端的相关功能
 * <p>
 * Author: 19461
 * Date: 2024/2/23
 */
@Component
@Data
@Slf4j
public class Web{

    @Autowired
    WebInfoSendThread webInfoSendThread;

    public Web(){

    }

    public void init(){


    }

    public void run(){
        webInfoSendThread.start();
    }
}