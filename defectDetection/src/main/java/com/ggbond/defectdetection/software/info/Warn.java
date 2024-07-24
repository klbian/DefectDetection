package com.ggbond.defectdetection.software.info;

import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.pojo.Warnings;
import com.ggbond.defectdetection.service.WarningsService;
import com.ggbond.defectdetection.software.common.ConfigProperties;
import com.ggbond.defectdetection.software.data.DataModule;
import com.ggbond.defectdetection.software.face.MainInterface;
import com.ggbond.defectdetection.util.SseUtil;
import com.ggbond.defectdetection.util.SystemStatusUtil;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.time.LocalDateTime;

/**
 * 处理与告警相关的事件,监听告警发生条件,输出告警信息,中断系统运行等等
 * <p>
 * Author: 19461
 * Date: 2024/2/16
 */
@Data
@Component("Warn")
@Aspect
@Slf4j
@DependsOn("ConfigProperties")
public class Warn {

    @Autowired
    private DataModule dataModule;

    @Autowired
    private WarningsService warningsService;

    @Autowired
    private MainInterface mainInterface;

    @Autowired
    SseUtil sseUtil;

    @Getter
    private static double defectiveRateWarningLine;
    @Getter
    private static int continuousWorkingMinutesWarningLine;
    @Getter
    private static Long warningInterval;

    public Warn(){

    }
    @Pointcut("@annotation(WarnPoint)")
    public void connectErrorPoint(){

    }

    @AfterThrowing("connectErrorPoint()")
    public void warnHandler(JoinPoint joinPoint){

    }

    //载入参数
    @PostConstruct
    public void init(){
        //加载配置
        defectiveRateWarningLine= ConfigProperties.properties.getWarnsConfig().getDefectiveRateWarningLine();
        continuousWorkingMinutesWarningLine=ConfigProperties.properties.getWarnsConfig().getContinuousWorkingMinutesWarningLine();
        warningInterval=ConfigProperties.properties.getWarnsConfig().getWaringInterval();
    }

    //系统异常检查线程
    public Runnable checkWarningsThread(){

        return ()->{
            Long warningInterval= Warn.warningInterval;
            long lastWarn1=0L;
            long lastWarn2=0L;

            while(true){
                long now=System.currentTimeMillis();
                if(DataModule.getDefectiveRate()>defectiveRateWarningLine&&(now-lastWarn1>warningInterval*1000)){


                    String content="缺陷率过高,已经超过 %04.2f%%".formatted(DataModule.getDefectiveRate()*100);
                    log.info(content);
                    Warnings warnings=new Warnings();
                    warnings.setLevel(WarningsEnum.warn.level);
                    warnings.setType(WarningsEnum.warn.type);
                    warnings.setContent(content);
                    warnings.setCreateTime(LocalDateTime.now());

                    warningsService.save(warnings);

                    mainInterface.getOperationAndOutputInterface().updateOutput(null,warnings);

                    //发送警告到web

                    new Thread(sseUtil.sendMessageToAll(String.valueOf(Result.WARN_CODE),content),"发送警告信息").start();
                    //发送警告到本地
                    JOptionPane.showMessageDialog(mainInterface,content,"警告",JOptionPane.WARNING_MESSAGE);
                    lastWarn1=System.currentTimeMillis();

                }else if((SystemStatusUtil.getContinuousWorkingSeconds()/60)>continuousWorkingMinutesWarningLine&&(now-lastWarn2>warningInterval*1000)) {

                    int hour=SystemStatusUtil.getContinuousWorkingSeconds()/3600;
                    int minute=(SystemStatusUtil.getContinuousWorkingSeconds()%3600)/60;
                    int seconds=(SystemStatusUtil.getContinuousWorkingSeconds()%3600)%60;

                    String content="系统工作时间过长,已经超过%d小时%d分钟%d秒".formatted(hour,minute,seconds);
                    log.info(content);

                    Warnings warnings=new Warnings();
                    warnings.setLevel(WarningsEnum.warn.level);
                    warnings.setType(WarningsEnum.warn.type);
                    warnings.setContent(content);
                    warnings.setCreateTime(LocalDateTime.now());

                    warningsService.save(warnings);

                    mainInterface.getOperationAndOutputInterface().updateOutput(null,warnings);

                    new Thread(sseUtil.sendMessageToAll(String.valueOf(Result.WARN_CODE),content),"发送警告信息").start();

                    JOptionPane.showMessageDialog(mainInterface,content,"警告",JOptionPane.WARNING_MESSAGE);
                    lastWarn2=System.currentTimeMillis();
                }

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
    }
}