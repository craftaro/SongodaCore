package com.songoda.core.math;

public class Eval {

    private int pos = -1, ch;
    private final String toParse;
    private final String warningMessage;

    public Eval(String toParse, String warningMessage) {
        this.toParse = toParse;
        this.warningMessage = warningMessage + " ";
    }

    private void nextChar() {
        ch = (++pos < toParse.length()) ? toParse.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') nextChar();
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    public double parse() {
        nextChar();
        double x = parseExpression();
        if (pos < toParse.length()) throw new RuntimeException(warningMessage + "Unexpected: " + (char) ch);
        return x;
    }

    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor
    // factor = `+` factor | `-` factor | `(` expression `)`
    //        | number | functionName factor | factor `^` factor

    private double parseExpression() {
        double x = parseTerm();
        for (; ; ) {
            if (eat('+')) x += parseTerm(); // addition
            else if (eat('-')) x -= parseTerm(); // subtraction
            else return x;
        }
    }

    private double parseTerm() {
        double x = parseFactor();
        for (; ; ) {
            if (eat('*')) x *= parseFactor(); // multiplication
            else if (eat('/')) x /= parseFactor(); // division
            else return x;
        }
    }

    private double parseFactor() {
        if (eat('+')) return parseFactor(); // unary plus
        if (eat('-')) return -parseFactor(); // unary minus

        double x;
        int startPos = this.pos;
        if (eat('(')) { // parentheses
            x = parseExpression();
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            x = Double.parseDouble(toParse.substring(startPos, this.pos));
        } else if (ch >= 'a' && ch <= 'z') { // functions
            while (ch >= 'a' && ch <= 'z') nextChar();
            String func = toParse.substring(startPos, this.pos);
            x = parseFactor();
            if (func.equals("sqrt")) x = Math.sqrt(x);
            else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
            else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
            else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
            else throw new RuntimeException(warningMessage + "Unknown function: " + func);
        } else {
            throw new RuntimeException(warningMessage + "Unexpected: " + (char) ch);
        }

        if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

        return x;
    }
}
