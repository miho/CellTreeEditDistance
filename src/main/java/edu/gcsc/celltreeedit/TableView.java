package edu.gcsc.celltreeedit;

import javax.swing.*;
import java.awt.*;

public class TableView extends JFrame{

    JTable table;
    public TableView(String[] names, float[][] result){
        setLayout(new FlowLayout());
        table =new JTable(names.length+1,names.length+1);

        table.setValueAt("Files",0,0);
        for(int i=0; i<names.length;i++){
            table.setValueAt(names[i],i+1,0);
            table.setValueAt(names[i],0,i+1);
        }

        for(int i=0; i<names.length;i++){
            for(int j=0; j<result.length;j++){
                table.setValueAt(result[i][j],i+1,j+1);
                System.out.println();
            }
        }


        JScrollPane pane= new JScrollPane(table);
        add(pane);
    }
}