package edu.gcsc.celltreeedit;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class TableView extends JPanel implements Serializable{

    JTable table;
    public TableView(String[] names, float[][] result){

        table =new JTable(names.length+1,names.length+1);
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
        table.setPreferredScrollableViewportSize(new Dimension(400,200));
        table.setFillsViewportHeight(true);
        JScrollPane pane= new JScrollPane(table);
        add(pane);
    }

    public TableView(){
        table =new JTable(20,2);
        table.setValueAt("ID",0,0);
        table.setValueAt("1",1,0);
        table.setValueAt("2",2,0);
        table.setValueAt("3",3,0);
        table.setValueAt("4",4,0);
        table.setValueAt("5",5,0);
        table.setValueAt("6",6,0);
        table.setValueAt("7",7,0);
        table.setValueAt("8",8,0);
        table.setValueAt("9",9,0);
        table.setValueAt("10",10,0);
        table.setValueAt("1",1,1);
        table.setValueAt("1/n",2,1);
        table.setValueAt("length of t[i]",3,1);
        table.setValueAt("surface of t[i]",4,1);
        table.setValueAt("volume of t[i]",5,1);
        table.setValueAt("length of T[i]",6,1);
        table.setValueAt("volume of T[i]",7,1);
        table.setValueAt("surface of T[i]",8,1);
        table.setValueAt("length of t[i] / length of T[i]",9,1);
        table.setValueAt("surface of t[i] / surface of T[i]",10,1);

        JScrollPane pane= new JScrollPane(table);
        add(pane);
    }
}