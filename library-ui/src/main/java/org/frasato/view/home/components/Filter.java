package org.frasato.view.home.components;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class Filter extends JPanel {

    private JTextField filterField;
    private JButton searchButton;

    public Filter(){ init(); }

    public void init(){

        setLayout(new MigLayout("fill", "[grow]", "[]"));
        setBackground(new Color(31, 58, 119));
        putClientProperty(FlatClientProperties.STYLE, "arc: 6;");

        filterField = new JTextField();
        filterField.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "borderWidth: 0;" +
                "focusedBorderColor: #ACC813;"
        );

        searchButton = new JButton("Pesquisar");
        searchButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6;" +
                "borderWidth: 0;"
        );

        add(filterField, "split 2, right, w 210!");
        add(searchButton, "w 90!");
    }

    public String getText(){
        return this.filterField.getText();
    }

    public JButton getButton(){
        return this.searchButton;
    }
}
