package org.frasato.utils;

import org.frasato.model.BookTableModel;
import org.frasato.view.home.HomeView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.util.regex.Pattern;

public class FilterTable {
    private TableRowSorter<BookTableModel> sorter;

    public void execute(HomeView homeView, BookTableModel model){
        JTable table = homeView.getTable().getTable();
        this.sorter = new TableRowSorter<>(model);

        table.setRowSorter(sorter);

        Runnable applyFilter = () -> {
            String text = homeView.getFilter().getText().trim();
            if(text.isBlank()){
                sorter.setRowFilter(null);
                return;
            }

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
        };

        homeView.getFilter()
                .getFilterField()
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {
                            @Override
                            public void insertUpdate(DocumentEvent e) {
                                applyFilter.run();
                            }

                            @Override
                            public void removeUpdate(DocumentEvent e) {
                                applyFilter.run();
                            }

                            @Override
                            public void changedUpdate(DocumentEvent e) {
                                applyFilter.run();
                            }
                        }
                );
    }
}
