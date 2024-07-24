package com.ggbond.defectdetection.software.face;

import com.ggbond.defectdetection.pojo.Device;
import com.ggbond.defectdetection.service.DeviceService;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.common.SysStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 主界面
 * <p>
 * Author: 19461
 * Date: 2024/2/4
 */

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Component
public class MainInterface extends JFrame {

    @Autowired
    ApplicationContext context;

    @Autowired
    GUIAttributes attributes;

    //左方面板
    @Autowired
    StatusAndDevicesInterface statusAndDevicesInterface;

    //中间面板
    @Autowired
    RealtimeInterface realtimeInterface;

    //右方面板
    @Autowired
    OperationAndOutputInterface operationAndOutputInterface;

    //菜单
    @Autowired
    TopMenu topMenu;

    public void init(List<Device> devices, Map<String, LinkedHashMap> dataMaps){
        String lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        //测试是否载入属性成功
        if(!attributes.test){
            throw new RuntimeException("引入配置失败");
        }
        this.setName("主面板");
        //点击退出后只会退出窗口而不是整个程序
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                CommonResource.setStatus(SysStatus.ClOSING);
                CommonResource.saveCurrentWorkOrder();
                Device.allDeviceOffline(context.getBean(DeviceService.class));
            }
        });

        //获取主面板
        Container mainPanel=this.getContentPane();

        //设置为borderlayout布局

        this.setLayout(new BorderLayout());

        //加载界面
        mainPanel.add(BorderLayout.WEST, statusAndDevicesInterface);
        mainPanel.add(BorderLayout.CENTER,realtimeInterface);
        mainPanel.add(BorderLayout.EAST, operationAndOutputInterface);

        //添加菜单
        this.setJMenuBar(topMenu);

        //基本属性载入
        this.setBounds(attributes.x, attributes.y, attributes.width, attributes.height); //界面属性装载
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);   //最大化
        this.setResizable(false);         //不能改变大小
        this.setVisible(true);          //设置为可视化

        //
        if(CommonResource.getCurrentWorkOder()!=null)
            realtimeInterface.init(dataMaps,CommonResource.getCurrentWorkOder().getId(),CommonResource.getCurrentNum(),CommonResource.getDetectSum());
        else
            realtimeInterface.init(dataMaps,0,CommonResource.getCurrentNum(),CommonResource.getDetectSum());
        statusAndDevicesInterface.init(devices);
        operationAndOutputInterface.init();

    }



}