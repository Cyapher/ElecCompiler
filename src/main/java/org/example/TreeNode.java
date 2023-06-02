package org.example;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private static int counter = 0;
    private String label;
    private String value;
    private String name;

    private TreeNode node;
    private int index;
    private int left;
    private List<TreeNode> children;

//    private int index;
    public TreeNode(String label) {
        this(label, "");
//        this.label = label;
//        this.value = "";
//        this.children = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public TreeNode(String label, String value) {

        this.index = counter++;
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
    public int getIndex() {
        return index;
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
    public TreeNode getChildByName(String name) {
        for (TreeNode child : children) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    public  TreeNode getNode(int index) {
       return this.node;
    }

    public boolean isLeaf(){
        if(children.size() == 0){
            return true;
        }
        else {
            return false;
        }
    }

//    public TreeNode getChildByIndex(int index) {
//        for (int x = 0; x < children.size() - 1; x++) {
//           TreeNode child = children.get(x);
//
//            return child;
//        }
//        return null;
//    }
    
    public String toString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();

        sb.append(prefix);
        sb.append(isTail ? "└── " : "├── ");
        sb.append(label);
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

//    public void setIndex(int index) {
//        this.index =
//    }
}