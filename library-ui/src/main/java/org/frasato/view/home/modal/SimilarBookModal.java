package org.frasato.view.home.modal;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SimilarBookModal extends JDialog {

    private final JPanel listPanel;
    private final JButton addButton;
    private final JButton cancelButton;

    public SimilarBookModal() {

        setTitle("Adicionar livros similares");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setModal(true);

        setLayout(new MigLayout("fill, insets 15", "[grow]", "[grow][]"));
        listPanel = new JPanel(new MigLayout("wrap 1, fillx", "[grow]"));

        JScrollPane scroll = new JScrollPane(listPanel);

        addButton = new JButton("Adicionar");
        cancelButton = new JButton("Cancelar");

        JPanel footer = new JPanel(new MigLayout("insets 15", "[right][right]"));

        footer.add(cancelButton);
        footer.add(addButton);

        add(scroll, "grow");
        add(footer, "dock south");
    }

    public JPanel getListPanel() {return listPanel;}

    public JButton getAddButton() {return addButton;}

    public JButton getCancelButton() {return cancelButton;}
}