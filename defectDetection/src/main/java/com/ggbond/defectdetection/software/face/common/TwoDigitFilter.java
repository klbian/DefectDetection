package com.ggbond.defectdetection.software.face.common;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TwoDigitFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        // 过滤非数字字符
        if (!string.matches("\\d*")) {
            return;
        }
        // 限制输入长度为5
        if (fb.getDocument().getLength() + string.length() > 5) {
            return;
        }
        super.insertString(fb, offset, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        // 过滤非数字字符
        if (!text.matches("\\d*")) {
            return;
        }
        // 限制输入长度为5
//        if (fb.getDocument().getLength() - length + text.length() > 5) {
//            return;
//        }
//        super.replace(fb, offset, length, text, attrs);
    }
}
