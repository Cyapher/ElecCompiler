package org.example;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Semantic {

    private HashMap<String, String> initializedVars = new HashMap<>();

    private List<AbstractMap.SimpleEntry<String, String>> tokens; // value = type
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

    public void TraverseParseTree(TreeNode parsetree) {
        System.out.println("Parse Tree: \n" + parsetree.getChildren().get(0));
        // TreeNode endOfLineNode = findNodeByName(parsetree, "END OF LINE");
        // System.out.println("Child: \n" + endOfLineNode);
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
                            value = "\"" + tokens.get(i + 2).getKey() + "\""; // Preserve quotes for string values
                        } else {
                            value = tokens.get(i + 2).getKey();
                        }
                        array[identifierIndices.get(identifier)][3] = value; // Update value in array
                        symbolValues.put(identifier, value); // Update value in symbol table
                        array[i] = new String[] { Integer.toString(i), identifier + " - Reassignment", "IDENTIFIER",
                                value, null };
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
                            value = "\"" + tokens.get(i + 2).getKey() + "\""; // Preserve quotes for string values
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

        System.out.println("\nToken List as Table:");
        printTable(array, columnNames);
        SampleTable st = new SampleTable(array, columnNames);
        st.createAndShowGUI();

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

    // public void traverse(TreeNode node){
    //// if (currentToken().getValue() == ("cofs")){
    //// if ( currentToken().getValue() == nextToken().getValue()){
    //// System.out.println("hatdofASLDKJA;SLDKJA;SLDK");
    //// System.out.println(currentToken().getValue());
    //// }
    ////// }
    //// System.out.println(nextToken().getValue());
    //
    // }
    public void traverse(TreeNode node) {
        if (node == null)
            return;
        // System.out.println("label: " + node.getLabel() + " || type: " +
        // node.getValue() );
        // if ( node.getValue() == nextNode().getValue()){
        // System.out.println("hatdofASLDKJA;SLDKJA;SLDK");
        //
        // }
        //
        // for (TreeNode child : node.getChildren()) {
        // traverse(child);
        // }
        System.out
                .println("index: " + node.getIndex() + "|| label: " + node.getLabel() + " || name: " + node.getName());
        // if (node.getLabel().equals("cofs")) {
        // String varName = node.getValue();
        // if (initializedVars.containsKey(varName)) {
        // System.err.println("Error: variable '" + varName + "' has already been
        // initialized.");
        // System.exit(1);
        // }
        // initializedVars.put(varName, "cofs");
        // System.out.println("Added variable " + varName + " of type 'cofs' to symbol
        // table.");
        // }

        // operations
        // if (node.getName() == "ADDITION" ){
        // if(){}
        // }

        // Continue traversal
        for (TreeNode child : node.getChildren()) {
            traverse(child);
        }

    }

    public void traverse() {
        traverse(parseTree);
    }
}
