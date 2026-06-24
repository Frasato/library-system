package org.frasato.controller.include;

import org.frasato.service.AddBookService;
import org.frasato.view.home.modal.IncludeModal;
import javax.swing.*;

public class IncludeModalController {
    public IncludeModalController(){
        IncludeModal includeModal = new IncludeModal();

        JButton addButton = includeModal.getAddButton();
        addButton.addActionListener(e -> {
            AddBookService bookService = new AddBookService();
            bookService.execute(includeModal.getIsbnField());

            includeModal.dispose();
        });

        includeModal.setVisible(true);
    }
}
