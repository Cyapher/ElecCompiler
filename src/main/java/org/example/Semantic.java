package org.example;

import java.awt.print.PrinterException;
import java.util.*;
import java.util.AbstractMap.*;

import javax.swing.JTable;

public class Semantic {
    private List<SimpleEntry<String, String>> tokens; // value = type
    private TreeNode parseTree;
    private JTable symbolTable;
    private Map<String, String> symbolValues; // Stores identifier values
    private Map<String, Integer> identifierIndices; // Stores identifier indices in array

    public Semantic(List<SimpleEntry<String, String>> tokens, TreeNode parseTree) {
        this.tokens = tokens;
        this.parseTree = parseTree;
        this.symbolValues = new HashMap<>();
        this.identifierIndices = new HashMap<>();
    }

    public void TraverseParseTree(TreeNode parsetree){
        System.out.println("Parse Tree: \n" + parsetree.getChildren().get(0));
        TreeNode endOfLineNode = findNodeByName(parsetree, "END OF LINE");
        System.out.println("Child: \n" + endOfLineNode);
    }

    public void Semantic() {
        System.out.println("============ IN SEMANTIC ============");

        String[] columnNames = { "Index", "Token", "TokenType", "Value", "Reference" };
        String[][] array = new String[tokens.size()][5];

        for (int i = 0; i < tokens.size(); i++) { // i = tokens
            SimpleEntry<String, String> entry = tokens.get(i);
            if (entry.getValue().equals("IDENTIFIER")) {
                String identifier = entry.getKey();
                if (identifierIndices.containsKey(identifier)) {
                    if (tokens.get(i + 1).getValue().equals("ASSIGNMENT")) {
                        String value;
                        if (tokens.get(i + 2).getValue().equals("STRING_VALUE")) {
                            value = "\"" + tokens.get(i + 2).getKey() + "\"";  // Preserve quotes for string values
                        } else {
                            value = tokens.get(i + 2).getKey();
                        }
                        array[identifierIndices.get(identifier)][3] = value; // Update value in array
                        symbolValues.put(identifier, value); // Update value in symbol table
                        array[i] = new String[] { Integer.toString(i), identifier + " - Reassignment", "IDENTIFIER", value, null };
                        i += 2; // Skip assignment and value tokens
                    }
                } else {
                    identifierIndices.put(identifier, i);
                    array[i][0] = Integer.toString(i);
                    array[i][1] = identifier;
                    array[i][2] = entry.getValue();
                    if (tokens.get(i + 1).getValue().equals("ASSIGNMENT")) {
                        String value;
                        if (tokens.get(i + 2).getValue().equals("STRING_VALUE")) {
                            value = "\"" + tokens.get(i + 2).getKey() + "\"";  // Preserve quotes for string values
                        } else {
                            value = tokens.get(i + 2).getKey();
                        }
                        array[i][3] = value;
                        symbolValues.put(identifier, value);
                    } else {
                        array[i][3] = "<unassigned>";
                    }
                }
            } else {
                array[i][0] = Integer.toString(i);
                array[i][1] = entry.getKey();
                array[i][2] = entry.getValue();
                array[i][3] = entry.getKey();
            }
            // handle reference column logic here if needed
        }


        System.out.println("Token List: \n");
        for (String[] row : array) {
            for (String element : row) {
                System.out.print(element + "\t");
            }
            System.out.println();
        }

        System.out.println("\nToken List as Table: \n");
        printTable(array, columnNames);
    }

    private void printTable(String[][] data, String[] columnNames) {
        int numRows = data.length;
        int numColumns = columnNames.length;

        // Print column names
        for (String columnName : columnNames) {
            System.out.print(columnName + "\t");
        }
        System.out.println();

        // Print table data
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                System.out.print(data[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
