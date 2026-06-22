package org.frasato.view.home.components;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class Bottom extends JPanel {

    private JButton importButton;
    private JButton editButton;
    private JButton includeButton;

    public Bottom(){ init(); }

    public void init(){
        importButton = new JButton("Importar");
        editButton = new JButton("Editar");
        includeButton = new JButton("Adicionar");

        setLayout(new MigLayout("", "[][][]", "[]"));

        importButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "minimumHeight: 30;" +
                "hoverBackground: #bfdfff;"
        );

        editButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "minimumHeight: 30;" +
                "hoverBackground: #bfdfff;"
        );

        includeButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "minimumHeight: 30;" +
                "hoverBackground: #bfdfff;"
        );

        add(importButton);
        add(editButton);
        add(includeButton);
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
}
