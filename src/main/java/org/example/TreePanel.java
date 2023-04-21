package org.example;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

public class TreePanel extends JPanel {
    public TreePanel(TreeNode root) {
        setLayout(new BorderLayout());
        JTree tree = createTree(root);
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JTree createTree(TreeNode root) {
        DefaultMutableTreeNode swingRoot = createSwingNode(root);
        return new JTree(swingRoot);
    }

    private DefaultMutableTreeNode createSwingNode(TreeNode node) {
        DefaultMutableTreeNode swingNode = new DefaultMutableTreeNode(node.getValue());
        for (TreeNode child : node.getChildren()) {
            swingNode.add(createSwingNode(child));
        }
        return swingNode;
    }
}
