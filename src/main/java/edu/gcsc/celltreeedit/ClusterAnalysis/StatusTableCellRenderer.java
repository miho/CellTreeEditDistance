package edu.gcsc.celltreeedit.ClusterAnalysis;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


/**
 * Renderer for JTable. Used to color table cells based on their value.
 */
public class StatusTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        //Cells are by default rendered as a JLabel.
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        //Get the status for the current row.
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        if (tableModel.getValueAt(row, col) != null) {
            if (tableModel.getValueAt(row, col).getClass().equals(String.class)) {
                float cellValue = Float.parseFloat((String) tableModel.getValueAt(row, col));
                if (cellValue > 0.2f) {
                    l.setBackground(Color.RED);
                } else if (cellValue > 0.1f) {
                    l.setBackground(Color.ORANGE);
                } else if (cellValue > 0.05f) {
                    l.setBackground(Color.YELLOW);
                } else {
                    l.setBackground(Color.GREEN);
                }
            } else if (tableModel.getValueAt(row, col).getClass().equals(int.class)) {
                l.setBackground(Color.GRAY);
//                int cellValue = (Integer) tableModel.getValueAt(row, col);
            }

        } else {
            l.setBackground(Color.WHITE);
        }

        //Return the JLabel which renders the cell.
        return l;

    }
}