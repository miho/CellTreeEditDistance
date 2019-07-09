//package edu.gcsc.celltreeedit.PartitioningError;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//import java.awt.*;
//
//public class StatusTableCellRenderer extends DefaultTableCellRenderer {
//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
//
//        //Cells are by default rendered as a JLabel.
//        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
//
//        //Get the status for the current row.
//        RelPartitioningErrorTable tableModel = (RelPartitioningErrorTable) table.getModel();
//        if (tableModel.getStatus(row) == CustomTableModel.APPROVED) {
//            l.setBackground(Color.GREEN);
//        } else {
//            l.setBackground(Color.RED);
//        }
//
//        //Return the JLabel which renders the cell.
//        return l;
//
//    }
//}