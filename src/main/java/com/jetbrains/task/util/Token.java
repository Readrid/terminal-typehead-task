package com.jetbrains.task.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Token {

    TK_PLUS ("\\+"),
    TK_MINUS ("-"),
    TK_MUL ("\\*"),
    TK_LESS ("<"),
    TK_GT (">"),
    TK_EQ ("="),
    TK_AND ("&"),
    TK_OR ("\\|"),
    TK_LPAREN ("\\("),
    TK_RPAREN ("\\)"),
    TK_RBRACKET ("\\}"),
    TK_CALL ("%>%"),
    TK_KEY_MAP ("map\\{"),
    TK_KEY_FILTER ("filter\\{"),
    TK_KEY_ELEMENT ("element"),

    INTEGER ("\\d+");


    private final Pattern pattern;

    Token(String regex) {
        pattern = Pattern.compile("^" + regex);
    }

    public int endOfMatch(String s) {
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return matcher.end();
        }
        return -1;
    }


}
