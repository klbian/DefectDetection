package com.ggbond.defectdetection.software.face;

import com.ggbond.defectdetection.pojo.Device;
import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.pojo.Warnings;
import com.ggbond.defectdetection.software.face.common.DevicePanel;
import com.ggbond.defectdetection.software.face.common.OutputPanel;
import com.ggbond.defectdetection.software.face.common.StatusPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 系统状态界面和结果输出界面
 * <p>
 * Author: 19461
 * Date: 2024/2/6
 */
@EqualsAndHashCode(callSuper = true)
@Component
@Data
@Slf4j
@DependsOn("GUIAttributes")
public class StatusAndDevicesInterface extends JPanel {

    //面板大小
//    public final int width=GUIAttributes.attributes.width/4;
//    public final int height=GUIAttributes.attributes.height;

    //子面板的宽
    public final int sonWidth=GUIAttributes.attributes.width/4;
    public final int sonHeight=GUIAttributes.attributes.height/2;

    private StatusPanel statusPanel=new StatusPanel(this);


    public DevicePanel devicePanel=new DevicePanel();


    public StatusAndDevicesInterface(){
        super();

        //布局设置
        BoxLayout boxLayout=new BoxLayout(this,BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);


        //添加子面板
        statusPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        this.add(statusPanel);
        devicePanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        this.add(devicePanel);


        //属性配置
        this.setPreferredSize(new Dimension(GUIAttributes.attributes.width/5,GUIAttributes.attributes.height));
        this.setBackground(Color.GREEN);
        this.setVisible(true);

    }

    public void init(List<Device> devices){
        this.updateDevices(devices);
    }
    //系统信息刷新
    public void updateStatus(){
        SwingUtilities.invokeLater(() -> statusPanel.updatePanel());
    }



    public void updateDevices(List<Device> deviceList){
        SwingUtilities.invokeLater(()->{
            this.devicePanel.updateDevices(deviceList);
        });
    }
}