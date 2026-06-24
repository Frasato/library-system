package org.frasato.controller.home;

import org.frasato.controller.edit.EditModalController;
import org.frasato.controller.include.IncludeModalController;
import org.frasato.model.BookTableModel;
import org.frasato.service.*;
import org.frasato.utils.FilterTable;
import org.frasato.view.home.HomeView;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.io.File;

public class HomeController {
    private final HomeView homeView;
    private final BookTableModel model;
    private TableRowSorter<BookTableModel> sorter;

    public HomeController(){
        model = new BookTableModel();
        homeView = new HomeView(model);

        init();
    }

    public JPanel view(){return homeView;}

    private void init(){
        loadBooks();
        bindEvents();
        filter();
    }

    private void bindEvents(){
        homeView.getBottom().getIncludeButton()
                .addActionListener(e -> openIncludeModal());

        homeView.getBottom().getImportButton()
                .addActionListener(e -> importFile());

        homeView.getBottom().getEditButton()
                .addActionListener(e -> openEditModal());

        homeView.getFilter().getButton()
                .addActionListener(e -> loadBooks());
    }

    private void importFile(){
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showDialog(homeView, "Selecionar");
        ImportBookService importBookService = new ImportBookService();

        if(result == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            importBookService.execute(file);
        }
    }

    private void filter(){ new FilterTable().execute(homeView, model);}

    private void openEditModal(){ new EditModalController(homeView, model); }

    private void openIncludeModal(){ new IncludeModalController(); }

    private void loadBooks(){
        ListBooksService listBooksService = new ListBooksService();
        model.setBooks(listBooksService.execute());
    }
}
