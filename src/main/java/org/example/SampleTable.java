package org.example;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import java.awt.Dimension;
import java.awt.GridLayout;

public class SampleTable extends JPanel{
    private static String[] columnNames;
    private static String[][] array;

    public SampleTable(String[][] array, String[] columnNames){
        super(new GridLayout(1,0));

        this.columnNames = columnNames;
        this.array = array;

        JTable table = new JTable(array, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
 
        //Add the scroll pane to this panel.
        add(scrollPane);
    }

    // GETTERS ----------------------------------------------------
    public int getColumnCount(){
        return columnNames.length;
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return array[row][col];
    }

    // SHOW GUI ----------------------------------------------------
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        TableColumn col = null;

        //Create and set up the content pane.
        SampleTable newContentPane = new SampleTable(array, columnNames);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

}
