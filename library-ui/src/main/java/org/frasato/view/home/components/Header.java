package org.frasato.view.home.components;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;

public class Header extends JPanel {

    public Header(){ init(); }

    public void init(){
        setLayout(new MigLayout("insets 10", "[][][][]", "[]"));

        SearchBox titleBox = new SearchBox("Titulo");
        SearchBox authorBox = new SearchBox("Autor");
        SearchBox isbnBox = new SearchBox("ISBN");
        SearchBox publishDateBox = new SearchBox("Publicação");

        add(titleBox);
        add(authorBox);
        add(isbnBox);
        add(publishDateBox);
    }
}
