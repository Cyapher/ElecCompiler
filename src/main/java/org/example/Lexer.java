package org.example;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Lexer {

	public List<SimpleEntry<String, String>> analyzeTokens() throws IOException {
		List<SimpleEntry<String, String>> symbol_table = SYMBOL_TABLE();

		try {
			// Open the source code file
			File file = new File("src/main/java/org/example/source.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));

			// Initialize the lexical analyzer
			boolean isBreakTriggered = false;

			// to find line error
			// int lineNumber = 1;
			HashMap<String, String> reserved = RESERVED();
			HashMap<String, String> ar_ops = AR_OPS();
			HashMap<String, String> rel_ops = REL_OPS();
			HashMap<String, String> special_symbols = SPECIAL_SYMBOLS();
			List<Character> letters = LETTERS();
			List<Character> digits_char = DIGITS_CHAR();
			List<Character> identifiers = IDENTIFIERS();

			// Analyze each token in the source code
			String line;
			while ((line = reader.readLine()) != null) {
				// Tokenize each line using regular expressions
				// Pattern pattern = Pattern.compile("\\b\\w+\\b|\\S");
				Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\b\\d*\\.?\\d+\\b|\\b\\w+\\b|\\S");
				Matcher matcher = pattern.matcher(line);

				while (matcher.find()) {
					String currentToken = matcher.group();
					if (reserved.containsKey(currentToken)) {
						symbol_table.add(new SimpleEntry<>(currentToken, reserved.get(currentToken)));
					} else if (ar_ops.containsKey(currentToken)) {
						symbol_table.add(new SimpleEntry<>(currentToken, ar_ops.get(currentToken)));
					} else if (rel_ops.containsKey(currentToken)) {
						symbol_table.add(new SimpleEntry<>(currentToken, rel_ops.get(currentToken)));
					} else if (special_symbols.containsKey(currentToken)) {
						if (currentToken.equals("\"")) {
							symbol_table.add(new SimpleEntry<>(currentToken, "DOUBLE_QUOTE"));
						} else {
							symbol_table.add(new SimpleEntry<>(currentToken, special_symbols.get(currentToken)));
						}
					} else if (currentToken.charAt(0) == '\"' &&
							currentToken.charAt(currentToken.length() - 1) == '\"') {
						String stringWithoutQuotes = currentToken.substring(1, currentToken.length() - 1);
						symbol_table.add(new SimpleEntry<>("\"", "DOUBLE_QUOTE")); // Add opening double quote
						symbol_table.add(new SimpleEntry<>(stringWithoutQuotes, "STRING_VALUE")); // Add string value
																									// without double
																									// quotes
						symbol_table.add(new SimpleEntry<>("\"", "DOUBLE_QUOTE")); // Add closing double quote
					} else if (!reserved.containsKey(currentToken) || !ar_ops.containsKey(currentToken)
							|| !rel_ops.containsKey(currentToken) || !special_symbols.containsKey(currentToken)) {
						List<Character> token = new ArrayList<>();
						for (char c : currentToken.toCharArray()) {
							token.add(c);
						}
						if (digits_char.containsAll(token)) {
							if (!symbol_table.contains(currentToken)) {
								symbol_table.add(new SimpleEntry<>(currentToken, "INTEGER VALUE"));
							}

						} else if (containsSingleDot(currentToken)) {
							if (!symbol_table.contains(currentToken)) {
								symbol_table.add(new SimpleEntry<>(currentToken, "FLOAT VALUE"));
							}
						} else if (identifiers.containsAll(token)) {
							// checks if first char in string is a digit
							if (!digits_char.contains(currentToken.charAt(0))) {
								// checks if current token is a reserved word
								if (!reserved.containsKey(currentToken)) {
									symbol_table.add(new SimpleEntry<>(currentToken, "IDENTIFIER"));
								} else {
									System.out.println(currentToken + ": is a RESERVED word");
									isBreakTriggered = true;
									break;
								}

							} else {
								System.out.println(currentToken + ": not a valid identifier");
								throw new IllegalArgumentException("Invalid identifier name");
							}
						} else {
							System.out.println(currentToken + ": invalid Identifier name");
							throw new IllegalArgumentException("Invalid identifier name");
						}

					} else {
						System.out.println(currentToken + ": UNKNOWN TOKEN");
						throw new IllegalArgumentException("Unknown token");
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return symbol_table;
	}

	private static HashMap<String, String> RESERVED() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("cofs", "cofs"); // int
		map.put("luts", "luts"); // float
		map.put("Sinds", "Sinds"); // String
		map.put("TAPS", "TAPS"); // AND
		map.put("GINTS", "GINTS"); // OR
		map.put("DEINS", "DEINS"); // NOT
		map.put("ifever", "ifever"); // if operation
		map.put("ifnot", "ifnot"); // else operation
		map.put("unless", "ELSE IF");// else if operation
		map.put("makegawa", "makegawa");// do operation
		map.put("habang", "habang");// while operation
		map.put("kung", "kung");// for operation
		map.put("makeSulat", "makeSulat");
		return map;
	}

	private static HashMap<String, String> SPECIAL_SYMBOLS() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("{", "OPEN CURLY");
		map.put("}", "CLOSE CURLY");
		map.put("(", "OPEN PARENTHESIS");
		map.put(")", "CLOSE PARENTHESIS");
		map.put("[", "OPEN SQUARE");
		map.put("]", "CLOSE SQUARE");
		map.put("=", "ASSIGNMENT");
		map.put(",", "COMMA");
		map.put(";", "END OF LINE");
		map.put("\"", "DOUBLE QUOTES");
		map.put("\'", "SINGLE QUOTES");
		return map;
	}

	private static HashMap<String, String> AR_OPS() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("+", "ADDITION");
		map.put("-", "SUBTRACTION");
		map.put("*", "MULTIPLICATION");
		map.put("/", "DIVISION");
		map.put("%", "MODULO");
		return map;
	}

	private static HashMap<String, String> REL_OPS() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("⩵", "EQUALITY");
		map.put("≤", "LESS THAN EQUAL TO");
		map.put("≥", "GREATER THAN EQUAL TO");
		map.put("≠", "NON EQUALITY");
		map.put("<", "LESS THAN");
		map.put(">", "GREATER THAN");
		return map;
	}

	private static List<Character> LETTERS() {
		List<Character> letterChar = new ArrayList<>();
		for (int i = 97; i < 123; i++) {
			letterChar.add((char) i);
		}

		for (int i = 65; i <= 90; i++) {
			letterChar.add((char) i);
		}
		return letterChar;
	}

	private static List<Character> DIGITS_CHAR() {
		List<Character> digits_char = new ArrayList<>();
		for (int i = 48; i < 58; i++) {
			digits_char.add((char) i);
		}
		return digits_char;
	}

	private static List<Character> IDENTIFIERS() {
		List<Character> digits_char = DIGITS_CHAR();
		List<Character> letter = LETTERS();
		List<Character> identifiers = new ArrayList<>();
		identifiers.addAll(letter);
		identifiers.addAll(digits_char);
		identifiers.add((char) 95);
		return identifiers;
	}

	private static List<SimpleEntry<String, String>> SYMBOL_TABLE() {
		return new ArrayList<>();
	}

	public static boolean containsSingleDot(String str) {
		// Use regular expression to check if the string contains exactly one dot
		return str.matches(".*\\..*") && !str.matches(".*\\..*\\..*");
	}

}