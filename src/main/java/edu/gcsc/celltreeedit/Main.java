package edu.gcsc.celltreeedit;


import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {
    public static void main(String[] args) throws IOException {
//            Matrix.compare(ChooseFiles.choose());

        ChooseFiles filechooser = new ChooseFiles();
        File[] file=filechooser.choose();
        int size=file.length;
        String[] names=new String[size];
        for(int i=0;i<size;i++){
            names[i]=file[i].getName();
        }
        TableView tableView= new TableView(names, Matrix.compare(filechooser.choose()));
        tableView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tableView.setSize(800,300);
        tableView.setVisible(true);
        tableView.setTitle("matrix");

    }
}

