package org.example;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Semantic {
    private HashMap<String, String> initializedVars = new HashMap<>();
    private HashMap<String, String> varValues = new HashMap<>();

    private List<AbstractMap.SimpleEntry<String, String>> tokens;
    private int currentIndex;
    private int scope = 1;
    private HashMap<Integer, List<String>> identifiersList = new HashMap<>();
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

    public void traverse(TreeNode node) {
        if (node == null) return;

        TreeNode parentNode = node; // Set parent node as the current node

        for (TreeNode child : node.getChildren()) {
            System.out.println("Parent: " + parentNode.getLabel());
            System.out.println("Child: " + child.getLabel());
        }
 //           traverse(child);
        if (node.getLabel().equals("Cofs Initialization") || node.getLabel().equals("Luts Initialization") || node.getLabel().equals("Sinds Initialization")) {
            String varName = node.getChildren().get(1).getValue(); // 42?
            String type = node.getChildren().get(0).getValue(); // cofs

            // Check if variable has already been initialized with a different type
            if (initializedVars.containsKey(varName)) {
                if (!initializedVars.get(varName).equals(type)) {
                    System.err.println("Error: variable '" + varName + "' cannot be reinitialized with a different type.");
                    System.exit(1);
                } else {
                    System.out.println("Variable " + varName + " already initialized with type '" + type + "'.");
                }
            } else {
                initializedVars.put(varName, type);
                varValues.put(varName, null); // When a variable is initialized, set its value to null
                System.out.println("Added variable " + varName + " of type '" + type + "' to symbol table.");
            }

            if(node.getChildren().get(2).getLabel().startsWith("ASSIGNMENT")) {
                //try print

                TreeNode childs = node.getChildren().get(3).getChildren().get(0).getChildren().get(0);
                System.out.println("childs:" + childs.getValue());
                String value = childs.getName();
                String assignedType = initializedVars.get(varName);
                System.out.println("initializedVars Assigned Type:" + initializedVars.get(varName));
                if (assignedType == null) {
                    System.err.println("Error: variable '" + varName + "' has not been initialized.");
                    System.exit(1);
                }

                if ((assignedType.equals("Type (Cofs Type)") || assignedType.equals("Type (Luts Type)")) && !isNumeric(value)) {
                    System.err.println("Error: variable '" + varName + "' of type '" + assignedType + "' cannot be assigned a non-numeric value.");
                    System.exit(1);
                }

                if (assignedType.equals("Type (Sinds Type)") && isNumeric(value)) {
                    System.err.println("Error: variable '" + varName + "' of type '" + assignedType + "' cannot be assigned a numeric value.");
                    System.exit(1);
                }

                // When a variable is assigned a value, update its value in the varValues hashmap
                varValues.put(varName, value);
            }
        }


        else if (node.getLabel().equals("Assignment Statement"))
        {
            TreeNode childz = node.getChildren().get(0);
            String checker = childz.getValue();
            System.out.println("childz: "+ checker);

            for (String value : initializedVars.keySet()) {
                System.out.println("soutedvalue:" + value);
                if(checker.equals(value))
                {
                    TreeNode childsVal = node.getChildren().get(2).getChildren().get(0).getChildren().get(0); //4322
                    int childsValInt = Integer.parseInt(childsVal.getValue());
                    if(initializedVars.containsKey(checker))
                    {
                        varValues.replace(checker, childsVal.getValue());
                }

                }
                System.out.println("children is present");
            }

        }
        System.out.println("varvalMap: "+ varValues);
        System.out.println("initVarMap: "+initializedVars);






            // Continue traversal
            for (TreeNode childs : node.getChildren()) {
                traverse(childs);
            }
        }



    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

        public void traverse () {
            traverse(parseTree);
            for (String value : initializedVars.keySet()) {
                System.out.println(value + ": " + varValues.get(value));
            }
            // After traversal, print the current state of initializedVars and varValues
            System.out.println("Current state of initializedVars: " + initializedVars);
            System.out.println("Current state of varValues: " + varValues);
        }
    }
