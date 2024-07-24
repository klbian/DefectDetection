package com.ggbond.defectdetection.software.face.common;

import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.pojo.Warnings;
import com.ggbond.defectdetection.software.face.OperationAndOutputInterface;
import com.ggbond.defectdetection.software.face.StatusAndDevicesInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Author: 19461
 * Date: 2024/3/18
 */ //状态输出
@EqualsAndHashCode(callSuper = true)
@Data
public class OutputPanel extends JPanel {

    //标题
    private TitledBorder titledBorder = new TitledBorder("输出");

    //输出框
    private JScrollPane logPanel = new JScrollPane();
    private JTextArea2 logArea = new JTextArea2();

    public OutputPanel() {
        super();
        this.setPreferredSize(new Dimension(OperationAndOutputInterface.sonWidth, OperationAndOutputInterface.sonHeight));
        this.setBackground(new Color(242, 242, 242));

        //输出框
        logPanel.setPreferredSize(new Dimension(OperationAndOutputInterface.sonWidth - 130, OperationAndOutputInterface.sonHeight+200));

        //配置文本框
        logArea.setEditable(false); //不可编辑
        logArea.setLineWrap(true); //自动换行
        logArea.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        logArea.setPreferredSize(new Dimension(OperationAndOutputInterface.sonWidth - 130, OperationAndOutputInterface.sonHeight+180));

        //添加组件
        this.setBorder(titledBorder);
        logPanel.setViewportView(logArea);
        logPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(logPanel);

    }

    public void logOutput(SysLog sysLog) {

        if (sysLog.getOperatorType() == 0) {
            return;
        }

        //时间信息
        LocalDateTime date = sysLog.getOpTime();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm:ss", Locale.CHINA);
        String time = dateFormat.format(date);

        String singleLog = "%s : 操作主体:%s  操作:%s  对象:%s\n";
        logArea.append(singleLog.formatted(time, sysLog.getMainRole(), sysLog.getOperation(), sysLog.getTarget()));
    }

    public void warnOutput(Warnings warnings) {

        LocalDateTime date = warnings.getCreateTime();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm:ss", Locale.CHINA);
        String time = dateFormat.format(date);

        String singleWarn = "%s : %s: [%s]\n";
        logArea.append(singleWarn.formatted(time, warnings.getType(), warnings.getContent()));
    }

}