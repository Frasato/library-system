package org.frasato.view.home.components;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.frasato.model.BookTableModel;
import javax.swing.*;

public class Table extends JPanel {

    private JTable table;

    public Table(BookTableModel model){ init(model); }

    public void init(BookTableModel model){

        setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
        putClientProperty(FlatClientProperties.STYLE, "arc: 6;");

        table = new JTable(model);

        table.setRowHeight(40);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, "grow, push");
    }

    public JTable getTable(){
        return table;
    }
}
