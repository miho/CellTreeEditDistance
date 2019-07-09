package edu.gcsc.celltreeedit.PartitioningError;

import edu.gcsc.celltreeedit.UniqueMetadata;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView;
import java.awt.*;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class RelPartitioningErrorTable extends JPanel implements Serializable {

    JTable table;

    public RelPartitioningErrorTable(float[][] relPartitioningErrors, List<UniqueMetadata> uniqueMetadata) {

        // fill table with values and leave upper right half blank
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        int size = relPartitioningErrors.length;
        table = new JTable(size, size + 1);

        TableColumn tc;
        for (int i = 1; i < size + 1; i++) {
            tc = table.getColumnModel().getColumn(i);
            tc.setHeaderValue(uniqueMetadata.get(i - 1).getUniqueMetadataId());
            table.setValueAt(uniqueMetadata.get(i - 1).getUniqueMetadataId(), i - 1, 0);
        }

        table.setDefaultRenderer(Object.class, new StatusTableCellRenderer());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= i; j++) {
                table.setValueAt(df.format(relPartitioningErrors[i][j]), i, j + 1);
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