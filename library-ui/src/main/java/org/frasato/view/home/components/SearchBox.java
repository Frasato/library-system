package org.frasato.view.home.components;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class SearchBox extends JPanel {
    private JTextField searchField;

    public SearchBox(String title){
        init(title);
    }

    public void init(String title){
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[][]"));

        putClientProperty(FlatClientProperties.STYLE, "arc:6;");
        setBackground(new Color(230, 232, 232));
        setPreferredSize(new Dimension(220, 100));

        JLabel boxTitle = new JLabel(title);
        searchField = new JTextField();

        boxTitle.putClientProperty(FlatClientProperties.STYLE, "font: bold 14;");
        add(boxTitle, "wrap");

        searchField.putClientProperty(FlatClientProperties.STYLE, "arc:6;");
        add(searchField, "growx");
    }

    public String getText(){
        return searchField.getText();
    }
}
