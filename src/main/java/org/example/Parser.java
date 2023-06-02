package org.example;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Parser {
	private List<SimpleEntry<String, String>> tokens; //value = type
	private int currentIndex;
	private int scope = 1;
	private HashMap<Integer, List<String>> identifiersList = new HashMap<Integer, List<String>>();
	private TreeNode parseTree; 

	public Parser(List<SimpleEntry<String, String>> tokens) {
		this.tokens = tokens;
		this.currentIndex = 0;
		this.identifiersList.put(scope, Arrays.asList());
	}

	private SimpleEntry<String, String> currentToken() {
		if (currentIndex >= tokens.size()) {
			return null;
		}
		return tokens.get(currentIndex);
	}

	private boolean hasTokens() {
		return currentIndex < tokens.size();
	}

	private SimpleEntry<String, String> nextToken() {
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

	private void expect(String type) {
		if (!accept(type)) {
			if (type.equals("EQUALITY"))
				throw new IllegalStateException("Expected token of type: EQUALITY but got: "
						+ (currentToken() != null ? currentToken().getValue() : "null"));
			else
				throw new IllegalStateException("Expected token of type: " + type + " but got: "
						+ (currentToken() != null ? currentToken().getValue() : "null"));
		}
	}

	public TreeNode parse() {
		System.out.println("============ IN PARSER ============");
		this.parseTree = new TreeNode("Program", "Program");

		while (hasTokens()) {
			this.parseTree.addChild(statement(parseTree));
		}

		return this.parseTree;
	}

	private SimpleEntry<String, String> peekNextToken() {
		if (currentIndex + 1 >= tokens.size()) {
			return null;
		}
		return tokens.get(currentIndex + 1);
	}

	//Statement
	private TreeNode statement(TreeNode parseTree) {
		TreeNode statementNode = null;

		if (isType("cofs")) {
			if (peekNextToken().getValue().equals("OPEN SQUARE")) {
				statementNode = cofsArrayInitialization();
			} else {
				statementNode = cofsInitialization();
			}
		} else if (isType("luts")) {
			if (peekNextToken().getValue().equals("OPEN SQUARE")) {
				statementNode = lutsArrayInitialization();
			} else {
				statementNode = lutsInitialization();
			}
		} else if (isType("Sinds")) {
			if (peekNextToken().getValue().equals("OPEN SQUARE")) {
				statementNode = sindsArrayInitialization();
			} else {
				statementNode = sindsInitialization();
			}
		} else if (isType("IDENTIFIER")) {
			if (peekNextToken().getValue().equals("OPEN SQUARE")) {
				statementNode = arrayAssignmentStatement();
			} else {
				statementNode = assignmentStatement();
			}
		} else if (isType("ifever")) {
			statementNode = ifEverStatement();
		} else if (isType("ifnot")) {
			statementNode = ifNotStatement();
		} else if (isType("unless")) {
			statementNode = UnlessStatement();
		} else if (isType("habang")) {
			statementNode = habangStatement();
		} else if (isType("makegawa")) {
			statementNode = makeGawaStatement();
		} else if (isType("kung")) {
			statementNode = kungStatement();
		} else if (isType("makeSulat")) {
			statementNode = makesulatStatement();
		} else if (isType("OPEN CURLY")) {
			statementNode = enterScope();
		} else if (isType("CLOSE CURLY")) {
			statementNode = exitScope();
		} else {
			throw new IllegalStateException("Unknown statement: " + currentToken().getKey());
		}

		return statementNode;
	}

	// ABSTRACT TREE CREATION =======================================

	// GENERAL METHODS -----------------------------------------------

	private TreeNode statements() {
		TreeNode statementsNode = new TreeNode("Statements", "Statements");
		statementsNode.setName("Statements");

		while (!isType("CLOSE CURLY")) {
			TreeNode statementNode = statement(statementsNode);
			statementsNode.addChild(statementNode);
		}

		return statementsNode;
	}

	// INITIALIZATION OF VARIABLES ----------------------------------

	// cofs(int) initialization
	private TreeNode cofsInitialization() {
		TreeNode node = new TreeNode("Cofs Initialization", "Cofs Initialization");
		node.setName("Cofs Initialization");

		TreeNode typeNode = new TreeNode("Type", "cofs");
		typeNode.setName("Cofs Type");
		node.addChild(typeNode);
		expect("cofs");

		TreeNode identifierNode = new TreeNode("Identifier", currentToken().getKey());
		identifierNode.setName("Variable Name");
		node.addChild(identifierNode);
		expect("IDENTIFIER");
		
		//System.out.println("PEEK: " + peekNextToken().getValue());

		if(currentAndNext()){
			throw new IllegalStateException("Unexpected Tokens");
		}

		if (accept("ASSIGNMENT")) {
			TreeNode assignmentNode = new TreeNode("Assignment", "=");
			assignmentNode.setName("Assignment Operator");
			node.addChild(assignmentNode);

			// expression / operator / operand
			// while loop for expression(!end of line)
			while (!isType("END OF LINE")) {

				// recognizing between parenthesis/non-parenthesis expressions and operands will
				// be done in expression()
				TreeNode expressionNode = expression();
				expressionNode.setName("Assigned Value");
				node.addChild(expressionNode);


				// i-angat ito??
				if (accept("CLOSE PARENTHESIS")) {
					throw new IllegalStateException("Unexpected token of type: CLOSE PARENTHESIS");
				}
			}
		}
		// else if(isType("ADDITION") || isType("SUBTRACTION") ||
		// isType("MULTIPLICATION") || isType("DIVISION") || isType("MODULO")){

		// TreeNode expressionNode = expression();
		// }

		expect("END OF LINE");

		return node;
	}

	// luts(float) initialization
	private TreeNode lutsInitialization() {
		TreeNode node = new TreeNode("Luts Initialization", "Luts Initialization");
		node.setName("Luts Initialization");

		TreeNode typeNode = new TreeNode("Type", "luts");
		typeNode.setName("Luts Type");
		node.addChild(typeNode);
		expect("luts");

		TreeNode identifierNode = new TreeNode("Identifier", currentToken().getKey());
		identifierNode.setName("Variable Name");
		node.addChild(identifierNode);
		expect("IDENTIFIER");

		if (accept("ASSIGNMENT")) {
			TreeNode assignmentNode = new TreeNode("Assignment", "=");
			assignmentNode.setName("Assignment Operator");
			node.addChild(assignmentNode);

			if (accept("OPEN PARENTHESIS")) {
				TreeNode openingP = new TreeNode("OPEN PARENTHESIS", "(");
				node.addChild(openingP);
				if (isType("luts")) {
					TreeNode castNode = casting();
					node.addChild(castNode);
				} else {
					// accept this >> (1.232+232) / 2 * 3
					TreeNode expressionNode = expression();
					expressionNode.setName("AFTER");
					node.addChild(expressionNode);
				}
			} else {
				TreeNode valueNode = new TreeNode("Value", currentToken().getKey());
				valueNode.setName("Assigned Value");
				node.addChild(valueNode);
				expect("FLOAT VALUE");
			}
		}

		expect("END OF LINE");
		return node;
	}

	// Sinds(String) initialization
	private TreeNode sindsInitialization() {
		TreeNode node = new TreeNode("Sinds Initialization", "Sinds Initialization");
		node.setName("Sinds Initialization");

		TreeNode typeNode = new TreeNode("Type", "Sinds");
		typeNode.setName("Sinds Type");
		node.addChild(typeNode);
		expect("Sinds");

		checkScope();

		TreeNode identifierNode = new TreeNode("Identifier", currentToken().getKey());
		identifierNode.setName("Variable Name");
		node.addChild(identifierNode);
		expect("IDENTIFIER");

		if (accept("ASSIGNMENT")) {
			TreeNode assignmentNode = new TreeNode("Assignment", "=");
			assignmentNode.setName("Assignment Operator");
			node.addChild(assignmentNode);

			TreeNode openingQuoteNode = new TreeNode("DOUBLE_QUOTE", "\"");
			openingQuoteNode.setName("Opening Double Quote");
			node.addChild(openingQuoteNode);
			expect("DOUBLE_QUOTE"); // Expect opening double quote

			TreeNode valueNode = new TreeNode("Value", currentToken().getKey());
			valueNode.setName("Assigned Value");
			node.addChild(valueNode);
			expect("STRING_VALUE");

			TreeNode closingQuoteNode = new TreeNode("DOUBLE_QUOTE", "\"");
			closingQuoteNode.setName("Closing Double Quote");
			node.addChild(closingQuoteNode);
			expect("DOUBLE_QUOTE"); // Expect closing double quote
		}

		TreeNode EOFNode = new TreeNode("END OF LINE", ";");
		EOFNode.setName("END OF LINE Operator");
		node.addChild(EOFNode);
		expect("END OF LINE");

		return node;
	}

	// INITIALIZATION OF ARRAYS -----------------------------------------

	// cofs(int) array initialization
	private TreeNode cofsArrayInitialization() {
		TreeNode node = new TreeNode("Cofs Array Initialization", "Cofs Array Initialization");
		node.setName("Cofs Array Initialization");

		TreeNode typeNode = new TreeNode("Type", "cofs");
		typeNode.setName("Cofs Type");
		node.addChild(typeNode);
		expect("cofs");

		// Handle array dimensions and initialization
		handleArrayDimensionsAndInitialization(node);

		TreeNode EOFNode = new TreeNode("END OF LINE", ";");
		EOFNode.setName("END OF LINE Operator");
		node.addChild(EOFNode);
		expect("END OF LINE");

		return node;
	}

	// luts(float) array initialization
	private TreeNode lutsArrayInitialization() {
		TreeNode node = new TreeNode("Luts Array Initialization", "Luts Array Initialization");
		node.setName("Luts Array Initialization");

		TreeNode typeNode = new TreeNode("Type", "luts");
		typeNode.setName("Luts Type");
		node.addChild(typeNode);
		expect("luts");

		// Handle array dimensions and initialization
		handleArrayDimensionsAndInitialization(node);

		TreeNode EOFNode = new TreeNode("END OF LINE", ";");
		EOFNode.setName("END OF LINE Operator");
		node.addChild(EOFNode);
		expect("END OF LINE");

		return node;
	}

	// Sinds(String) array initialization
	private TreeNode sindsArrayInitialization() {
		TreeNode node = new TreeNode("Sinds Array Initialization","Sinds Array Initialization");
		node.setName("Sinds Array Initialization");

		TreeNode typeNode = new TreeNode("Type", "Sinds");
		typeNode.setName("Sinds Type");
		node.addChild(typeNode);

		expect("Sinds");

		// Handle array dimensions and initialization
		handleArrayDimensionsAndInitialization(node);

		TreeNode EOFNode = new TreeNode("END OF LINE", ";");
		EOFNode.setName("END OF LINE Operator");
		node.addChild(EOFNode);
		expect("END OF LINE");

		return node;
	}

	private void handleArrayDimensionsAndInitialization(TreeNode node) {
		boolean hasExplicitSize = false; // Add this variable definition

		if (accept("OPEN SQUARE")) {
			TreeNode sizeNode = new TreeNode("Explicit Array Size");
			sizeNode.setName("Explicit Array Size");
			node.addChild(sizeNode);

			if (isType("INTEGER VALUE")) {
				TreeNode sizeValueNode = new TreeNode("INTEGER VALUE", currentToken().getValue());
				sizeNode.addChild(sizeValueNode);
				nextToken();
				hasExplicitSize = true;
			}

			expect("CLOSE SQUARE");
		}

		if (isType("IDENTIFIER")) {
			TreeNode identifierNode = new TreeNode("Identifier", currentToken().getKey());
			identifierNode.setName("Variable Name");
			node.addChild(identifierNode);
			expect("IDENTIFIER");
		}

		if (accept("ASSIGNMENT")) {
			TreeNode assignmentNode = new TreeNode("Assignment", "=");
			assignmentNode.setName("Assignment Operator");
			node.addChild(assignmentNode);

			expect("OPEN CURLY");

			TreeNode arrayElementsNode = new TreeNode("Array Elements");
			arrayElementsNode.setName("Array Elements");
			node.addChild(arrayElementsNode);

			while (!isType("CLOSE CURLY")) {
				TreeNode elementNode = new TreeNode("Array Element");
				elementNode.setName("Array Element");

				if (isType("INTEGER VALUE")) {
					TreeNode elementValueNode = new TreeNode("INTEGER VALUE", currentToken().getValue());
					elementNode.addChild(elementValueNode);
					arrayElementsNode.addChild(elementNode);

					nextToken(); // Consume the INTEGER VALUE token
				} else if (isType("FLOAT VALUE")) {
					TreeNode elementValueNode = new TreeNode("FLOAT VALUE", currentToken().getValue());
					elementNode.addChild(elementValueNode);
					arrayElementsNode.addChild(elementNode);

					nextToken(); // Consume the FLOAT VALUE token

				} else if (isType("DOUBLE_QUOTE")) {
					expect("DOUBLE_QUOTE");
					if (isType("STRING_VALUE")) {
						TreeNode elementValueNode = new TreeNode("STRING VALUE", currentToken().getValue());
						elementNode.addChild(elementValueNode);
						arrayElementsNode.addChild(elementNode);
						nextToken(); // Consume the STRING VALUE token
					}
					expect("DOUBLE_QUOTE");
				}

				else {
					throw new IllegalStateException(
							"Expected token of type: INTEGER VALUE, FLOAT VALUE, or STRING VALUE but got: "
									+ currentToken().getKey().toString());
				}

				// Add this check for the COMMA token
				if (isType("COMMA")) {
					nextToken(); // Consume the comma
				}
			}

			expect("CLOSE CURLY");
			// If there was an explicit array size, check if the number of elements matches
			// the specified size
			if (hasExplicitSize) {
				String explicitArraySizeValue = node.getChildByName("Explicit Array Size").getValue();
				if (!explicitArraySizeValue.isEmpty()) {
					int specifiedSize = Integer.parseInt(explicitArraySizeValue);
					int actualSize = arrayElementsNode.getChildren().size();

					if (specifiedSize != actualSize) {
						throw new IllegalStateException("Array size mismatch: specified " + specifiedSize
								+ ", but found " + actualSize + " elements");
					}
				}

			}
		}
	}

	private TreeNode arrayAssignmentStatement() {
		TreeNode node = new TreeNode("Array Assignment Statement", "Array Assignment Statement");
		node.setName("Array Assignment Statement");

		TreeNode identifierNode = new TreeNode("Identifier", currentToken().getKey());
		identifierNode.setName("Array Name");
		node.addChild(identifierNode);
		expect("IDENTIFIER");

		expect("OPEN SQUARE");

		TreeNode indexNode = new TreeNode("Array Index", currentToken().getKey());
		indexNode.setName("Array Index");
		node.addChild(indexNode);
		expect("INTEGER VALUE");

		expect("CLOSE SQUARE");

		TreeNode assignmentNode = new TreeNode("Assignment", "=");
		assignmentNode.setName("Assignment Operator");
		node.addChild(assignmentNode);
		expect("ASSIGNMENT");

		TreeNode valueNode = new TreeNode("Value", currentToken().getKey());
		valueNode.setName("Assigned Value");
		node.addChild(valueNode);

		if (isType("INTEGER VALUE")) {
			expect("INTEGER VALUE");
		} else if (isType("FLOAT VALUE")) {
			expect("FLOAT VALUE");
		} else if (isType("DOUBLE_QUOTE")) {
			expect("DOUBLE_QUOTE");
			if (isType("STRING VALUE")) {
				expect("STRING VALUE");
			}
			expect("DOUBLE_QUOTE");
		} else {
			throw new IllegalStateException(
					"Expected token of type: INTEGER VALUE, FLOAT VALUE, or STRING VALUE but got: "
							+ currentToken().getKey().toString());
		}

		TreeNode EOFNode = new TreeNode("END OF LINE", ";");
		EOFNode.setName("END OF LINE Operator");
		node.addChild(EOFNode);
		expect("END OF LINE");

		return node;
	}

	private TreeNode assignmentStatement() {
		TreeNode node = new TreeNode("Assignment Statement", "Assignment Statement");
		TreeNode identifierNode = new TreeNode("Identifier", currentToken().getKey());
		node.setName("Assignment Statement");
		identifierNode.setName("Assigned Variable");
		node.addChild(identifierNode);
		expect("IDENTIFIER");

		TreeNode assignmentNode = new TreeNode("Assignment", "=");
		assignmentNode.setName("Assignment Operator");
		node.addChild(assignmentNode);
		expect("ASSIGNMENT");

		node.addChild(expression());

		TreeNode EOFNode = new TreeNode("END OF LINE", ";");
		EOFNode.setName("END OF LINE Operator");
		node.addChild(EOFNode);
		expect("END OF LINE");

		return node;
	}

	// ARITHMETIC OPERATIONS ----------------------------------------

	private TreeNode expression() {
		TreeNode expressionNode = new TreeNode("Expression", "Expression");
		expressionNode.setName("Expression");
		if (isType("OPEN PARENTHESIS")) {
			TreeNode openingP = new TreeNode("OPEN PARENTHESIS", "(");
			expressionNode.addChild(openingP);
			nextToken();
			if (isType("luts") || isType("cofs")) {
				System.out.println("iraa");
				TreeNode castNode = casting();
				expressionNode.addChild(castNode);
			} else {
				System.out.println("else inside chain");
				TreeNode termNode = term();
				expressionNode.addChild(termNode);

				while (isType("ADDITION") || isType("SUBTRACTION") || isType("MODULO") || isType("LESS THAN")
						|| isType("GREATER THAN")) {
					TreeNode operatorNode = new TreeNode(currentToken().getValue(), currentToken().getKey());
					expressionNode.addChild(operatorNode);
					nextToken();

					termNode = term();
					expressionNode.addChild(termNode);
				}
				expect("CLOSE PARENTHESIS");
				TreeNode closingP = new TreeNode("CLOSE PARENTHESIS", ")");
				expressionNode.addChild(closingP);
			}

		} else if (isType("INTEGER VALUE") || isType("FLOAT VALUE")) {
			System.out.println("else outside chain");
			System.out.println(currentToken().getKey());
			TreeNode termNode = term();
			expressionNode.addChild(termNode);

			while (isType("ADDITION") || isType("SUBTRACTION") || isType("MODULO") || isType("LESS THAN")
					|| isType("GREATER THAN")) {
				TreeNode operatorNode = new TreeNode(currentToken().getValue(), currentToken().getKey());
				expressionNode.addChild(operatorNode);
				nextToken();

				termNode = term();
				expressionNode.addChild(termNode);
			}
		}

		else if (!currentAndNext()) {
			TreeNode operatorNode = new TreeNode(currentToken().getValue(), currentToken().getKey());
			operatorNode.setName("Operator");
			expressionNode.addChild(operatorNode);
			nextToken();
		}	

		return expressionNode;
	}

	private boolean currentAndNext() {
		boolean curr = false;
		boolean next = false;
		if (isType("ADDITION") || isType("SUBTRACTION") || isType("MODULO") || isType("LESS THAN") || isType("GREATER THAN") || isType("ASSIGNMENT")) {
			curr = true;
		}
		if ((peekNextToken().getValue() == "ADDITION")  
		|| (peekNextToken().getValue() == "SUBTRACTION") 
		|| (peekNextToken().getValue() == "MODULO")
		|| (peekNextToken().getValue() == "LESS THAN")
		|| (peekNextToken().getValue() == "GREATER THAN")) {
			next = true;
		}

		if (curr & next) {
			return true;
		} else {
			return false;
		}
	}

	private TreeNode term() {
		TreeNode termNode = new TreeNode("Term", "Term");
		termNode.setName("Term");

		TreeNode factorNode = factor();
		termNode.addChild(factorNode);

		while (isType("MULTIPLICATION") || isType("DIVISION")) {
			TreeNode operatorNode = new TreeNode("Operator", currentToken().getKey());
			termNode.addChild(operatorNode);
			nextToken();

			factorNode = factor();
			termNode.addChild(factorNode);
		}

		return termNode;
	}

	private TreeNode factor() {
		if (isType("OPEN PARENTHESIS")) {
			expect("OPEN PARENTHESIS");
			TreeNode expressionNode = expression();
			expect("CLOSE PARENTHESIS");
			return expressionNode;
		}

		TreeNode node;

		if (isType("INTEGER VALUE")) {
			node = new TreeNode("Value", currentToken().getKey());
			node.setName("Integer Value");
			nextToken();
		} else if (isType("FLOAT VALUE")) {
			node = new TreeNode("Value", currentToken().getKey());
			node.setName("Float Value");
			nextToken();
		} else if (isType("IDENTIFIER")) {
			node = new TreeNode("Identifier", currentToken().getKey());
			node.setName("Variable Name");
			nextToken();
		} else if (isType("EQUALITY")) {
			node = new TreeNode("Equality Operator", currentToken().getValue());
			nextToken();
			if (isType("INTEGER VALUE")) {
				TreeNode valueNode = new TreeNode("Value", currentToken().getKey());
				valueNode.setName("Integer Value");
				node.addChild(valueNode);
				nextToken();
			} else if (isType("FLOAT VALUE")) {
				TreeNode valueNode = new TreeNode("Value", currentToken().getKey());
				valueNode.setName("Float Value");
				node.addChild(valueNode);
				nextToken();
			} else if (isType("IDENTIFIER")) {
				TreeNode idNode = new TreeNode("Identifier", currentToken().getKey());
				idNode.setName("Variable Name");
				node.addChild(idNode);
				nextToken();
			} else {
				throw new IllegalStateException(
						"Expected integer, float, or identifier after equality operator, but got: "
								+ currentToken().getValue());
			}
		} else {
			throw new IllegalStateException(
					"Expected integer, float, identifier, or equality operator, but got: " + currentToken().getValue());
		}

		return node;
	}

	// RELATIONAL OPERATIONS ----------------------------------------

	private TreeNode condition() {
		TreeNode conditionNode = new TreeNode("condition", "condition");
		conditionNode.setName("condition");

		TreeNode termNode = term();
		conditionNode.addChild(termNode);

		while (isType("LESS THAN") || isType("GREATER THAN") || isType("EQUALITY") || isType("NON EQUALITY")
				|| isType("LESS THAN EQUAL TO") || isType("GREATER THAN EQUAL TO")) {
			TreeNode operatorNode = new TreeNode("RelOperator", currentToken().getKey());
			conditionNode.addChild(operatorNode);
			nextToken();

			termNode = term();
			conditionNode.addChild(termNode);
		}

		return conditionNode;
	}

	// LOGICAL OPERATIONS -------------------------------------------

	private TreeNode logCondition() { // parang term din pero it's here para sa precedence
		TreeNode logconNode = new TreeNode("logcondition", "logcondition");
		logconNode.setName("logcondition");

		TreeNode conditionNode = condition();
		logconNode.addChild(conditionNode);

		while (isType("TAPS") || isType("GINTS") || isType("DEINS")) {
			TreeNode operatorNode = new TreeNode(currentToken().getValue(), currentToken().getKey());
			logconNode.addChild(operatorNode);
			nextToken();

			conditionNode = condition();
			logconNode.addChild(conditionNode);
		}

		return logconNode;
	}

	// TYPE CASTING -------------------------------------------------

	// // For Explicit Casting
	private TreeNode casting() {
		System.out.println("cast");
		TreeNode castCheckNode = new TreeNode("Casting", "Casting");
		castCheckNode.setName("castCheckNode");

		if (isType("luts") || isType("cofs")) {
			TreeNode castNode = new TreeNode(currentToken().getValue(), currentToken().getKey());
			castCheckNode.addChild(castNode);
			System.out.println(castNode);
			nextToken();

			TreeNode targetTypeNode = new TreeNode(currentToken().getValue(), currentToken().getKey());
			castCheckNode.addChild(targetTypeNode);
			nextToken();

			TreeNode valueNode = expression(); // Call expression() to handle the casting value
			castCheckNode.addChild(valueNode);
		}
		return castCheckNode;
	}

	// Type Cast Error Checking
	private void castError(String reason) {
		throw new IllegalStateException("Cast Error" + reason);
	}

	// SCOPING ------------------------------------------------------

	private TreeNode enterScope() {
		TreeNode node = new TreeNode("Enter Scope", "Enter Scope");
		node.setName("Enter Scope");

		TreeNode scopeEntry = new TreeNode("Enter Scope", "{");
		scopeEntry.setName("Enter Scope");
		node.addChild(scopeEntry);
		expect("OPEN CURLY");
		this.scope++;
		this.identifiersList.put(scope, Arrays.asList());
		// System.out.println("enter" + this.scope);

		return node;
	}

	private TreeNode exitScope() {
		TreeNode node = new TreeNode("Exit Scope", "Exit Scope");
		node.setName("Exit Scope");

		TreeNode scopeExit = new TreeNode("Exit Scope", "}");
		scopeExit.setName("Enter Scope");
		node.addChild(scopeExit);
		expect("CLOSE CURLY");

		this.identifiersList.get(scope).clear();
		this.scope--;
		// System.out.println("exit" + this.scope);

		return node;
	}

	private boolean checkScope() {
		boolean check = false;

		List<String> identifiersCheckList = new ArrayList<String>();

		// System.out.println(currentToken().getKey().toString());
		// System.out.println(identifiersCheckList);

		for (String entry : this.identifiersList.get(scope)) { // check specific List<String> based on current scope
			identifiersCheckList.add(entry);
		}

		if (identifiersCheckList.contains(currentToken().getKey().toString())) {
			throw new IllegalStateException("Repeated variable name: " + currentToken().getKey().toString());
		} else {
			identifiersCheckList.add(currentToken().getKey().toString());
		}

		// System.out.println(identifiersCheckList);
		this.identifiersList.put(scope, identifiersCheckList);

		return check;
	}

	// CONTROL STRUCTURES -------------------------------------------

	// ifEver(if) Statement
	private TreeNode ifEverStatement() {
		TreeNode ifevernode = new TreeNode("ifEver Statement","ifEver Statement");
		ifevernode.setName("ifEver Statement");

		TreeNode typeNode = new TreeNode("Type", "ifever");
		typeNode.setName("ifever Type");
		ifevernode.addChild(typeNode);
		expect("ifever");

		if (accept("OPEN PARENTHESIS")) {
			TreeNode openParNode = new TreeNode("OPEN PARENTHESIS", "(");
			openParNode.setName("OpenPar Operator");
			ifevernode.addChild(openParNode);

			TreeNode logconNode = logCondition();
			logconNode.setName("Condition");
			ifevernode.addChild(logconNode);

			TreeNode closeParNode = new TreeNode("CLOSE PARENTHESIS", ")");
			closeParNode.setName("Closing paren ");
			ifevernode.addChild(closeParNode);
			expect("CLOSE PARENTHESIS");
		}

		if (accept("OPEN CURLY")) {
			TreeNode openCurlyNode = new TreeNode("OPEN CURLY", "{");
			openCurlyNode.setName("OpenCurly Operator");
			ifevernode.addChild(openCurlyNode);

			TreeNode statementsNode = statements();
			statementsNode.setName("Statements");
			ifevernode.addChild(statementsNode);

			TreeNode closeCurlyNode = new TreeNode("CLOSE CURLY", "}");
			closeCurlyNode.setName("Closing Curly ");
			ifevernode.addChild(closeCurlyNode);
			expect("CLOSE CURLY");
		}

		return ifevernode;
	}

	// ifNot(else) Statement
	private TreeNode ifNotStatement() {
		TreeNode ifNotnode = new TreeNode("ifNot Statement", "ifNot Statement");

		TreeNode typeNode = new TreeNode("Type", "ifnot");
		typeNode.setName("ifnot Type");
		ifNotnode.addChild(typeNode);
		expect("ifnot");

		if (accept("OPEN CURLY")) {
			TreeNode openCurlyNode = new TreeNode("OPEN CURLY", "{");
			openCurlyNode.setName("OpenCurly Operator");
			ifNotnode.addChild(openCurlyNode);

			TreeNode statementsNode = statements();
			statementsNode.setName("Statements");
			ifNotnode.addChild(statementsNode);

			TreeNode closeCurlyNode = new TreeNode("CLOSE CURLY", "}");
			closeCurlyNode.setName("Closing Curly ");
			ifNotnode.addChild(closeCurlyNode);
			expect("CLOSE CURLY");
		}

//		expect("END OF LINE");
		return ifNotnode;
	}

	// unless(elseIf) Statement
	private TreeNode UnlessStatement() {
		TreeNode unlessnode = new TreeNode("unless Statement", "unless Statement");

		TreeNode typeNode = new TreeNode("Type", "unless");
		typeNode.setName("unless Type");
		unlessnode.addChild(typeNode);
		expect("unless");

		if (accept("OPEN PARENTHESIS")) {
			TreeNode openParNode = new TreeNode("OPEN PARENTHESIS", "(");
			openParNode.setName("OpenPar Operator");
			unlessnode.addChild(openParNode);

			TreeNode logconNode = logCondition();
			logconNode.setName("Condition");
			unlessnode.addChild(logconNode);

			TreeNode closeParNode = new TreeNode("CLOSE PARENTHESIS", ")");
			closeParNode.setName("Closing paren ");
			unlessnode.addChild(closeParNode);
			expect("CLOSE PARENTHESIS");
		}

		if (accept("OPEN CURLY")) {
			TreeNode openCurlyNode = new TreeNode("OPEN CURLY", "{");
			openCurlyNode.setName("OpenCurly Operator");
			unlessnode.addChild(openCurlyNode);

			TreeNode statementsNode = statements();
			statementsNode.setName("Statements");
			unlessnode.addChild(statementsNode);

			TreeNode closeCurlyNode = new TreeNode("CLOSE CURLY", "}");
			closeCurlyNode.setName("Closing Curly ");
			unlessnode.addChild(closeCurlyNode);
			expect("CLOSE CURLY");
		}

		return unlessnode;
	}

	// habang(while) loop
	private TreeNode habangStatement() {
		TreeNode Habangnode = new TreeNode("habang Statement", "habang Statement");

		TreeNode typeNode = new TreeNode("Type", "habang");
		typeNode.setName("habangnode Type");
		Habangnode.addChild(typeNode);
		expect("habang");

		if (accept("OPEN PARENTHESIS")) {
			TreeNode openParNode = new TreeNode("OPEN PARENTHESIS", "(");
			openParNode.setName("OpenPar Operator");
			Habangnode.addChild(openParNode);

			TreeNode logconNode = logCondition();
			logconNode.setName("Condition");
			Habangnode.addChild(logconNode);

			TreeNode closeParNode = new TreeNode("CLOSE PARENTHESIS", ")");
			closeParNode.setName("Closing paren ");
			Habangnode.addChild(closeParNode);
			expect("CLOSE PARENTHESIS");
		}
		if (accept("OPEN CURLY")) {
			TreeNode openCurlyNode = new TreeNode("OPEN CURLY", "{");
			openCurlyNode.setName("OpenCurly Operator");
			Habangnode.addChild(openCurlyNode);

			TreeNode statementsNode = statements();
			statementsNode.setName("Statements");
			Habangnode.addChild(statementsNode);

			TreeNode closeCurlyNode = new TreeNode("CLOSE CURLY", "}");
			closeCurlyNode.setName("Closing Curly ");
			Habangnode.addChild(closeCurlyNode);
			expect("CLOSE CURLY");
		}
//		expect("END OF LINE");
		return Habangnode;
	}

	// makeGawa(do-while) loop
	private TreeNode makeGawaStatement() {
		TreeNode makeGawanode = new TreeNode("makegawa Statement", "makegawa Statement");

		TreeNode typeNode = new TreeNode("Type", "makegawa");
		typeNode.setName("makegawa Type");
		makeGawanode.addChild(typeNode);
		expect("makegawa");

		if (accept("OPEN CURLY")) {
			TreeNode openCurlyNode = new TreeNode("OPEN CURLY", "{");
			openCurlyNode.setName("OpenCurly Operator");
			makeGawanode.addChild(openCurlyNode);

			TreeNode statementsNode = statements();
			statementsNode.setName("Statements");
			makeGawanode.addChild(statementsNode);

			TreeNode closeCurlyNode = new TreeNode("CLOSE CURLY", "}");
			closeCurlyNode.setName("Closing Curly ");
			makeGawanode.addChild(closeCurlyNode);
			expect("CLOSE CURLY");
		}

		if (accept("habang")) {
			TreeNode habangNode = new TreeNode("Type", "habang");
			habangNode.setName("habangnode Type");
			makeGawanode.addChild(habangNode);

			TreeNode openParNode = new TreeNode("OPEN PARENTHESIS", "(");
			openParNode.setName("OpenPar Operator");
			makeGawanode.addChild(openParNode);
			expect("OPEN PARENTHESIS");

			TreeNode logconNode = logCondition();
			logconNode.setName("Condition");
			makeGawanode.addChild(logconNode);

			TreeNode closeParNode = new TreeNode("CLOSE PARENTHESIS", ")");
			closeParNode.setName("Closing paren ");
			makeGawanode.addChild(closeParNode);
			expect("CLOSE PARENTHESIS");
		}
		TreeNode EOFNode = new TreeNode("END OF LINE", ";");
		EOFNode.setName("END OF LINE Operator");
		makeGawanode.addChild(EOFNode);
		expect("END OF LINE");
		return makeGawanode;
	}

	private TreeNode kungStatement() {
		TreeNode kungnode = new TreeNode("kung Statement");

		TreeNode typeNode = new TreeNode("Type", "kung");
		typeNode.setName("kung Type");
		kungnode.addChild(typeNode);
		expect("kung");

		TreeNode openParNode = new TreeNode("OPEN PARENTHESIS", "(");
		openParNode.setName("OpenPar Operator");
		kungnode.addChild(openParNode);
		expect("OPEN PARENTHESIS");

		// Initialization Expression
		TreeNode initializationNode = cofsInitialization();
		initializationNode.setName("Initialization");
		kungnode.addChild(initializationNode);

		// Condition Expression
		TreeNode conditionNode = logCondition();
		conditionNode.setName("Condition");
		kungnode.addChild(conditionNode);
		TreeNode EOFNode = new TreeNode("END OF LINE", ";");
		EOFNode.setName("END OF LINE Operator");
		conditionNode.addChild(EOFNode);
		expect("END OF LINE");

		//increment

		TreeNode expressionNode = updateExpression();
		expressionNode.setName("Update");
		kungnode.addChild(expressionNode);

		expect("CLOSE PARENTHESIS");

		//Statement
		if (accept("OPEN CURLY")) {
			TreeNode openCurlyNode = new TreeNode("OPEN CURLY", "{");
			openCurlyNode.setName("OpenCurly Operator");
			kungnode.addChild(openCurlyNode);

			TreeNode statementsNode = statements();
			statementsNode.setName("Statements");
			kungnode.addChild(statementsNode);

			TreeNode closeCurlyNode = new TreeNode("CLOSE CURLY", "}");
			closeCurlyNode.setName("Closing Curly ");
			kungnode.addChild(closeCurlyNode);
			expect("CLOSE CURLY");
		}

		return kungnode;
	}

	private TreeNode updateExpression() {
		TreeNode updateNode = new TreeNode("Update", "Update");

		if (isType("IDENTIFIER")) {
			TreeNode identifierNode = new TreeNode("Identifier", currentToken().getKey());
			updateNode.addChild(identifierNode);
			nextToken();

			if (accept("ADDITION") && isType("ADDITION")) { //Increment
				TreeNode incrementNode = new TreeNode("Increment", "++");
				incrementNode.setName("Increment");
				updateNode.addChild(incrementNode);
				expect("ADDITION");
			} else if (accept("SUBRACTION") && isType("ADDITION")) {
				TreeNode incrementNode = new TreeNode("Decrement", "--");
				incrementNode.setName("Decrement");
				updateNode.addChild(incrementNode);
			}
		}

		return updateNode;
	}


	// FUNCTIONS ----------------------------------------------------

	// Print/MakeSulat Statement
	private TreeNode makesulatStatement() {
		TreeNode node = new TreeNode("makeSulat Statement", "makeSulat Statement");
		node.setName("makeSulat Statement");

		expect("makeSulat");

		if (accept("OPEN PARENTHESIS")) {
			TreeNode openParNode = new TreeNode("OPEN PARENTHESIS", "(");
			openParNode.setName("OpenPar Operator");
			node.addChild(openParNode);

			while (!isType("CLOSE PARENTHESIS")) {
				if (isType("DOUBLE_QUOTE")) { // for Strings
					TreeNode openingQuoteNode = new TreeNode("DOUBLE_QUOTE", "\"");
					openingQuoteNode.setName("Opening Double Quote");
					node.addChild(openingQuoteNode);
					expect("DOUBLE_QUOTE"); // Expect opening double quote

					TreeNode valueNode = new TreeNode("Value", currentToken().getKey());
					valueNode.setName("Assigned Value");
					node.addChild(valueNode);
					expect("STRING_VALUE");

					TreeNode closingQuoteNode = new TreeNode("DOUBLE_QUOTE", "\"");
					closingQuoteNode.setName("Closing Double Quote");
					node.addChild(closingQuoteNode);
					expect("DOUBLE_QUOTE"); // Expect closing double quote

				} else if (isType("IDENTIFIER")) {
					TreeNode idNode = new TreeNode("IDENTIFIER", currentToken().getKey());
					idNode.setName("IDENTIFIER");
					node.addChild(idNode);
					expect("IDENTIFIER");
					
				}else if (isType("ADDITION")) {
					TreeNode concatNode = new TreeNode("CONCAT", "+");
					concatNode.setName("CONCAT");
					node.addChild(concatNode);
					expect("ADDITION");

				} else if (isType("INTEGER VALUE")) {
					TreeNode intNode = new TreeNode("INTEGER VALUE", currentToken().getKey());
					intNode.setName("INTEGER VALUE");
					node.addChild(intNode);
					expect("INTEGER VALUE");

				} else if (!accept("DOUBLE_QUOTE") && !accept("INTEGER VALUE")) { // for expressions
					node.addChild(expression());

				} else {
					throw new IllegalStateException(
							"Expected string or expression, but got: " + currentToken().getValue());
				}
			}

			TreeNode closingPar = new TreeNode("CLOSE PARENTHESIS", ")");
			node.addChild(closingPar);
			expect("CLOSE PARENTHESIS");

		}

		TreeNode EOFNode = new TreeNode("END OF LINE", ";");
		EOFNode.setName("END OF LINE Operator");
		node.addChild(EOFNode);
		expect("END OF LINE");
		return node;
	}

	// GETTER ----------------------------------------------------
	public TreeNode getTree(){
		return parseTree;
	}

	

}