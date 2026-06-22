package org.frasato.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BookTableModel extends AbstractTableModel {

    private final String[] columns = {
            "Título",
            "Publicação",
            "Editora",
            "Autor",
            "Livros Semelhantes"
    };

    private List<BookModel> books = new ArrayList<>();

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BookModel book = books.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> book.getTitle();
            case 1 -> book.getPublishDate();
            case 2 -> getFirstPublisher(book);
            case 3 -> getFirstAuthor(book);
            case 4 -> getFirstSimilarBook(book);
            default -> "";
        };
    }

    public void setBooks(List<BookModel> books) {
        this.books = books;
        fireTableDataChanged();
    }

    public void addBook(BookModel book) {
        books.add(book);
        fireTableRowsInserted(
                books.size() - 1,
                books.size() - 1
        );
    }

    public void clear() {
        books.clear();
        fireTableDataChanged();
    }

    public BookModel getBookAt(int row) {
        return books.get(row);
    }

    private String getFirstPublisher(BookModel book) {
        return book.getPublishers() != null &&
                !book.getPublishers().isEmpty()
                ? book.getPublishers().getFirst()
                : "";
    }

    private String getFirstAuthor(BookModel book) {
        return book.getAuthors() != null &&
                !book.getAuthors().isEmpty()
                ? book.getAuthors().getFirst().getName()
                : "";
    }

    private String getFirstSimilarBook(BookModel book) {
        return book.getSimilarBooks() != null &&
                !book.getSimilarBooks().isEmpty()
                ? book.getSimilarBooks().getFirst().getTitle()
                : "";
    }
}