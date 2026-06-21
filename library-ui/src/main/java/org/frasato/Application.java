package org.frasato;

import org.frasato.controller.home.HomeController;
import javax.swing.*;

public class Application extends JFrame {

    public Application(){
        init();
    }

    private void init(){
        setTitle("Livraria");
        setSize(1200, 700);
        setLocationRelativeTo(null);

        HomeController homeController = new HomeController();
        add(homeController.view());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
