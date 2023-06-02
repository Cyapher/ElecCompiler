package org.example;

import jdk.nashorn.internal.ir.Symbol;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String label;
    private String value;
    private String name;
    private List<TreeNode> children;
    private Symbol symbol; // Symbol associated with the node (e.g., variable, function)

    public TreeNode(String label) {
        this.label = label;
        this.value = "";
        this.children = new ArrayList<>();
    }

    public TreeNode(String label, String value) {
        this.label = label;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public TreeNode getChildByName(String name) {
        for (TreeNode child : children) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    public String toString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();

        sb.append(prefix);
        sb.append(isTail ? "└── " : "├── ");
        sb.append(label);
        if (symbol != null) {
            sb.append(" [");
            sb.append(symbol.toString());
            sb.append("]");
        }
        sb.append("\n");

        for (int i = 0; i < children.size() - 1; i++) {
            sb.append(children.get(i).toString(prefix + (isTail ? "    " : "│   "), false));
        }
        if (children.size() > 0) {
            sb.append(children.get(children.size() - 1).toString(prefix + (isTail ? "    " : "│   "), true));
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return toString("", true);
    }
}
