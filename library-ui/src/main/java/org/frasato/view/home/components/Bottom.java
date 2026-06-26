package org.frasato.view.home.components;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class Bottom extends JPanel {

    private JButton importButton;
    private JButton editButton;
    private JButton includeButton;
    private JButton similarButton;

    public Bottom(){ init(); }

    public void init(){
        importButton = new JButton("Importar");
        editButton = new JButton("Editar");
        includeButton = new JButton("Adicionar");
        similarButton = new JButton("Adicionar Similar");

        setLayout(new MigLayout("", "[][][]", "[]"));

        importButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "hoverBackground: #bfdfff;"
        );

        editButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "hoverBackground: #bfdfff;"
        );

        includeButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "hoverBackground: #bfdfff;"
        );

        similarButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "hoverBackground: #bfdfff;"
        );

        add(importButton, "h 30!");
        add(editButton, "h 30!");
        add(includeButton, "h 30!");
        add(similarButton, "h 30!");
    }

    public JButton getImportButton() {
        return importButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getIncludeButton() {
        return includeButton;
    }

    public JButton getSimilarButton() { return similarButton; }
}
