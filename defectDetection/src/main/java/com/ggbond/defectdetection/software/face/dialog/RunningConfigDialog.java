package com.ggbond.defectdetection.software.face.dialog;

import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.pojo.Warnings;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.common.ConfigProperties;
import com.ggbond.defectdetection.software.info.Log;
import com.ggbond.defectdetection.software.info.OpEnum;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: 19461
 * Date: 2024/3/5
 */
@Component
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class RunningConfigDialog extends JDialog {

    private JPanel contentPanel=new JPanel();
    private List<JTextField> textFieldList=new ArrayList<>();

    String[] inputNames={"存储路径","检测时间间隔"};

    //存储路径
    JLabel dirSavePathLabel=new JLabel(inputNames[0]);
    JFileChooser fc=new JFileChooser();
    JTextField fcPathText=new JTextField();
    JButton fcDialogButton=new JButton();
    String storagePath=ConfigProperties.properties.getModelConfig().getResStoragePath();

    //检测时间间隔
    JLabel detectIntervalLabel=new JLabel(inputNames[1]);
    JTextField detectIntervalText=new JTextField();
    JLabel unit=new JLabel("s");

    //保存按钮
    JButton saveButton=new JButton("保存");

    public RunningConfigDialog(){
        super();

        int width=300;
        int height=150;

        this.setAlwaysOnTop(true);         //总在最前面
        this.setModal(true);
        this.setTitle("运行属性配置");
        this.toFront();
        //配置面板
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(width,height);
        this.setVisible(false);


        contentPanel.setPreferredSize(new Dimension(width,height));
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));

        //1
        JPanel jPanel1=new JPanel();
        jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));

        fcPathText.setEditable(false);
        fcPathText.setColumns(16);
        fcPathText.setPreferredSize(new Dimension(40,jPanel1.getHeight()));

        fcDialogButton.setSize(21,21);
        fcDialogButton.setHideActionText(true);
        fcDialogButton.setIcon(new ImageIcon("D:\\java.java\\defectDetection\\src\\main\\resources\\icon\\filechoosericon.png"));
        fcDialogButton.setBorder(null);

        jPanel1.add(dirSavePathLabel);
        jPanel1.add(fcPathText);
        jPanel1.add(fcDialogButton);

        contentPanel.add(jPanel1);

        //2
        JPanel jPanel2=new JPanel();
        jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));

        detectIntervalText.setColumns(3);

        jPanel2.add(detectIntervalLabel);
        jPanel2.add(detectIntervalText);
        jPanel2.add(unit);

        contentPanel.add(jPanel2);

        //3
        saveButton.setPreferredSize(new Dimension(40,20));
        saveButton.addActionListener(e->{
            log.info("点击保存");
            clickSaveButton();
        });
        saveButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        saveButton.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(saveButton);


        this.add(contentPanel);
    }

    @PostConstruct
    public void init(){

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fcPathText.setPreferredSize(new Dimension(80,15));
        fcDialogButton.addActionListener(e->{
            int res = fc.showDialog(this, "确认");
            if(res == JFileChooser.APPROVE_OPTION){
                fcPathText.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

    }

    public void showDialog(Window parent){

        detectIntervalText.setText(String.valueOf(ConfigProperties.properties.getRunningConfig().getDetectInterval()));
        fcPathText.setText(ConfigProperties.properties.getModelConfig().getResStoragePath());

        detectIntervalText.updateUI();
        fcPathText.updateUI();

        this.setLocationRelativeTo(parent);
        this.setVisible(true);

    }

    public void clickSaveButton(){

        SwingWorker<Void,Void> worker= new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                String path = fcPathText.getText();
                String interval = detectIntervalText.getText();

                if (!interval.matches("\\d*")) {
                    JOptionPane.showMessageDialog(contentPanel, "含有非数字字符", "格式错误", JOptionPane.ERROR_MESSAGE);
                    return null;
                } else {
                    ConfigProperties.properties.getModelConfig().setResStoragePath(path);
                    ConfigProperties.properties.getModelConfig().saveConfig();

                    ConfigProperties.properties.getRunningConfig().setDetectInterval(Integer.parseInt(interval));
                    ConfigProperties.properties.getRunningConfig().saveConfig();
                    JOptionPane.showMessageDialog(contentPanel, "保存成功,重启系统后生效", "通知", JOptionPane.INFORMATION_MESSAGE);
                    SysLog sysLog=new SysLog(null,
                            LocalDateTime.now(),
                            OpEnum.Add.getName(),
                            CommonResource.getOperatorId(),
                            1,
                            Log.getClassToName().get(ConfigProperties.class),
                            null,
                            null);
                    Log.handlerLog(sysLog);
                    dispose();
                }
                return null;
            }
        };
        worker.execute();
    }
}