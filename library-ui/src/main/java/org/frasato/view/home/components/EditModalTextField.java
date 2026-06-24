package org.frasato.view.home.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;

public class EditModalTextField extends JTextField {

    public EditModalTextField(String placeholder){init(placeholder);}

    private void init(String placeholder){
        putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT,
                placeholder
        );

        putClientProperty(FlatClientProperties.STYLE, "arc:12;");
    }
}
