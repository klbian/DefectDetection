package com.ggbond.defectdetection.software.face.common;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Author: 19461
 * Date: 2024/3/1
 */ // 自动换行渲染器
public class MultiLineTableCellRenderer extends DefaultTableCellRenderer {
    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // 调用父类的方法获取渲染组件
        java.awt.Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 设置单元格的换行模式
        if (cell instanceof JTextArea) {
            JTextArea textArea = (JTextArea) cell;
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
        } else if (cell instanceof JEditorPane) {
            JEditorPane editorPane = (JEditorPane) cell;
            editorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
            editorPane.setFont(UIManager.getFont("Label.font"));
            editorPane.setBackground(UIManager.getColor("Label.background"));
            editorPane.setEditable(false);
        }

        return cell;
    }
}