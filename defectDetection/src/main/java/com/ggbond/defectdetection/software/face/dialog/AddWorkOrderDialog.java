package com.ggbond.defectdetection.software.face.dialog;

import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.pojo.WorkOrder;
import com.ggbond.defectdetection.service.WorkOrderService;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.common.SysStatus;
import com.ggbond.defectdetection.software.face.common.JTextArea2;
import com.ggbond.defectdetection.software.info.Log;
import com.ggbond.defectdetection.software.info.OpEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * 添加工单对话框
 * <p>
 * Author: 19461
 * Date: 2024/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class AddWorkOrderDialog extends JDialog {

    @Autowired
    WorkOrderService workOrderService;

    //输入框panel
    private JPanel inputsPanel=new JPanel();

    //输入内容
    //检测数量
    JLabel detectSumLabel=new JLabel("检测数量:");
    JSpinner detectSumText=new JSpinner();
    JPanel detectSumPanel=new JPanel();

    //备注
    JLabel remarkTextLabel=new JLabel("备注:");
    JTextArea2 remarkText=new JTextArea2();
    JPanel remarkPanel=new JPanel();

    //按钮面板
    JPanel buttonPanel=new JPanel();

    //按钮
    private JButton confirmButton=new JButton("确认");
    private JButton cancelButton=new JButton("取消");

    public AddWorkOrderDialog(){
        super();
        init();
    }

    public void showDialog(Window parent){
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }


    public void init(){
        this.setAlwaysOnTop(true);         //总在最前面
        this.setModal(true);
        this.setTitle("创建工单");
        this.toFront();
        //配置面板
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(500,320);
        this.setVisible(false);

        //设置输入面板
        inputsPanel.setBorder(new EmptyBorder(10,10,10,10));

        //设置输入框
        SpinnerModel model=new SpinnerNumberModel(1,1,99999,100);
        detectSumText.setModel(model);
        detectSumText.setToolTipText("检测总数");
        remarkText.setToolTipText("备注信息");
        remarkText.setPreferredSize(new Dimension(300,100));


        //添加输入框
        detectSumPanel.add(detectSumLabel);
        detectSumPanel.add(detectSumText);
        remarkPanel.add(remarkTextLabel);
        remarkPanel.add(remarkText);

        FlowLayout flowLayout=new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        detectSumPanel.setLayout(flowLayout);
        detectSumPanel.setBorder(new EmptyBorder(0,0,0,250));

        inputsPanel.setLayout(new BoxLayout(inputsPanel,BoxLayout.Y_AXIS));
//        inputsPanel.add(Box.createVerticalStrut(50));
        inputsPanel.add(detectSumPanel);
        inputsPanel.add(remarkPanel);

        //添加输入面板
        this.add(BorderLayout.CENTER,inputsPanel);

        //设置按钮面板
        flowLayout.setAlignment(FlowLayout.CENTER);
        buttonPanel.setLayout(flowLayout);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        this.add(BorderLayout.SOUTH,buttonPanel);
        this.pack();

        //添加按钮点击事件
        confirmButton.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(null,"创建新工单成功","创建",JOptionPane.INFORMATION_MESSAGE);
            Integer detectSum= (Integer) detectSumText.getValue();
            String remark=remarkText.getText();
            WorkOrder workOrder=new WorkOrder();
            workOrder.setCurrentNum(0);
            workOrder.setDetectSum(detectSum);
            workOrder.setRemark(remark);
            workOrder.setCreateTime(LocalDateTime.now());
            workOrder.setCreateId(CommonResource.getOperatorId());
            workOrderService.save(workOrder);
            CommonResource.addNewWorkOrder(workOrder);

            remarkText.setText("");
            detectSumText.setValue(1);

            SysLog sysLog=new SysLog(null,
                    LocalDateTime.now(),
                    OpEnum.Create.getName(),
                    CommonResource.getOperatorId(),
                    1,
                    Log.getClassToName().get(WorkOrder.class),
                    null,
                    null);
            Log.handlerLog(sysLog);
        });
        cancelButton.addActionListener(e -> {
            dispose();
        });
    }


}