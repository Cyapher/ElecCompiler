package org.example;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

public class TokenStream {
    private final List<SimpleEntry<String, String>> tokens;
    private int currentToken;

    public TokenStream(List<SimpleEntry<String, String>> tokens) {
        this.tokens = tokens;
        this.currentToken = 0;
    }

    public SimpleEntry<String, String> getNextToken() {
        if (currentToken < tokens.size()) {
            return tokens.get(currentToken++);
        }
        return null;
    }

    public SimpleEntry<String, String> peek() {
        if (currentToken < tokens.size()) {
            return tokens.get(currentToken);
        }
        return null;
    }

    public boolean hasNext() {
        return currentToken < tokens.size();
    }
}
