package org.frasato.controller.home;

import org.frasato.view.home.HomeView;

import javax.swing.*;

public class HomeController {
    private final HomeView homeView;

    public HomeController(){
        homeView = new HomeView();
    }

    public JPanel view(){return homeView;}
}
