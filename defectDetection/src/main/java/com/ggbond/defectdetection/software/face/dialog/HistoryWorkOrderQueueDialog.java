package com.ggbond.defectdetection.software.face.dialog;

import com.ggbond.defectdetection.pojo.WorkOrder;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.face.common.MultiLineTableCellRenderer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Author: 19461
 * Date: 2024/2/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class HistoryWorkOrderQueueDialog extends JDialog {


    private JScrollPane tablePanel = new JScrollPane();
    private JTable table = new JTable(){
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public HistoryWorkOrderQueueDialog() {
        init();
    }

    public void showDialog(Window parent, String title) {

        this.setLocationRelativeTo(parent);
        this.setVisible(true);
        this.setTitle(title);

        List<WorkOrder> workOrderList = CommonResource.getWorkOrderService().list();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        model.setRowCount(0);

        for (WorkOrder workOrder:workOrderList){

            Object[] rowData = {workOrder.getId(),
                    workOrder.getCreateId(),
                    workOrder.getCurrentNum(),
                    workOrder.getDetectSum() ,
                    formatter.format(workOrder.getCreateTime()),
                    workOrder.getIsOver()==1?"完成":"未完成",
                    workOrder.getFinishTime()!=null?formatter.format(workOrder.getFinishTime()):"",
                    workOrder.getRemark()};

            model.addRow(rowData);
        }


        model.fireTableDataChanged();
        table.setModel(model);
        table.updateUI();

//         将JTable重新绑定到JScrollPane中
        tablePanel.setViewportView(table);
        tablePanel.repaint();
    }

    public void init() {

        this.setAlwaysOnTop(true);         //总在最前面
        this.toFront();

        //配置面板
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(600, 320);
        this.setVisible(false);
        this.setResizable(false);

        //配置表格面板
        tablePanel.setPreferredSize(new Dimension(450, 290));
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tablePanel.add(table);
        tablePanel.setViewportView(table);

        //设置表格
        table.setDragEnabled(false);
//        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(new Dimension(450, 290));
        table.setSelectionBackground(new Color(89, 168, 105));
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int row = table.getSelectedRow();/*判断哪一行被选中了,如果没有选中，返回值为-1*/

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

        String[] columnNames = {"编号","创建者id", "当前检测数","检测总数", "创建时间","是否完成","完成时间" ,"备注"};


        DefaultTableModel model= (DefaultTableModel) table.getModel();

        for(String columnName:columnNames){
            model.addColumn(columnName);
        }

        table.getColumnModel().getColumn(3).setCellRenderer(new MultiLineTableCellRenderer());
        //添加表格
        this.add(tablePanel);

    }

}