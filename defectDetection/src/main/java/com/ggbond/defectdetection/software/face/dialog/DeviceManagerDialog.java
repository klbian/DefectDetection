package com.ggbond.defectdetection.software.face.dialog;

import com.ggbond.defectdetection.pojo.Device;
import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.service.DeviceService;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.info.Log;
import com.ggbond.defectdetection.software.info.OpEnum;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Author: 19461
 * Date: 2024/3/8
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Component
public class DeviceManagerDialog extends JDialog {

    @Autowired
    DeviceService deviceService;

    //主面板
    JPanel contentPanel=new JPanel();

    //设备表格
    JScrollPane tablePanel=new JScrollPane();
    JTable devicesTable=new JTable(){

        public boolean isCellEditable(int row, int column) {
            return false;
        }

    };

    //设备详情页
    JPanel detailPanel=new JPanel();
    JScrollPane detailScrollPanel=new JScrollPane();
    JTable detailTable=new JTable(){
        public boolean isCellEditable(int row, int column) {
            if(column==0){
                return false;
            }
            if(column==1){
                int[] rows={1,2,3,4,8};
                for (int i : rows) {
                    if(row==i){
                        return true;
                    }
                }
            }
            return false;
        }
        String editValue;
        public void editingStopped(ChangeEvent changeevent)
        {
            int r=getEditingRow();
            int c=getEditingColumn();
//            log.info(r+"--"+c);
            editValue = String.valueOf(this.getValueAt(r, c));
//            log.info(editValue);
            TableCellEditor tablecelleditor = getCellEditor();
            if(tablecelleditor != null)
            {
                Object obj = tablecelleditor.getCellEditorValue();
                this.setValueAt(obj, editingRow, editingColumn);
                removeEditor();
            }
        }
    };
    JButton saveButton=new JButton("保存");

    //操作按钮
    int w=50;
    int h=30;
    Dimension buttonSize=new Dimension(w,h);
    JPanel buttonPanel=new JPanel();
    JButton confirmButton=new JButton("确定");
    JButton delButton=new JButton("删除");
    JButton addButton=new JButton("添加");
    JButton testButton=new JButton("测试连接");

    //属性
    Dimension size=new Dimension(550, 270);

    List<Device> deviceList;

    public void showDialog(Window parent){

        this.setLocationRelativeTo(parent);
        this.setVisible(true);

        this.setTitle("设备管理");

        updateDeviceTable();

    }

