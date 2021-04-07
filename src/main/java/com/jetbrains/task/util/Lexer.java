package com.jetbrains.task.util;

import java.util.Arrays;
import java.util.List;

public class Lexer {

    private final String input;
    private int pos = 0;
    private boolean isExhausted = false;

    public Lexer(String s) {
        input = s;
    }

    public Token getNextToken() {
        if (isExhausted) {
            throw new IllegalStateException();
        }

        ignoreWhiteSpaces();
        Token curToken = null;

        isExhausted = true;
        for (Token tok : Token.values()) {
            int endOfToken = tok.endOfMatch(input.substring(pos));

            if (endOfToken != -1) {
                curToken = tok;
                pos += endOfToken;
                isExhausted = pos >= input.length();
                break;
            }
        }

        return curToken;
    }

    public boolean isExhausted() {
        return isExhausted;
    }

    public int getCurPos() {
        return pos;
    }

    public String getInputSubstr(int start) {
        return input.substring(start, pos - 1).replaceAll("\\s+", "");
    }

    private void ignoreWhiteSpaces() {
        List<Character> whiteSpaces = Arrays.asList(' ', '\n', '\t');
        while (pos < input.length() && whiteSpaces.contains(input.charAt(pos))) {
            pos++;
        }
    }


}
