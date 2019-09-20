package edu.gcsc.celltreeedit;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.Serializable;

public class Tables extends JPanel implements Serializable{

    JTable table;
    public Tables(String[] names, float[][] result){

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
        table.setPreferredScrollableViewportSize(new Dimension(850,200));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane pane= new JScrollPane(table);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane);

    }

    public Tables(){
        table =new JTable(16,2);
        table.setValueAt("ID",0,0);
        table.setValueAt("Label definition",0,1);
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
        table.setValueAt("11",11,0);
        table.setValueAt("12",12,0);
        table.setValueAt("13",13,0);
        table.setValueAt("14",14,0);
        table.setValueAt("15",15,0);
        table.setValueAt("1",1,1);
        table.setValueAt("1 / |T|",2,1);
        table.setValueAt("1 / |T[i]|",3,1);
        table.setValueAt("length of t[i]",4,1);
        table.setValueAt("approxLength of t[i]",5,1);
        table.setValueAt("length of T",6,1);
        table.setValueAt("length of t[i] / length of T",7,1);
        table.setValueAt("surface of t[i]",8,1);
        table.setValueAt("approxSurface of t[i]",9,1);
        table.setValueAt("surface of T",10,1);
        table.setValueAt("surface of t[i] / surface of T",11,1);
        table.setValueAt("volume of t[i]",12,1);
        table.setValueAt("approxVolume of t[i]",13,1);
        table.setValueAt("volume of T",14,1);
        table.setValueAt("volume of t[i] / Volume of T",15,1);

        JScrollPane pane= new JScrollPane(table);
        add(pane);
    }

    public Tables(String[] firstColumn, String[] secondColumn, String[] thirdColumn, String[] columnNames){
        int rowSize = firstColumn.length;
        int colSize = columnNames.length;

        table = new JTable(rowSize, colSize){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumn tc;
        for (int i = 0; i < colSize; i++) {
            tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(columnNames[i]);
        }
        for (int i = 0; i < rowSize; i++) {
            table.setValueAt(firstColumn[i], i, 0);
            table.setValueAt(secondColumn[i], i, 1);
            table.setValueAt(thirdColumn[i], i, 2);
        }

        table.setFillsViewportHeight(true);
        JScrollPane pane= new JScrollPane(table);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane);
    }

    public void printTable(File outputDirectory, String filename) {
        Utils.printTableToTxt(this.table, outputDirectory, filename);
    }
}