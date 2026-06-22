package org.frasato.controller.home;

import org.frasato.model.AuthorModel;
import org.frasato.model.BookModel;
import org.frasato.model.BookTableModel;
import org.frasato.view.home.HomeView;
import org.frasato.view.home.modal.EditModal;
import org.frasato.view.home.modal.IncludeModal;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.UUID;

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
                .addActionListener(e -> new IncludeModal().setVisible(true));

        homeView.getBottom()
                .getImportButton()
                .addActionListener(e -> importFile());

        homeView.getBottom()
                .getEditButton()
                .addActionListener(e -> new EditModal().setVisible(true));
    }

    private void importFile(){
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showDialog(homeView, "Selecionar");

        if(result == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            System.out.println(file);
        }
    }

    private void loadBooks(){

        List<String> isbn = List.of("0049932551", "0049932333");
        List<String> publishers = List.of("Publisher 1", "Publisher 2");
        List<AuthorModel> author = List.of(
                new AuthorModel(UUID.randomUUID(), "Autor Numero 1", "01-03-1996")
        );

        List<BookModel> bookModels = List.of(
            new BookModel(UUID.randomUUID(), "Cronicas de Fogo e Gelo", "10-03-2024", isbn, publishers, author, null),
            new BookModel(UUID.randomUUID(), "Harry Pote", "03-12-2002", isbn, publishers, author, null)
        );
        model.setBooks(bookModels);
    }
}
