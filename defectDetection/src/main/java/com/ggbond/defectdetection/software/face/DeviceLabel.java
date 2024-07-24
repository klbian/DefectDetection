package com.ggbond.defectdetection.software.face;

import com.ggbond.defectdetection.pojo.Device;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;

/**
 * 设备标签,用于显示设备信息
 * <p>
 * Author: 19461
 * Date: 2024/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceLabel extends JLabel {

    private int width=GUIAttributes.attributes.width/8;

    private int height=GUIAttributes.attributes.getHeight()/15;

    private Device device;


    public DeviceLabel(){
        super();
        this.setPreferredSize(new Dimension(width,height));  //大小
        this.setBorder(BorderFactory.createRaisedBevelBorder()); //突出显示边框
        this.setBackground(new Color(240,240,240)); //背景色
        this.setAlignmentX(0.5F);  //居中显示
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setFont(new Font("黑体", Font.PLAIN,15));
    }
    public DeviceLabel(Device device){
        super();
        this.setPreferredSize(new Dimension(width,height));  //大小
        this.setBorder(BorderFactory.createRaisedBevelBorder()); //突出显示边框
        this.setBackground(new Color(240,240,240)); //背景色
        this.setAlignmentX(0.5F);  //居中显示
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setFont(new Font(null, Font.PLAIN,15));
        setDevice(device);
    }

    public void setDevice(Device device){
        this.device=device;
        String content="<html>" +
                "<p>序号:%d &nbsp;类型:%d</p>" +
                "<p>名称:%s &nbsp;&nbsp;%s" +
                "</html>";
        //对注释处理
        String remark=device.getRemark();
        if(remark!=null){
            if(remark.length()>8) {
                this.setToolTipText(remark);
                remark = remark.replace(remark.substring(9), "...");
            }
        }else {
            remark="";
        }
        //设置文本
        this.setText(content.formatted(device.getId(),device.getType(),device.getName(),remark));
    }
}