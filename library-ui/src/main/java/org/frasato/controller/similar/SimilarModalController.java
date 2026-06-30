package org.frasato.controller.similar;

import org.frasato.model.BookModel;
import org.frasato.model.BookTableModel;
import org.frasato.service.ListBooksService;
import org.frasato.service.SimilarBooksService;
import org.frasato.view.home.HomeView;
import org.frasato.view.home.modal.SimilarBookModal;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abre o modal de livros semelhantes para o livro selecionado na tabela.
 * Se nenhum livro estiver selecionado, exibe um aviso e não abre o modal.
 */
public class SimilarModalController {

    private SimilarBookModal view;
    private List<BookModel> books;
    private final List<JCheckBox> checkboxes = new ArrayList<>();

    public SimilarModalController(HomeView homeView, BookTableModel model) {
        JTable table = homeView.getTable().getTable();
        int selectedRow = table.getSelectedRow();

        if(selectedRow == -1){
            JOptionPane.showMessageDialog(homeView, "Selecione um livro!");
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        BookModel selectedBook = model.getBookAt(modelRow);

        this.view = new SimilarBookModal();
        loadBooks(selectedBook.getId().toString());

        JButton cancelButton = view.getCancelButton();
        cancelButton.addActionListener(e -> view.dispose());

        JButton addButton = view.getAddButton();
        addButton.addActionListener(e -> saveSelection(selectedBook.getId().toString()));

        view.setVisible(true);
    }

    private void loadBooks(String currentBookId) {
        books = new ListBooksService().execute();
        JPanel panel = view.getListPanel();
        panel.removeAll();
        checkboxes.clear();

        for (BookModel book : books) {
            if(book.getId().toString().equals(currentBookId)){
                continue;
            }

            JCheckBox checkbox = new JCheckBox(book.getTitle());
            checkboxes.add(checkbox);
            panel.add(checkbox, "growx");
        }

        panel.revalidate();
        panel.repaint();
    }

    private void saveSelection(String id) {
        List<BookModel> selected = getSelectedBooks();
        List<String> selectedBooksId = selected.stream().map(book -> book.getId().toString()).toList();

        SimilarBooksService similarBooksService = new SimilarBooksService();
        similarBooksService.execute(selectedBooksId, id);

        view.dispose();
    }

    private List<BookModel> getSelectedBooks() {
        List<BookModel> selected = new ArrayList<>();
        for (int i = 0; i < checkboxes.size(); i++) {
            if (checkboxes.get(i).isSelected()) {
                selected.add(books.get(i));
            }
        }
        return selected;
    }
}