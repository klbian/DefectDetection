package com.ggbond.defectdetection.software.face;

import com.ggbond.defectdetection.software.face.dialog.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * 顶栏菜单
 * <p>
 * Author: 19461
 * Date: 2024/2/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@Slf4j
public class TopMenu extends JMenuBar {

    @Autowired
    AddWorkOrderDialog addWorkOrderDialog;

    private HistoryWorkOrderQueueDialog historyWorkOrderQueueDialog=new HistoryWorkOrderQueueDialog();

    @Autowired
    ExceptionConfigDialog exceptionConfigDialog;

    @Autowired
    RunningConfigDialog runningConfigDialog;

    @Autowired
    DeviceManagerDialog deviceManagerDialog;

    private String[] menusName={"工单管理","设备管理","异常管理","属性配置"};

    public TopMenu(){
        super();
        init();
    }

    private void init(){

        //工单管理menu
        JMenu workMenu=new JMenu(menusName[0]);

        JMenuItem workQueue=new JMenuItem("工单队列");
        workQueue.addActionListener(e -> {
            historyWorkOrderQueueDialog.showDialog(SwingUtilities.getWindowAncestor(this),"工单队列");
            log.info("点击工单队列");
        });


        JMenuItem workCreate=new JMenuItem("创建工单");
        workCreate.addActionListener(e->{
            addWorkOrderDialog.showDialog(SwingUtilities.getWindowAncestor(this));
//            log.info(SwingUtilities.getWindowAncestor(this).getName());
            log.info("点击创建工单");
        });

        workMenu.add(workCreate);
        workMenu.add(workQueue);
//        workMenu.add(workHistory);

        //设备管理menu
        JMenu deviceMenu=new JMenu(menusName[1]);

        JMenuItem checkDevice=new JMenuItem("设备管理");
        checkDevice.addActionListener(e->{
            deviceManagerDialog.showDialog(SwingUtilities.getWindowAncestor(this));
            log.info("点击设备管理");
        });

        deviceMenu.add(checkDevice);

        //异常管理menu
        JMenu exceptionMenu=new JMenu(menusName[2]);

        JMenuItem exceptionHandler=new JMenuItem("异常管理");
        exceptionHandler.addActionListener(e->{
            exceptionConfigDialog.showDialog(SwingUtilities.getWindowAncestor(this));
            log.info("点击异常管理");
        });

        exceptionMenu.add(exceptionHandler);

        //属性配置menu
        JMenu propertyMenu=new JMenu(menusName[3]);

        JMenuItem propertySet=new JMenuItem("属性配置");
        propertySet.addActionListener(e->{
            runningConfigDialog.showDialog(SwingUtilities.getWindowAncestor(this));
            log.info("点击属性配置");
        });

        propertyMenu.add(propertySet);

        //载入menuBar
        this.add(workMenu);
        this.add(deviceMenu);
        this.add(exceptionMenu);
        this.add(propertyMenu);
    }

}