package com.ggbond.defectdetection.software.face.dialog;

import com.ggbond.defectdetection.pojo.Operator;
import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.pojo.Warnings;
import com.ggbond.defectdetection.service.WarningsService;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.common.ConfigProperties;
import com.ggbond.defectdetection.software.common.SysStatus;
import com.ggbond.defectdetection.software.face.common.MultiLineTableCellRenderer;
import com.ggbond.defectdetection.software.info.Log;
import com.ggbond.defectdetection.software.info.LogPoint;
import com.ggbond.defectdetection.software.info.OpEnum;
import com.ggbond.defectdetection.software.info.Warn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Author: 19461
 * Date: 2024/2/29
 */
//异常配置窗口
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@Slf4j
public class ExceptionConfigDialog extends JDialog {

    @Autowired
    WarningsService warningsService;

    //界面1--异常配置信息
    private JPanel warnConfigPanel=new JPanel();
    private List<JPanel> settingPanels= new LinkedList<>();
    private JButton saveButton=new JButton("保存");

    JTextField[] textFields=new JTextField[3];

    //界面2--异常表格
    private JScrollPane tablePanel=new JScrollPane();
    private JTable table=new JTable(){
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public ExceptionConfigDialog(){
        init();
    }
    public void showDialog(Window parent){

        this.setLocationRelativeTo(parent);
        this.setVisible(true);

        List<Warnings> warningsList = warningsService.list();

        DefaultTableModel model= (DefaultTableModel) table.getModel();


        model.setRowCount(0);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        for(Warnings warnings:warningsList){
            Object[] rowData={warnings.getId(),dateFormat.format(warnings.getCreateTime()),warnings.getLevel(),warnings.getType(),warnings.getContent()};
            model.addRow(rowData);
        }

        //设置界面1数值
        String[] defaultValue={String.valueOf((int)(Warn.getDefectiveRateWarningLine()*100)), String.valueOf(Warn.getContinuousWorkingMinutesWarningLine()), String.valueOf(Warn.getWarningInterval())};
        for (int i = 0; i < 3; i++) {
            textFields[i].setText(defaultValue[i]);
            textFields[i].updateUI();
        }

        model.fireTableDataChanged();
        table.setModel(model);
        table.updateUI();
        tablePanel.updateUI();

    }

    public void init(){

        this.setAlwaysOnTop(true);         //总在最前面
        this.toFront();

        int width=700;
        int height=320;

        //配置面板
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(width,height);
        this.setPreferredSize(new Dimension(width,height));
        this.setVisible(false);
        this.setResizable(false);

        //界面1配置
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new JTextField();
        }
        int inputWidth=100;
        int inputHeight=25;
        String[] configItemsName={"缺陷率警告线","持续工作时长警告线","警告触发间隔"};
        for(int i=0;i<3;i++){
            String itemName=configItemsName[i];
            JPanel jPanel=new JPanel();
            jPanel.setLayout(new FlowLayout());

            JLabel name=new JLabel(itemName+":  ");
            textFields[i].setPreferredSize(new Dimension(inputWidth,inputHeight));
            textFields[i].setName(itemName);
            textFields[i].setColumns(5);
//            ((AbstractDocument) textFields[i].getDocument()).setDocumentFilter(new TwoDigitFilter());
            textFields[i].setForeground(Color.black);

            JLabel unit=new JLabel("%");
            if(i==0){
                unit.setText("%");
            }else if(i==1){
                unit.setText("s");
            }else {
                unit.setText("s");
            }
            jPanel.add(name);
            jPanel.add(textFields[i]);
            jPanel.add(unit);
            jPanel.setBorder(new EmptyBorder(10,10,10,10));

            settingPanels.add(jPanel);
        }

        saveButton.setPreferredSize(new Dimension(30,15));
        saveButton.addActionListener(e->{
            clickSaveConfig();
        });

        warnConfigPanel.setLayout(new BoxLayout(warnConfigPanel,BoxLayout.Y_AXIS));


        for(JPanel panel:settingPanels){

            warnConfigPanel.add(panel);
        }
        warnConfigPanel.add(saveButton);

        this.add(warnConfigPanel,BorderLayout.WEST);
        warnConfigPanel.setBorder(new TitledBorder("异常属性配置"));


        //界面2配置
        Dimension tableSize=new Dimension(550,245);

        tablePanel.setPreferredSize(tableSize);
        table.setPreferredSize(tableSize);
        table.setPreferredScrollableViewportSize(tableSize);

        tablePanel.setViewportView(table);
        tablePanel.setBorder(new EmptyBorder(10,10,10,10));

        //加载列名
        String[] columnNames={"编号","警告时间","级别","类型","内容"};

        DefaultTableModel model= (DefaultTableModel) table.getModel();

        for(String columnName:columnNames){
            model.addColumn(columnName);
        }
        table.setEnabled(false);
        table.getColumnModel().getColumn(4).setCellRenderer(new MultiLineTableCellRenderer());

        this.add(tablePanel,BorderLayout.CENTER);

    }
    @LogPoint(value = OpEnum.Update,mainRole = Operator.class,target = ConfigProperties.class)
    public void clickSaveConfig(){
        log.info("点击保存");
        SwingWorker<Void,Void> worker=new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                ConfigProperties.WarnsConfig warnsConfig=new ConfigProperties.WarnsConfig();
                List<String> configs=new ArrayList<>();
                for (int i=0;i<3;i++) {
                    if(!textFields[i].getText().matches("\\d*")){
                        log.info(textFields[i].getText());
                        JOptionPane.showMessageDialog(warnConfigPanel,"含有除数字外的字符","格式错误",JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    configs.add(textFields[i].getText());
                }
                warnsConfig.setDefectiveRateWarningLine(Integer.parseInt(configs.get(0))/100.0);
                warnsConfig.setContinuousWorkingMinutesWarningLine(Integer.parseInt(configs.get(1)));
                warnsConfig.setWaringInterval((long) Integer.parseInt(configs.get(2)));
                warnsConfig.saveConfig();
                JOptionPane.showMessageDialog(warnConfigPanel,"保存成功","通知",JOptionPane.INFORMATION_MESSAGE);

                SysLog sysLog=new SysLog(null,
                        LocalDateTime.now(),
                        OpEnum.Update.getName(),
                        CommonResource.getOperatorId(),
                        1,
                        Log.getClassToName().get(Warnings.class),
                        null,
                        null);
                Log.handlerLog(sysLog);

                return null;
            }
        };
        worker.execute();
        this.dispose();
    }

}