    public DeviceManagerDialog(){

//        this.setAlwaysOnTop(true);         //总在最前面
        this.toFront();

        //配置面板
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(false);
        this.setResizable(false);

        contentPanel.setLayout(new BorderLayout());
//        contentPanel.setSize(size);
        contentPanel.setPreferredSize(size);
        contentPanel.setBackground(new Color(242,242,242));

        //设备表格
        Dimension deviceTableSize=new Dimension(320,220);
        tablePanel.setPreferredSize(deviceTableSize);
        tablePanel.setViewportView(devicesTable);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,10,10,10),BorderFactory.createLineBorder(Color.black,1)));

        devicesTable.setSelectionBackground(new Color(89, 168, 105));
        devicesTable.setDragEnabled(false);
        devicesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableModel model= (DefaultTableModel) devicesTable.getModel();

        String[] columnNames={"编号","类型","名称","备注"};
        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }
        devicesTable.setPreferredScrollableViewportSize(deviceTableSize);

        contentPanel.add(tablePanel,BorderLayout.CENTER);

        //细节表
        int detailPanelW=230;
        int detailPanelH=120;
        detailPanel.setPreferredSize(new Dimension(detailPanelW,detailPanelH));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,5));
        detailPanel.setLayout(new BorderLayout());


        detailScrollPanel.setViewportView(detailTable);
        detailScrollPanel.setPreferredSize(new Dimension(detailPanelW,detailPanelH));

        detailTable.setPreferredSize(new Dimension(detailPanelW,detailPanelH+40));
        detailTable.setPreferredScrollableViewportSize(new Dimension(detailPanelW,detailPanelH+40));
        detailTable.setBackground(new Color(238, 238, 238));
        detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        detailPanel.add(detailScrollPanel,BorderLayout.CENTER);


        JPanel buttonPanel2=new JPanel();

        saveButton.setPreferredSize(new Dimension(90,25));
        testButton.setPreferredSize(new Dimension(90,25));

        buttonPanel2.add(saveButton);
        buttonPanel2.add(testButton);
        buttonPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        detailPanel.add(buttonPanel2,BorderLayout.SOUTH);

        contentPanel.add(detailPanel,BorderLayout.EAST);

        //底部按钮
        FlowLayout flowLayout=new FlowLayout();
        buttonPanel.setLayout(flowLayout);

        buttonPanel.add(confirmButton);
        buttonPanel.add(delButton);
        buttonPanel.add(addButton);

        contentPanel.add(buttonPanel,BorderLayout.SOUTH);

        this.add(contentPanel,BorderLayout.CENTER);
        this.pack();
    }

    public void updateDeviceTable(){
        DefaultTableModel model= (DefaultTableModel) devicesTable.getModel();
        model.setRowCount(0);
        for(Device device:deviceList){
            Object[] row={
                    device.getId(),
                    device.getType(),
                    device.getName(),
                    device.getRemark()
            };
            model.addRow(row);
        }

        model.fireTableDataChanged();
    }


    @PostConstruct
    public void init(){

        deviceList=deviceService.list();

        devicesTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row=devicesTable.getSelectedRow();
                showDeviceDetail(deviceList.get(row));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


        confirmButton.addActionListener(e->{
            dispose();
        });

        saveButton.addActionListener(e->{
            clickSaveButton();
        });

        addButton.addActionListener(e->{
            clickAddButton();
        });

        delButton.addActionListener(e->{
            clickDelButton();
        });

        testButton.addActionListener(e->{
            clickTestButton();
        });
    }

    public void showDeviceDetail(Device device){

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String[] column1={"编号","类型","mac地址:","ip地址","名称","创建时间","修改时间","状态","备注:"};
        Object[] column2={
                device.getId(),
                device.getType(),
                device.getMac(),
                device.getIp(),
                device.getName(),
                device.getCreateTime()!=null?formatter.format(device.getCreateTime()):null,
                device.getUpdateTime()!=null?formatter.format(device.getUpdateTime()):null,
                device.getIsConnect()?"已连接":"未连接",
                device.getRemark()
        };

        DefaultTableModel model= (DefaultTableModel) detailTable.getModel();

        model.setColumnCount(0);
        model.addColumn("信息",column1);
        model.addColumn("值",column2);

        model.fireTableDataChanged();
    }

    public void clickSaveButton(){
        log.info("点击保存");
        SwingWorker<Void,Void> worker= new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                if(devicesTable.getSelectedRow()==-1&&!deviceList.isEmpty()){
                    JOptionPane.showMessageDialog(null, "请先选中设备", "通知", JOptionPane.INFORMATION_MESSAGE);
                    return null;
                }
                Device device = new Device(
                        (Integer) detailTable.getValueAt(0, 1),
                        Integer.parseInt(String.valueOf(detailTable.getValueAt(1, 1)) ) ,
                        (String) detailTable.getValueAt(2, 1),
                        (String) detailTable.getValueAt(3, 1),
                        (String) detailTable.getValueAt(4, 1),
                        (String) detailTable.getValueAt(8, 1),
                        null,
                        null,
                        null,
                        LocalDateTime.now(),
                        null,
                        null
                );
                if(device.getId()==null){
                    device.setCreateTime(device.getUpdateTime());
                }
                log.info(device.toString());
                boolean res=deviceService.saveOrUpdate(device);
                if (res) {
                    JOptionPane.showMessageDialog(null, "保存成功", "通知", JOptionPane.INFORMATION_MESSAGE);
                    deviceList = deviceService.list();
                    SysLog sysLog = new SysLog(
                            null,
                            LocalDateTime.now(),
                            OpEnum.Update.getName(),
                            CommonResource.getOperatorId(),
                            1,
                            Log.getClassToName().get(Device.class),
                            null,
                            0
                    );
                    Log.handlerLog(sysLog);
                    updateDeviceTable();

                } else {
                    JOptionPane.showMessageDialog(null, "保存失败", "通知", JOptionPane.WARNING_MESSAGE);
                }
                return null;
            }
        };
        worker.execute();
    }

    public void clickAddButton(){

        log.info("点击创建");
        DefaultTableModel model= (DefaultTableModel) detailTable.getModel();
        model.setRowCount(deviceList.size()+1);

        Device device=new Device();
        device.setCreateId(CommonResource.getOperatorId());
        device.setCreateTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());
        device.setUpdateId(CommonResource.getOperatorId());

        showDeviceDetail(device);

        SysLog sysLog = new SysLog(
                null,
                LocalDateTime.now(),
                OpEnum.Add.getName(),
                CommonResource.getOperatorId(),
                1,
                Log.getClassToName().get(Device.class),
                null,
                0
        );
        Log.handlerLog(sysLog);
        updateDeviceTable();
    }

    public void clickDelButton(){
        log.info("点击删除");
        SwingWorker<Void,Void> worker=new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {

                int rowIdx=devicesTable.getSelectedRow();

                int id= (int) devicesTable.getValueAt(rowIdx,0);
                log.info(String.valueOf(id));

                int res = JOptionPane.showConfirmDialog(null, "是否确认删除当前选中 id为:%d 设备".formatted(id), "删除", JOptionPane.YES_NO_OPTION);

                if(res==JOptionPane.YES_OPTION){
                    if(deviceService.removeById(id)){
                        JOptionPane.showMessageDialog(null, "删除成功", "通知", JOptionPane.INFORMATION_MESSAGE);
                        updateDeviceTable();
                        SysLog sysLog = new SysLog(
                                null,
                                LocalDateTime.now(),
                                OpEnum.Delete.getName(),
                                CommonResource.getOperatorId(),
                                1,
                                Log.getClassToName().get(Device.class),
                                null,
                                0
                        );

                        deviceList=deviceService.list();
                        Log.handlerLog(sysLog);
                        updateDeviceTable();

                    }else{
                        JOptionPane.showMessageDialog(null, "删除失败", "通知", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                return null;
            }
        };
        worker.execute();
    }

    public void clickTestButton(){
        log.info("点击测试");
        SwingWorker<Void,Void> worker=new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if(devicesTable.getSelectedRow()==-1){
                    JOptionPane.showMessageDialog(null, "请先选中设备", "通知", JOptionPane.INFORMATION_MESSAGE);
                    return null;
                }
                int rowIdx= (int) devicesTable.getSelectedRow();
                Device device=deviceList.get(rowIdx);

                boolean res=Device.testConnect(device);

                if(res){
                    device.setIsConnect(true);
                    JOptionPane.showMessageDialog(null, "连接成功", "通知", JOptionPane.INFORMATION_MESSAGE);
                    deviceService.updateById(device);
                    deviceList.get(rowIdx).setIsConnect(true);
                    showDeviceDetail(device);
                }else{
                    JOptionPane.showMessageDialog(null, "连接失败", "通知", JOptionPane.WARNING_MESSAGE);
                }

                return null;
            }
        };
        worker.execute();
    }
}