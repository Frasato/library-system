package org.frasato.view.home.modal;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.frasato.view.home.components.EditModalTextField;
import javax.swing.*;

public class EditModal extends JDialog {

    private JButton finishButton;
    private JButton deleteButton;
    private JTextField title;
    private JTextField publishDate;
    private JTextField isbn;
    private JTextField publisher;
    private JTextField author;

    public EditModal() {
        init();
    }

    private void init() {

        setTitle("Editar Livro");
        setSize(700, 550);
        setLocationRelativeTo(null);

        setLayout(new MigLayout(
                "fill, insets 40, align center center",
                "[grow, center]",
                "[]20[]10[]10[]10[]10[]20[]"
        ));

        JLabel label = new JLabel("Informações sobre o livro");
        label.putClientProperty(
                FlatClientProperties.STYLE, "font: +8"
        );

        title = new EditModalTextField("Título");
        publishDate = new EditModalTextField("Data de publicação");
        isbn = new EditModalTextField("ISBN");
        publisher = new EditModalTextField("Editora");
        author = new EditModalTextField("Autor");

        finishButton = new JButton("Concluir");
        finishButton.putClientProperty(
                FlatClientProperties.STYLE, "arc:12;" +
                "hoverBackground:#bfdfff;"
        );

        deleteButton = new JButton("Deletar");
        deleteButton.putClientProperty(
                FlatClientProperties.STYLE, "arc: 12;" +
                "foreground: #ff4a3d;" +
                "hoverBackground: #ffd9d6;"
        );

        add(label, "wrap");

        add(title, "w 350!, h 42!, wrap");
        add(publishDate, "w 350!, h 42!, wrap");
        add(isbn, "w 350!, h 42!, wrap");
        add(publisher, "w 350!, h 42!, wrap");
        add(author, "w 350!, h 42!, wrap");

        JPanel buttons = new JPanel(new MigLayout("insets 0, gap 12", "[][]"));

        buttons.add(finishButton, "w 140!, h 42!");
        buttons.add(deleteButton, "w 140!, h 42!");
        add(buttons);
    }

    public JButton getFinishButton() {
        return finishButton;
    }
    public JButton getDeleteButton() {
        return deleteButton;
    }
    public String getTitle() { return title.getText(); }
    public String getPublishDate() {return publishDate.getText();}
    public String getIsbn() {return isbn.getText();}
    public String getPublisher() {return publisher.getText();}
    public String getAuthor() {return author.getText();}

    public void setTitleValue(String value){
        title.setText(value);
    }

    public void setPublishDateValue(String value){
        publishDate.setText(value);
    }

    public void setIsbnValue(String value){
        isbn.setText(value);
    }

    public void setPublisherValue(String value){
        publisher.setText(value);
    }

    public void setAuthorValue(String value){
        author.setText(value);
    }
}