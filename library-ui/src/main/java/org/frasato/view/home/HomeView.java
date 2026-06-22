package org.frasato.view.home;

import net.miginfocom.swing.MigLayout;
import org.frasato.model.BookTableModel;
import org.frasato.view.home.components.Filter;
import org.frasato.view.home.components.Header;
import org.frasato.view.home.components.Table;

import javax.swing.*;

public class HomeView extends JPanel {

    private Table table;

    public HomeView(BookTableModel model){ init(model); }

    public void init(BookTableModel model){
        setLayout(new MigLayout("fill", "[grow]", "[][][grow][]"));
        add(new Header(), "wrap");
        add(new Filter(), "growx, wrap");

        table = new Table(model);
        add(table, "grow, push");
    }

    public Table getTable(){
        return this.table;
    }
}
