package org.frasato.view.home;

import net.miginfocom.swing.MigLayout;
import org.frasato.view.home.components.Header;

import javax.swing.*;

public class HomeView extends JPanel {

    public HomeView(){ init(); }

    public void init(){
        setLayout(new MigLayout("fill, insets 15", "[grow]", "[][pref!][grow][]"));
        add(new Header());
    }
}
