package org.frasato.controller.home;

import org.frasato.model.BookTableModel;
import org.frasato.service.AddBookService;
import org.frasato.service.ListBooksService;
import org.frasato.view.home.HomeView;
import org.frasato.view.home.modal.EditModal;
import org.frasato.view.home.modal.IncludeModal;
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
    }

    private void bindEvents(){
        homeView.getBottom()
                .getIncludeButton()
                .addActionListener(e -> openIncludeModal());

        homeView.getBottom()
                .getImportButton()
                .addActionListener(e -> importFile());

        homeView.getBottom()
                .getEditButton()
                .addActionListener(e -> openEditModal());
    }

    private void importFile(){
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showDialog(homeView, "Selecionar");

        if(result == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            System.out.println(file);
        }
    }

    private void openEditModal(){
        EditModal editModal = new EditModal();

        JButton finishButton = editModal.getFinishButton();
        JButton deleteButton = editModal.getDeleteButton();

        editModal.setVisible(true);
    }

    private void openIncludeModal(){
        IncludeModal includeModal = new IncludeModal();

        JButton addButton = includeModal.getAddButton();
        addButton.addActionListener(e -> {
            AddBookService bookService = new AddBookService();
            bookService.execute(includeModal.getIsbnField());
        });

        includeModal.setVisible(true);
    }

    private void loadBooks(){
        ListBooksService listBooksService = new ListBooksService();
        model.setBooks(listBooksService.execute());
    }
}
