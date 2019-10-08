/*
 * Copyright 2018-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * Copyright 2018-2019 Goethe Center for Scientific Computing, University Frankfurt. All rights reserved.
 * Copyright 2018 Erid Guga. All rights reserved.
 * Copyright 2019 Lukas Maurer. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
package edu.gcsc.celltreeedit.ClusterAnalysis;

import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
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

    public RelPartitioningErrorTable(float[][] relPartitioningErrors, List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadata) {

        // decimal format and rounding
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        // first column is used for uniqueMetadataObject-Id -> size+1 columns
        int size = relPartitioningErrors.length;

        table = new JTable(size, size + 1){
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
//        table.setPreferredScrollableViewportSize(new Dimension(850, 200));
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//        table.setFillsViewportHeight(true);
//        JScrollPane pane = new JScrollPane(table);
//        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        add(pane);

        table.setFillsViewportHeight(true);
        JScrollPane pane= new JScrollPane(table);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane);

    }

}