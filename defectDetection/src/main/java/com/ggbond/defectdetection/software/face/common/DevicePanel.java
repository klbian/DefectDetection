package com.ggbond.defectdetection.software.face.common;

import com.ggbond.defectdetection.pojo.Device;
import com.ggbond.defectdetection.software.face.DeviceLabel;
import com.ggbond.defectdetection.software.face.OperationAndOutputInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: 19461
 * Date: 2024/3/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DevicePanel extends JPanel {
    //标题
    TitledBorder titledBorder = new TitledBorder("设备状态");

    //设备队列
    List<DeviceLabel> deviceLabelList = new ArrayList<>();

    public DevicePanel() {
        super();
        //标题
        this.setBorder(titledBorder);
        //配置
        this.setPreferredSize(new Dimension(OperationAndOutputInterface.sonWidth, OperationAndOutputInterface.sonHeight));

        //设置布局
        FlowLayout flowLayout = new FlowLayout();

        this.setLayout(flowLayout);
    }

    public void updateDevices(List<Device> deviceList) {

        for (Device device : deviceList) {
            DeviceLabel deviceLabel = new DeviceLabel(device);
            this.add(deviceLabel);
            deviceLabelList.add(deviceLabel);
        }
        this.repaint();
    }
}