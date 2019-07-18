package edu.gcsc.celltreeedit.ClusterAnalysis;

import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Table for usage in JFrame. Displays relative Partitioning Errors of a Clustering
 */
public class RelPartitioningErrorTable extends JPanel implements Serializable {

    JTable table;

    public RelPartitioningErrorTable(double[][] relPartitioningErrors, List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadata) {

        // decimal format and rounding
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        // first column is used for uniqueMetadataObject-Id -> size+1 columns
        int size = relPartitioningErrors.length;
        table = new JTable(size, size + 1);

        // set header and first column
        TableColumn tc;
        for (int i = 0; i < size; i++) {
            // set column header
            tc = table.getColumnModel().getColumn(i + 1);
            tc.setHeaderValue(uniqueMetadata.get(i).getUniqueMetadataId());
            // set first column with uniqueMetadataObject-Id (starting with index 1 because lower left-matrix
            table.setValueAt(uniqueMetadata.get(i + 1).getUniqueMetadataId(), i, 0);
        }

        // renderer formats background color of cells based on their value
        table.setDefaultRenderer(Object.class, new StatusTableCellRenderer());
        for (int i = 0; i < size; i++) {
            // index of table-column
            for (int j = 1; j <= i + 1; j++) {
                table.setValueAt(df.format(relPartitioningErrors[i][j - 1]), i, j);
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