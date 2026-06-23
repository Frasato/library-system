package org.frasato.controller.home;

import org.frasato.model.BookModel;
import org.frasato.model.BookTableModel;
import org.frasato.service.*;
import org.frasato.view.home.HomeView;
import org.frasato.view.home.modal.EditModal;
import org.frasato.view.home.modal.IncludeModal;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.io.File;
import java.util.regex.Pattern;

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
        homeView.getBottom()
                .getIncludeButton()
                .addActionListener(e -> openIncludeModal());

        homeView.getBottom()
                .getImportButton()
                .addActionListener(e -> importFile());

        homeView.getBottom()
                .getEditButton()
                .addActionListener(e -> openEditModal());

        homeView.getFilter()
                .getButton()
                .addActionListener(e -> actualize());
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

    private void filter(){
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

    private void openEditModal(){
        JTable table = homeView.getTable().getTable();
        int selectedRow = table.getSelectedRow();
        DeleteBookService deleteBookService = new DeleteBookService();
        UpdateBookService updateBookService = new UpdateBookService();

        if(selectedRow == -1){
            JOptionPane.showMessageDialog(homeView, "Selecione um livro para editar!");
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        BookModel bookModel = model.getBookAt(modelRow);

        EditModal editModal = new EditModal();

        editModal.setTitleValue(bookModel.getTitle());
        editModal.setPublishDateValue(bookModel.getPublishDate());
        editModal.setIsbnValue(String.join(", ", bookModel.getIsbn()));
        editModal.setPublisherValue(String.join(", ", bookModel.getPublishers()));
        if(bookModel.getAuthors() != null && !bookModel.getAuthors().isEmpty()){
            editModal.setAuthorValue(bookModel.getAuthors().getFirst().getName());
        }

        JButton finishButton = editModal.getFinishButton();
        finishButton.addActionListener(e -> {
            String response = updateBookService.execute(
                    editModal.getTitle(),
                    editModal.getPublishDate(),
                    editModal.getIsbn(),
                    editModal.getPublisher(),
                    bookModel.getId().toString()
            );

            JOptionPane.showMessageDialog(homeView, response);
        });

        JButton deleteButton = editModal.getDeleteButton();
        deleteButton.addActionListener(e -> deleteBookService.execute(bookModel.getId()));

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

    private void actualize(){
        loadBooks();
    }

    private void loadBooks(){
        ListBooksService listBooksService = new ListBooksService();
        model.setBooks(listBooksService.execute());
    }
}
