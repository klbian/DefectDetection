package com.ggbond.defectdetection.software.face;

import com.ggbond.defectdetection.pojo.Device;
import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.pojo.Warnings;
import com.ggbond.defectdetection.software.face.common.DevicePanel;
import com.ggbond.defectdetection.software.face.common.OpPanel;
import com.ggbond.defectdetection.software.face.common.OutputPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 系统操作和外接设备界面类
 * <p>
 * Author: 19461
 * Date: 2024/2/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@Slf4j
public class OperationAndOutputInterface extends JPanel {

    //子组件的宽和高
    public static int sonWidth=GUIAttributes.attributes.getWidth()/4;
    public static int sonHeight= (int) (GUIAttributes.attributes.getHeight()*0.4);

    //系统操作
    @Autowired
    private OpPanel opPanel;

    //设备
    public OutputPanel outputPanel=new OutputPanel();

    public OperationAndOutputInterface(){
        super();
    }

    public void init(){

        //设置布局
        BorderLayout borderLayout=new BorderLayout();
        this.setLayout(borderLayout);
        //添加子组件
//        log.info(opPanel.toString());
        this.add(opPanel.getContentPanel(),BorderLayout.NORTH);
        this.add(outputPanel,BorderLayout.CENTER);

        //界面属性
        this.setPreferredSize(new Dimension(GUIAttributes.attributes.width/5, GUIAttributes.attributes.height));
        this.setBackground(new Color(242,242,242));
        this.setVisible(true);

        this.opPanel.init();
    }
    public void updateOutput(SysLog sysLog, Warnings warnings){

        SwingUtilities.invokeLater(()->{
            if(sysLog!=null){
                outputPanel.logOutput(sysLog);
            }
            if(warnings!=null) {
                outputPanel.warnOutput(warnings);
            }
        });
    }

}