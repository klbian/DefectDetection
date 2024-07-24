package com.ggbond.defectdetection.software.face.common;

import com.ggbond.defectdetection.pojo.Operator;
import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.common.SysStatus;
import com.ggbond.defectdetection.software.data.DataModule;
import com.ggbond.defectdetection.software.face.GUIAttributes;
import com.ggbond.defectdetection.software.image.ImageModule;
import com.ggbond.defectdetection.software.info.Log;
import com.ggbond.defectdetection.software.info.LogPoint;
import com.ggbond.defectdetection.software.info.OpEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Author: 19461
 * Date: 2024/3/4
 */
@Component(value = "OpPanel")
@Data
@Slf4j
public class OpPanel {

    private JPanel contentPanel =new JPanel();
    //标题

    TitledBorder titledBorder = new TitledBorder("操作");

    private int sonWidth= GUIAttributes.attributes.getWidth()/4;
    private int sonHeight= (int) (GUIAttributes.attributes.getHeight()*0.3);

    //面板
    JPanel opHoldPanel=new JPanel();
//    JPanel testHoldPanel =new JPanel();

    //按钮
    JButton startButton = new JButton("开始");
    JButton pauseAndContinueButton = new JButton("暂停/继续");
    JButton stopButton = new JButton("停止");
//    JButton testConnectDeviceButton=new JButton("设备连接测试");
    JButton testConnectModelButton=new JButton("模型连接测试");

    Dimension buttonSizeDim = new Dimension(120, 50);

    public OpPanel() {
        super();
        log.info("注入成功");
        //标题
        contentPanel.setBorder(titledBorder);

        //设置布局
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
        opHoldPanel.setLayout(null);

//        FlowLayout flowLayout=new FlowLayout(FlowLayout.LEFT);
//        flowLayout.setHgap(40);
//        testHoldPanel.setLayout(flowLayout);


        //设置按钮位置大小
        startButton.setLocation(40, 30);
        pauseAndContinueButton.setLocation(70 + buttonSizeDim.width, 30);
        stopButton.setLocation(40, 50 + buttonSizeDim.height);
        testConnectModelButton.setLocation(40,70+2*buttonSizeDim.height);


        startButton.setSize(buttonSizeDim);
        pauseAndContinueButton.setSize(buttonSizeDim);
        stopButton.setSize(buttonSizeDim);
//        testConnectDeviceButton.setSize(buttonSizeDim);
        testConnectModelButton.setSize(buttonSizeDim);


        //添加按钮
        opHoldPanel.add(startButton);
        opHoldPanel.add(pauseAndContinueButton);
        opHoldPanel.add(stopButton);
        opHoldPanel.add(testConnectModelButton);

//        testHoldPanel.add(testConnectDeviceButton);
//        testHoldPanel.add(testConnectModelButton);

        //配置
        contentPanel.setPreferredSize(new Dimension(sonWidth,sonHeight));
        contentPanel.add(opHoldPanel);
    }

    public void init() {
        //开始工作按钮
        startButton.addActionListener(e -> {
            clickStartButton();
        });

        stopButton.addActionListener(e -> {
            clickStopButton();
        });

        pauseAndContinueButton.addActionListener(e -> {

            clickPauseButton();
        });

        testConnectModelButton.addActionListener(e->{
            clickTestModelButton();
        });
    }

    @LogPoint(value = OpEnum.Start, mainRole = Operator.class, target = SysStatus.class)
    public void clickStartButton() {
        log.info("点击开始");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {

                if(CommonResource.getWorkOrderQueue().isEmpty()){
                    JOptionPane.showMessageDialog(null,"当前工单为空,请先创建工单","工单为空错误",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    CommonResource.setStatus(SysStatus.WORKING);
                    SysLog sysLog=new SysLog(null,
                            LocalDateTime.now(),
                            OpEnum.Start.getName(),
                            CommonResource.getOperatorId(),
                            1,
                            Log.getClassToName().get(SysStatus.class),
                            null,
                            null);
                    Log.handlerLog(sysLog);
                }
                return null;
            }
        };
        worker.execute();
    }

    @LogPoint(value = OpEnum.Pause, mainRole = Operator.class, target = SysStatus.class)
    public void clickPauseButton() {
        log.info("点击暂停/继续");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {



                if(CommonResource.getStatus()==SysStatus.STOPPING){
                    CommonResource.saveCurrentWorkOrder();
                    return null;
                }
                if (CommonResource.getStatus() == SysStatus.PAUSING) {
                    CommonResource.setStatus(SysStatus.WORKING);
                } else if (CommonResource.getStatus() == SysStatus.WORKING) {
                    CommonResource.setStatus(SysStatus.PAUSING);
                }
                SysLog sysLog=new SysLog(null,
                        LocalDateTime.now(),
                        OpEnum.Pause.getName(),
                        CommonResource.getOperatorId(),
                        1,
                        Log.getClassToName().get(SysStatus.class),
                        null,
                        null);
                Log.handlerLog(sysLog);
                CommonResource.saveCurrentWorkOrder();
                return null;
            }
        };
        worker.execute();
    }

    @LogPoint(value = OpEnum.Stop, mainRole = Operator.class, target = SysStatus.class)
    public void clickStopButton() {
        log.info("点击停止");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                if (CommonResource.getStatus() != SysStatus.NORMAL) {
                    CommonResource.saveCurrentWorkOrder();
                    CommonResource.setStatus(SysStatus.STOPPING);
                }
                SysLog sysLog = new SysLog(null,
                        LocalDateTime.now(),
                        OpEnum.Stop.getName(),
                        CommonResource.getOperatorId(),
                        1,
                        Log.getClassToName().get(SysStatus.class),
                        null,
                        null);
                Log.handlerLog(sysLog);
                return null;
            }
        };
        worker.execute();
    }

    public void clickTestModelButton(){
        SwingWorker<Void,Void> worker=new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if(ImageModule.ConnectModelTest()){
                    JOptionPane.showMessageDialog(null,"连接检测模型成功","通知",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null,"连接检测模型失败","警告",JOptionPane.WARNING_MESSAGE);
                }
                return null;
            }
        };
        worker.execute();
    }
}