package org.frasato.view.home.modal;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

public class EditModal extends JDialog {

    private JButton finishButton;
    private JButton deleteButton;
    private JTextField title;
    private JTextField publishDate;
    private JTextField isbn;
    private JTextField publisher;
    private JTextField author;

    public EditModal() { init(); }

    private void init(){
        setLayout(new MigLayout("fill", "[]", "[]"));
        setTitle("Editar Livro");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Informações sobre o livro");

        title = new JTextField();

        publishDate = new JTextField();

        isbn = new JTextField();

        publisher = new JTextField();

        author = new JTextField();

        finishButton = new JButton("Concluir");
        finishButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "minimumHeight: 30;" +
                "hoverBackground: #bfdfff;"
        );

        deleteButton = new JButton("Deletar");
        deleteButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "foreground: #ff4a3d;" +
                "minimumHeight: 30;" +
                "hoverBackground: #ffd9d6;"
        );

        add(label, "wrap");
        add(title, "wrap");
        add(publishDate, "wrap");
        add(isbn, "wrap");
        add(publisher, "wrap");
        add(author, "wrap");
        add(finishButton);
        add(deleteButton);

    }

    public JButton getFinishButton() {
        return finishButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }
}
