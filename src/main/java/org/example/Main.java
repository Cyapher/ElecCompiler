package org.example;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.io.IOException;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        List<SimpleEntry<String, String>> tokens = lexer.analyzeTokens();

        Parser parser = new Parser(tokens);
        TreeNode parseTree = parser.parse();

        Semantic sem = new Semantic(tokens, parseTree);
        sem.Semantic();

        // Print the token list
        System.out.println("============ IN MAIN ============");
        System.out.println("Token list:");
        for (SimpleEntry<String, String> token : tokens) {
            System.out.println(token.getKey() + " : " + token.getValue());
        }

        // Print the parse tree
        System.out.println("\nParse tree:");
        printParseTree(parseTree, "   ");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Parse Tree");
            TreePanel treePanel = new TreePanel(parseTree);
            frame.setContentPane(treePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setVisible(true);
        });
        System.out.println("Parsing successful!");
    }
    private static void printParseTree(TreeNode node, String indent) {
        String label = node.getLabel();
        String name = node.getName();
        if (name != null && !name.isEmpty()) {
            label += " (" + name + ")";
        }
        System.out.println(indent + label);

        for (TreeNode child : node.getChildren()) {
            printParseTree(child, indent + "   |");
        }
    }
}