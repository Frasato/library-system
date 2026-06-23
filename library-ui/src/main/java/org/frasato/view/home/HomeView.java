package org.frasato.view.home;

import net.miginfocom.swing.MigLayout;
import org.frasato.model.BookTableModel;
import org.frasato.view.home.components.Bottom;
import org.frasato.view.home.components.Filter;
import org.frasato.view.home.components.Table;
import javax.swing.*;

public class HomeView extends JPanel {

    private Table table;
    private Bottom bottom;
    private Filter filter;

    public HomeView(BookTableModel model){ init(model); }

    public void init(BookTableModel model){
        setLayout(new MigLayout("fill", "[grow]", "[][grow][]"));

        filter = new Filter();
        add(filter, "growx, wrap");

        table = new Table(model);
        add(table, "grow, push, wrap");

        bottom = new Bottom();
        add(bottom);
    }

    public Bottom getBottom(){ return this.bottom; }

    public Table getTable(){
        return this.table;
    }

    public Filter getFilter() { return this.filter; }
}
