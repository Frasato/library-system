package org.frasato.view.home.styles;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if(row % 2 == 0){
            setBackground(new Color(232, 232, 232));
        }else{
            setBackground(null);
        }

        if(isSelected) setBackground(new Color(172, 200, 19));

        return this;
    }
}
