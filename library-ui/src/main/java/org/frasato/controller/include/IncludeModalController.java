package org.frasato.controller.include;

import org.frasato.service.AddBookService;
import org.frasato.view.home.modal.IncludeModal;
import javax.swing.*;
import java.util.Arrays;

/**
 * Abre o modal de inclusão de livro e executa o cadastro em background (SwingWorker),
 * evitando travar a interface durante a chamada ao serviço.
 */
public class IncludeModalController {
    public IncludeModalController(){
        IncludeModal includeModal = new IncludeModal();

        JButton addButton = includeModal.getAddButton();
        addButton.addActionListener(e -> {
            addButton.setEnabled(false);
            includeModal.getProgressBar().setVisible(true);

            new SwingWorker<String, Void>(){

                @Override
                protected String doInBackground() {
                    AddBookService bookService = new AddBookService();
                    return bookService.execute(includeModal.getIsbnField());
                }

                @Override
                protected void done(){
                    try{
                        String message = get();
                        JOptionPane.showMessageDialog(includeModal, message);
                        includeModal.dispose();
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(includeModal, "Erro ao adicionar livro.");
                        throw new RuntimeException(Arrays.toString(ex.getStackTrace()));
                    }finally {
                        includeModal.getProgressBar().setVisible(false);
                        addButton.setEnabled(true);
                    }
                }
            }.execute();
        });

        includeModal.setVisible(true);
    }
}
