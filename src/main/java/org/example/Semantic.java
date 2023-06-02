package org.example;


import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Semantic {

    private HashMap<String, String> initializedVars = new HashMap<>();
    private HashMap<Integer, String> leafNodes = new HashMap<>();

    private List<AbstractMap.SimpleEntry<String, String>> tokens; //value = type
    private int currentIndex;
    private int scope = 1;
    private HashMap<Integer, List<String>> identifiersList = new HashMap<Integer, List<String>>();
    private TreeNode parseTree;
    private int left;

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

    private boolean hasTokens() {
        return currentIndex < tokens.size();
    }

    private AbstractMap.SimpleEntry<String, String> nextToken() {
        currentIndex++;
        return currentToken();
    }



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
        System.out.println("index: " + node.getIndex() +"|| label: " + node.getLabel() + " || name: " + node.getName() );
//        if (node.getValue().equals("cofs")){
//            System.out.println(node.getIndex());
//        }
//        if (node.getLabel().equals("cofs")) {
//            String varName = node.getValue();
//            if (initializedVars.containsKey(varName)) {
//                System.err.println("Error: variable '" + varName + "' has already been initialized.");
//                System.exit(1);
//            }
//            initializedVars.put(varName, "cofs");
//            System.out.println("Added variable " + varName + " of type 'cofs' to symbol table.");
//        }

        //operations

//        System.out.println(node.getChildByIndex(node.getIndex()));


        if (node.isLeaf()) {
            leafNodes.put(node.getIndex(), node.getValue());

//                System.out.println("hatdkas");
        }

        System.out.println(leafNodes);


//        if (node.getLabel() == "ADDITION" ) {
////            int left = node.getIndex() - 2;
////            int right = node.getIndex() + 1;
////            if(node.getIndex() == left){
////                System.out.println(node.getValue());
////            }
////            TreeNode var1 = node.getNode(left);
////            System.out.println("value of var1: " + var1.getValue());
//
//        }

            if (node.getLabel().equals("Cofs Value")) {
//            System.out.println(node.getIndex());
//                System.out.println("hatdkas");
//                if (node.isLeaf()) {
//                    leafNodes.put(node.getIndex(), node.getValue());
//
//
//                }
//
//                System.out.println(leafNodes);



//            System.out.println(left);
//            System.out.println(node.getChildByIndex(node.getIndex()));
//                if(node.getLeft() == ){
//                    System.out.println("hatffof");
//                }

        }

        if (node.getLabel() == "cofs" && node.getValue() == "cofs"){

        }




            // Continue traversal
        for (TreeNode child : node.getChildren()) {
            traverse(child);
        }

    }


    public void traverse() {
        traverse(parseTree);
    }

    public void interpret(TreeNode node){
        if (node == null) return;
            int var1;
            int var2;
        if (node.getLabel() == "ADDITION" ) {

            int left = node.getIndex() - 1;
            int right = node.getIndex() + 2;

            if(leafNodes.containsKey(left) && leafNodes.containsKey(right)){
//                System.out.println("value is: " + leafNodes.get(left));
//                System.out.println("value is: " + leafNodes.get(right));
                var1 = Integer.parseInt(leafNodes.get(left));
                var2 = Integer.parseInt(leafNodes.get(right));

                int sum = var1 + var2;

                System.out.println(sum);
            }

        }

        if (node.getLabel() == "SUBTRACTION" ) {

            int left = node.getIndex() - 1;
            int right = node.getIndex() + 2;

            if(leafNodes.containsKey(left) && leafNodes.containsKey(right)){

                var1 = Integer.parseInt(leafNodes.get(left));
                var2 = Integer.parseInt(leafNodes.get(right));

                int difference = var1 - var2;

                System.out.println(difference);
            }

        }
        if (node.getLabel() == "DIVISION" ) {

            int left = node.getIndex() - 1;
            int right = node.getIndex() + 1;

            if(leafNodes.containsKey(left) && leafNodes.containsKey(right)){

                var1 = Integer.parseInt(leafNodes.get(left));
                var2 = Integer.parseInt(leafNodes.get(right));

                int quotient = var1 / var2;

                System.out.println(quotient);
            }

        }
        if (node.getLabel() == "MULTIPLICATION" ) {

            int left = node.getIndex() - 1;
            int right = node.getIndex() + 1;

            if(leafNodes.containsKey(left) && leafNodes.containsKey(right)){

                var1 = Integer.parseInt(leafNodes.get(left));
                var2 = Integer.parseInt(leafNodes.get(right));

                int product = var1 * var2;

                System.out.println(product);
            }

        }
        if (node.getLabel() == "MODULO" ) {

            int left = node.getIndex() - 1;
            int right = node.getIndex() + 2;

            if(leafNodes.containsKey(left) && leafNodes.containsKey(right)){

                var1 = Integer.parseInt(leafNodes.get(left));
                var2 = Integer.parseInt(leafNodes.get(right));

                int remainder = var1 % var2;

                System.out.println(remainder);
            }

        }





        for (TreeNode child : node.getChildren()) {
            interpret(child);
        }

    }
    public void interpret() {
        interpret(parseTree);
    }

}


