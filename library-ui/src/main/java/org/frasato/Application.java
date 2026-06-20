package org.frasato;

import javax.swing.*;

public class Application extends JFrame {

    public Application(){
        init();
    }

    private void init(){
        setTitle("Livraria");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
