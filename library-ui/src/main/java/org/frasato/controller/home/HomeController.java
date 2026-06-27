package org.frasato.controller.home;

import org.frasato.controller.edit.EditModalController;
import org.frasato.controller.include.IncludeModalController;
import org.frasato.controller.similar.SimilarModalController;
import org.frasato.model.BookTableModel;
import org.frasato.service.*;
import org.frasato.utils.FilterTable;
import org.frasato.view.home.HomeView;
import javax.swing.*;
import java.io.File;

public class HomeController {
    private final HomeView homeView;
    private final BookTableModel model;

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
        JButton incButton = homeView.getBottom().getIncludeButton();
        for(var listener : incButton.getActionListeners()){
            incButton.removeActionListener(listener);
        }
        incButton.addActionListener(e -> openIncludeModal());

        homeView.getBottom().getImportButton()
                .addActionListener(e -> importFile());

        JButton editButton = homeView.getBottom().getEditButton();
        for(var listener : editButton.getActionListeners()){
            editButton.removeActionListener(listener);
        }
        editButton.addActionListener(e -> openEditModal());

        JButton similarButton = homeView.getBottom().getSimilarButton();
        for(var listener : similarButton.getActionListeners()){
            similarButton.removeActionListener(listener);
        }
        similarButton.addActionListener(e -> openSimilarModal());

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

    private void openSimilarModal(){ new SimilarModalController(homeView, model); }

    private void loadBooks(){
        JProgressBar progressBar = homeView.getTable().getProgressBar();
        progressBar.setVisible(true);
        homeView.setEnabled(false);

        new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                ListBooksService listBooksService = new ListBooksService();
                model.setBooks(listBooksService.execute());
                return null;
            }

            @Override
            protected void done(){
                progressBar.setVisible(false);
                homeView.setEnabled(true);
            }

        }.execute();
    }
}
