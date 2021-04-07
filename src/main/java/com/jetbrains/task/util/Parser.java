package com.jetbrains.task.util;

public class Parser {

    private final Lexer lexer;
    private Token curToken;

    private String curExpression;
    private String curElement;
    private String errorMessage;

    public Parser(String input) {
        lexer = new Lexer(input);
        curToken = lexer.getNextToken();
        curElement = "element";
        curExpression = "";
        errorMessage = "SYNTAX ERROR";
    }

    public boolean parse() {
        if (!parseCallChain()) {
            System.out.println(errorMessage);
            return false;
        }

        return true;
    }

    public String getSimplifiedLine() {
        if (curExpression.isEmpty()) {
            curExpression = "(1=1)";
        }
        return "filter{" + curExpression + "}%>%map{" + curElement + "}";
    }

    public boolean parseCallChain() {
        if (!parseCall()) {
            return false;
        }

        while (!lexer.isExhausted()) {
            if (!accept(Token.TK_CALL)) {
                return false;
            }
            if (!parseCall()) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean parseCall() {
        int lastPos = lexer.getCurPos();
        if (accept(Token.TK_KEY_MAP)) {
            return parseMapCall(lastPos);
        }
        if (accept(Token.TK_KEY_FILTER)) {
            return parseFilterCall(lastPos);
        }
        return false;
    }

    public boolean parseFilterCall(int lastPos) {
        ExpressionType expressionType = parseExpression();
        if (expressionType != ExpressionType.BOOLEAN) {
            if (expressionType != ExpressionType.INCORRECT) {
                errorMessage = "TYPE ERROR";
            }
            return false;
        }
        if (!curExpression.isEmpty()) {
            curExpression = "(" + curExpression + "&" +
                    lexer.getInputSubstr(lastPos).replace("element", curElement) + ")";
        } else {
            curExpression = lexer.getInputSubstr(lastPos).replace("element", curElement);
        }

        return accept(Token.TK_RBRACKET);
    }

    public boolean parseMapCall(int lastPos) {
        ExpressionType expressionType = parseExpression();
        if (expressionType != ExpressionType.INTEGER) {
            if (expressionType != ExpressionType.INCORRECT) {
                errorMessage = "TYPE ERROR";
            }
            return false;
        }
        curElement = lexer.getInputSubstr(lastPos).replace("element", curElement);
        return accept(Token.TK_RBRACKET);
    }

    public ExpressionType parseExpression() {
        if (accept(Token.TK_KEY_ELEMENT) || parseConstantExpression() != ExpressionType.INCORRECT) {
            return ExpressionType.INTEGER;
        }

        return parseBinaryExpression();
    }

    public ExpressionType parseConstantExpression() {
        if (accept(Token.TK_MINUS) && accept(Token.INTEGER)) {
            return ExpressionType.INTEGER;
        }
        if (accept(Token.INTEGER)) {
            return ExpressionType.INTEGER;
        }
        return ExpressionType.INCORRECT;
    }

    public ExpressionType parseBinaryExpression() {
        if (!accept(Token.TK_LPAREN)) {
            return ExpressionType.INCORRECT;
        }

        ExpressionType leftExprType = parseExpression();
        if (leftExprType == ExpressionType.INCORRECT) {
            return ExpressionType.INCORRECT;
        }
        ExpressionType operationType = parseOperation();
        if (operationType == ExpressionType.INCORRECT) {
            return ExpressionType.INCORRECT;
        }
        ExpressionType rightExprType = parseExpression();
        if (rightExprType == ExpressionType.INCORRECT) {
            return ExpressionType.INCORRECT;
        }

        ExpressionType expectedType = operationType;
        if (operationType == ExpressionType.COMPARE) {
            expectedType = ExpressionType.INTEGER;
            operationType = ExpressionType.BOOLEAN;
        }

        if (leftExprType != expectedType || rightExprType != expectedType) {
            errorMessage = "TYPE ERROR";
            return ExpressionType.INCORRECT;
        }

        if (!accept(Token.TK_RPAREN)) {
            return ExpressionType.INCORRECT;
        }

        return operationType;
    }

    public ExpressionType parseOperation() {
        if (accept(Token.TK_PLUS) || accept(Token.TK_MINUS) || accept(Token.TK_MUL)) {
            return ExpressionType.INTEGER;
        }

        if (accept(Token.TK_LESS) || accept(Token.TK_GT) || accept(Token.TK_EQ)) {
            return ExpressionType.COMPARE;
        }

        if (accept(Token.TK_AND) || accept(Token.TK_OR)) {
            return ExpressionType.BOOLEAN;
        }

        return ExpressionType.INCORRECT;
    }


    private boolean accept(Token tok) {
        if (curToken == tok) {
            if (!lexer.isExhausted())  {
                curToken = lexer.getNextToken();
            }
            return true;
        }

        return false;
    }

    private enum ExpressionType {
        BOOLEAN,
        INTEGER,
        COMPARE,
        INCORRECT
    }
}
