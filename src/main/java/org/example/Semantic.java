package org.example;


import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Semantic {

    private HashMap<String, String> initializedVars = new HashMap<>();

    private List<AbstractMap.SimpleEntry<String, String>> tokens; //value = type
    private int currentIndex;
    private int scope = 1;
    private HashMap<Integer, List<String>> identifiersList = new HashMap<Integer, List<String>>();
    private TreeNode parseTree;

    public Semantic(List<AbstractMap.SimpleEntry<String, String>> tokens, TreeNode parseTree) {
        this.tokens = tokens;
        this.currentIndex = 0;
        this.identifiersList.put(scope, Arrays.asList());
        this.parseTree = parseTree;

    }

    private AbstractMap.SimpleEntry<String, String> currentToken() {
        if (currentIndex >= tokens.size()) {
            return null;
        }
        return tokens.get(currentIndex);
    }

    //    private AbstractMap.SimpleEntry<String, String> currentNode() {
//        if (currentIndex >= parseTree.size()) {
//            return null;
//        }
//        return tokens.get(currentIndex);
//    }
    private boolean hasTokens() {
        return currentIndex < tokens.size();
    }

    private AbstractMap.SimpleEntry<String, String> nextToken() {
        currentIndex++;
        return currentToken();
    }

    //    private AbstractMap.SimpleEntry<String, String> nextNode() {
//        currentIndex++;
//        return currentNode();
//    }
    private boolean isType(String type) {
        return currentToken() != null && currentToken().getValue().equals(type);
    }

    private boolean accept(String type) {
        if (isType(type)) {
            nextToken();
            return true;
        }
        return false;
    }

    private AbstractMap.SimpleEntry<String, String> peekNextToken() {
        if (currentIndex + 1 >= tokens.size()) {
            return null;
        }
        return tokens.get(currentIndex + 1);
    }


    private void expect(String type) {
        if (!accept(type)) {
            if (type.equals("EQUALITY"))
                throw new IllegalStateException("Expected token of type: EQUALITY but got: "
                        + (currentToken() != null ? currentToken().getKey() : "null"));
            else
                throw new IllegalStateException("Expected token of type: " + type + " but got: "
                        + (currentToken() != null ? currentToken().getKey() : "null"));
        }
    }

    //    public void traverse(TreeNode node){
////        if (currentToken().getValue() == ("cofs")){
////            if ( currentToken().getValue()  == nextToken().getValue()){
////                System.out.println("hatdofASLDKJA;SLDKJA;SLDK");
////            System.out.println(currentToken().getValue());
////            }
//////        }
////        System.out.println(nextToken().getValue());
//
//    }
    public void traverse(TreeNode node) {
        if (node == null) return;
//    System.out.println("label: " + node.getLabel() + " || type: " + node.getValue() );
//    if ( node.getValue()  == nextNode().getValue()){
//                System.out.println("hatdofASLDKJA;SLDKJA;SLDK");
//
//            }
//
//    for (TreeNode child : node.getChildren()) {
//        traverse(child);
//    }
        System.out.println("label: " + node.getLabel() + " || name: " + node.getName() );
        if (node.getLabel().equals("cofs")) {
            String varName = node.getValue();
            if (initializedVars.containsKey(varName)) {
                System.err.println("Error: variable '" + varName + "' has already been initialized.");
                System.exit(1);
            }
            initializedVars.put(varName, "cofs");
            System.out.println("Added variable " + varName + " of type 'cofs' to symbol table.");
        }

        // Continue traversal
        for (TreeNode child : node.getChildren()) {
            traverse(child);
        }
    }


    public void traverse() {
        traverse(parseTree);
    }
}


