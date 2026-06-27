package org.frasato.view.home.modal;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

public class IncludeModal extends JDialog {

    private JTextField isbnField;
    private JButton addButton;
    private JProgressBar progressBar;

    public IncludeModal() {init();}

    public void init() {
        setTitle("Adicionar Novo Livro");
        setModal(true);

        setLayout(new MigLayout(
                "align center center",
                "[center]",
                "[]"
        ));

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setIndeterminate(true);

        setSize(400, 300);
        setLocationRelativeTo(null);

        isbnField = new JTextField(20);
        isbnField.putClientProperty(FlatClientProperties.STYLE, "arc:6;");

        addButton = new JButton("Adicionar");
        addButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "hoverBackground: #bfdfff;"
        );

        JLabel label = new JLabel("Informe o ISBN do livro");
        label.putClientProperty(FlatClientProperties.STYLE, "font: 16;" +
                "foreground: #1F3A77;"
        );

        add(label, "gapbottom 5, wrap");
        add(isbnField, "w 220!, gapbottom 8, wrap");
        add(addButton, "w 120!, h 30!");
    }

    public String getIsbnField() {
        return isbnField.getText();
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JProgressBar getProgressBar() { return progressBar; }
}