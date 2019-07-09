package edu.gcsc.celltreeedit.PartitioningError;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class RelPartitioningErrorTable extends JPanel implements Serializable {

    JTable table;

    public RelPartitioningErrorTable(float[][] relPartitioningErrors) {

        // fill table with values and leave upper right half blank
        int size = relPartitioningErrors.length;
        table = new JTable(size, size);
        for (int i = 0; i < size; i++) {
            // index of col-set
            for (int j = 0; j <= i; j++) {
                int row = i + 1;
                table.setValueAt(relPartitioningErrors[i][j], i, j);
            }
        }
        table.setPreferredScrollableViewportSize(new Dimension(850, 200));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane pane = new JScrollPane(table);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane);

    }
}