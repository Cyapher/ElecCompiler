package org.example;

import java.awt.print.PrinterException;
import java.util.*;
import java.util.AbstractMap.*;

import javax.swing.JTable;

public class Semantic {
    private List<SimpleEntry<String, String>> tokens; // value = type
    private TreeNode parseTree;
    private JTable symbolTable;

    public Semantic(List<SimpleEntry<String, String>> tokens, TreeNode parseTree) {
        this.tokens = tokens;
        this.parseTree = parseTree;
    }

    public void Semantic() {
        System.out.println("============ IN SEMANTIC ============");

        String[] columnNames = { "Index", "Token", "Type", "Value", "Reference" };
        String[][] array = new String[tokens.size()][5];
        
        // System.out.println("arr val" + array[0][1]);

        for (int i = 0; i < tokens.size(); i++) {
            SimpleEntry<String, String> entry = tokens.get(i);
            for (int j = 0; j < 5; j++) {
                if (j == 0) 
                    array[i][j] = Integer.toString(i); //Token Index
                else if (j == 1) 
                    array[i][j] = entry.getKey(); //Token Lexeme
                else if(j == 2)
                    array[i][j] = entry.getValue(); //Token Type
                else if(j == 3){  
                    int pointer = i;                  //Token Value 
                    if(entry.getValue() != "IDENTIFIER"){
                        array[i][j] = entry.getKey();
                    } else if(entry.getValue() == "IDENTIFIER" && array[pointer++][j] == "ASSIGNMENT"){
                        // array[i][j] = array[][]
                        System.out.println("JASKDHKSDHAKDSAKDHJAHDAKJHDASKJASDJKHASD HATTTTDOOOGGGG TANIGNA");
                    }
                } else{ //if identifier
                    
                }
                    
            }
        }

        System.out.println("Token List: \n");
        for (String[] row : array) {
            for (String element : row) {
                System.out.println(element + "\t");
            }
            System.out.println("\n");
        }

        //symbolTable = new JTable(array, columnNames);
        SampleTable st = new SampleTable(array, columnNames);
        st.createAndShowGUI();

    }
    
   


}
