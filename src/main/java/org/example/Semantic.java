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

    public void TraverseParseTree(TreeNode parsetree){
        System.out.println("Parse Tree: \n" + parsetree.getChildren().get(0));
        TreeNode endOfLineNode = findNodeByName(parsetree, "END OF LINE");
        System.out.println("Child: \n" + endOfLineNode);
    }

    public void Semantic() {
        System.out.println("============ IN SEMANTIC ============");

        TraverseParseTree(parseTree);
        String[] columnNames = { "Index", "Token", "Type", "Value", "Reference" };
        String[][] array = new String[tokens.size()][5];
        
        // System.out.println("arr val" + array[0][1]);
        System.out.println("*********** PROGRAM OUTPUT ***********");

        for (int i = 0; i < tokens.size(); i++) { // i = tokens
            SimpleEntry<String, String> entry = tokens.get(i);
            for (int j = 0; j < 5; j++) { //j = attribute
                if (j == 0) //Index
                    array[i][j] = Integer.toString(i); //Token Index
                else if (j == 1) 
                    array[i][j] = entry.getKey(); //Token Lexeme
                else if(j == 2)
                    array[i][j] = entry.getValue(); //Token Type
                else if(j == 3){                    //Token Value 
                    int pointer = i;//pointer
                    if(entry.getValue() != "IDENTIFIER"){
                        array[i][j] = entry.getKey();
                    } else if(entry.getValue() == "makeSulat"){//for makeSulat (print) Operation
                        pointer = i;
                        


                    }else if(entry.getValue() == "IDENTIFIER" && array[pointer++][j] == "ASSIGNMENT"){ 
                        array[i][j] = array[pointer++][j];
                    }
                } else{ 
                    
                }
                    
            }
        }

        // System.out.println("Token List: \n");
        // for (String[] row : array) {
        //     for (String element : row) {
        //         System.out.println(element + "\t");
        //     }
        //     System.out.println("\n");
        // }

        symbolTable = new JTable(array, columnNames);
        SampleTable st = new SampleTable(array, columnNames);
        st.createAndShowGUI();

    }
    
    public TreeNode findNodeByName(TreeNode node, String name) {
        if (node == null) {
            System.out.println("Node is null");
            return null;
        }
    
        String nodeName = node.getName();
        if (nodeName == null) {
            System.out.println("Node name is null");
        } else {
            System.out.println("Visiting node: " + nodeName);
        }
    
        if (nodeName != null && nodeName.equals(name)) {
            return node;
        }
    
        List<TreeNode> children = node.getChildren();
        if (children == null) {
            System.out.println("Node's children are null");
            return null;
        }
    
        for (int i = 0; i < children.size(); i++) {
            TreeNode child = children.get(i);
            System.out.println("Visiting child #" + i + ": " + child);
            TreeNode result = findNodeByName(child, name);
            if (result != null) {
                return result;
            }
        }
    
        return null;
    }
    
    
   


}
