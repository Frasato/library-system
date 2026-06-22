package org.frasato.controller.home;

import org.frasato.model.AuthorModel;
import org.frasato.model.BookModel;
import org.frasato.model.BookTableModel;
import org.frasato.view.home.HomeView;

import javax.swing.*;
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
