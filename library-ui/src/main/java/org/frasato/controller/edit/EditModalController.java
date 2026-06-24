package org.frasato.controller.edit;

import org.frasato.model.BookModel;
import org.frasato.model.BookTableModel;
import org.frasato.service.DeleteBookService;
import org.frasato.service.UpdateBookService;
import org.frasato.view.home.HomeView;
import org.frasato.view.home.modal.EditModal;

import javax.swing.*;

public class EditModalController {
    public EditModalController(HomeView homeView, BookTableModel model){
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
            editModal.dispose();
        });

        JButton deleteButton = editModal.getDeleteButton();
        deleteButton.addActionListener(e ->  {
            deleteBookService.execute(bookModel.getId());
            editModal.dispose();
        });

        editModal.setVisible(true);
    }
}
