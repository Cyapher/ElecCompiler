package org.example;

import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer {
    public void analyze(TreeNode parseTree) {
        // Perform semantic analysis
        System.out.println("Performing semantic analysis...");

        // Example: Print the label of each node in the parse tree
        traverseParseTree(parseTree);
    }

    private void traverseParseTree(TreeNode node) {
        // Print the label of the current node
        System.out.println("Analyzing node: " + node.getLabel());

        // Traverse the children of the current node
        for (TreeNode child : node.getChildren()) {
            traverseParseTree(child);
        }
    }
}
