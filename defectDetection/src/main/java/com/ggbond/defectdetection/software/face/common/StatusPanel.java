package com.ggbond.defectdetection.software.face.common;

import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.face.StatusAndDevicesInterface;
import com.ggbond.defectdetection.util.SystemStatusUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: 19461
 * Date: 2024/3/18
 */ //系统运行状态
@EqualsAndHashCode(callSuper = true)
@Data
public class StatusPanel extends JPanel {


    //标题
    private TitledBorder titledBorder = new TitledBorder("系统信息");

    //label的边框
    private EmptyBorder emptyBorder = new EmptyBorder(0, 20, 0, 0);

    //当前时间
    private Date now = new Date();
    JLabel nowLabel = new JLabel();

    //GPU,CPU
    JLabel CPUAndGPULabel = new JLabel();

    //内存
    JLabel memoryLabel = new JLabel();

    //持续工作时长
    JLabel continuousWorkingHoursLabel = new JLabel();

    //总检测数
    JLabel totalDetectLabel = new JLabel();

    //系统状态
    JLabel sysStatusLabel = new JLabel();


    public StatusPanel(StatusAndDevicesInterface statusAndDevicesInterface) {

        super();
        this.setPreferredSize(new Dimension(statusAndDevicesInterface.sonWidth, statusAndDevicesInterface.sonHeight));
        this.setBackground(new Color(242, 242, 242));


        //设置布局
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);

        //标题
        titledBorder.setTitleFont(new Font("黑体", Font.BOLD, 25));
        this.setBorder(titledBorder);

        //设置字体
        Font font = new Font("宋体 ", Font.PLAIN, 17);
        nowLabel.setFont(font);
        CPUAndGPULabel.setFont(font);
        memoryLabel.setFont(font);
        continuousWorkingHoursLabel.setFont(font);
        totalDetectLabel.setFont(font);
        sysStatusLabel.setFont(font);


        //设置大小
        Dimension labelSize = new Dimension(statusAndDevicesInterface.sonWidth - 50, statusAndDevicesInterface.sonHeight / 8);
        nowLabel.setPreferredSize(labelSize);
        CPUAndGPULabel.setPreferredSize(labelSize);
        memoryLabel.setPreferredSize(labelSize);
        continuousWorkingHoursLabel.setPreferredSize(labelSize);
        totalDetectLabel.setPreferredSize(labelSize);
        sysStatusLabel.setPreferredSize(labelSize);

        //设置边框,控制左间距
        nowLabel.setBorder(emptyBorder);
        CPUAndGPULabel.setBorder(emptyBorder);
        memoryLabel.setBorder(emptyBorder);
        continuousWorkingHoursLabel.setBorder(emptyBorder);
        totalDetectLabel.setBorder(emptyBorder);
        sysStatusLabel.setBorder(emptyBorder);


        //设置对齐位置
        nowLabel.setHorizontalAlignment(JLabel.CENTER);
        CPUAndGPULabel.setHorizontalAlignment(JLabel.CENTER);
        memoryLabel.setHorizontalAlignment(JLabel.CENTER);
        continuousWorkingHoursLabel.setHorizontalAlignment(JLabel.CENTER);
        totalDetectLabel.setHorizontalAlignment(JLabel.CENTER);
        sysStatusLabel.setHorizontalAlignment(JLabel.CENTER);


        //添加组件并设置间距
        int verticalPad = 5;
        this.add(nowLabel);
        this.add(Box.createVerticalStrut(verticalPad));
        this.add(CPUAndGPULabel);
        this.add(Box.createVerticalStrut(verticalPad));
        this.add(memoryLabel);
        this.add(Box.createVerticalStrut(verticalPad));
        this.add(continuousWorkingHoursLabel);
        this.add(Box.createVerticalStrut(verticalPad));
        this.add(totalDetectLabel);
        this.add(Box.createVerticalStrut(verticalPad));
        this.add(sysStatusLabel);


        this.updatePanel();
    }

    //更新状态面板
    public void updatePanel() {
        //获取时间
        now.setTime(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.CHINA);

        nowLabel.setText("当前时间: " + dateFormat.format(now));

        //获取CPU GPU
        String[] res = SystemStatusUtil.getStatus();
        CPUAndGPULabel.setText("CPU使用率: " + res[0] + "   GPU使用率: " + res[2]);

        // 内存状态
        memoryLabel.setText("内存使用率: " + res[1]);
        //系统持续工作时长
        continuousWorkingHoursLabel.setText("持续工作时长:  " + SystemStatusUtil.getContinuousWorkingSeconds() + "s");

        //总检测次数
        totalDetectLabel.setText("本次运行检测总数: " + CommonResource.getTotalDetect() + "次");

        //设置系统状态
        sysStatusLabel.setText("当前系统状态:  " + CommonResource.getStatus().getName());

    }
}