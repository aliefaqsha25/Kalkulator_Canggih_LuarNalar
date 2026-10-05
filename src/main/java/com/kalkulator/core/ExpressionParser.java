package com.kalkulator.core;

/**
 * Parser ekspresi matematika (recursive descent).
 *
 * <pre>
 * expression := term   { ('+' | '-') term }
 * term       := unary  { ('×' | '÷' | '*' | '/' | implisit) unary }
 * unary      := ('-' | '+') unary | power
 * power      := postfix [ '^' unary ]          (right-associative)
 * postfix    := primary { '%' }                (x% = x / 100)
 * primary    := angka | '(' expression ')' | '√' primary
 * </pre>
 */
public final class ExpressionParser {

    private final String s;
    private int p;

    private ExpressionParser(String s) {
        this.s = s;
    }

    /** Menghitung ekspresi. @throws CalculatorException jika ekspresi tidak valid. */
    public static double evaluate(String expression) {
        double result = new ExpressionParser(expression).parse();
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new CalculatorException("Error: tak terdefinisi");
        }
        return result;
    }

    private char peek() {
        return p < s.length() ? s.charAt(p) : '\0';
    }

    private double parse() {
        double v = expression();
        if (p < s.length()) throw new CalculatorException("Sintaks tidak valid");
        return v;
    }

    private double expression() {
        double v = term();
        while (true) {
            char c = peek();
            if (c == '+') { p++; v += term(); }
            else if (c == '-') { p++; v -= term(); }
            else return v;
        }
    }

    private double term() {
        double v = unary();
        while (true) {
            char c = peek();
            if (c == '×' || c == '*') {
                p++;
                v *= unary();
            } else if (c == '÷' || c == '/') {
                p++;
                double d = unary();
                if (d == 0) throw new CalculatorException("Error: dibagi nol");
                v /= d;
            } else if (Character.isDigit(c) || c == '.' || c == '(' || c == '√') {
                v *= power();                       // perkalian implisit: 2(3), 5√4
            } else {
                return v;
            }
        }
    }

    private double unary() {
        char c = peek();
        if (c == '-') { p++; return -unary(); }
        if (c == '+') { p++; return unary(); }
        return power();
    }

    private double power() {
        double base = postfix();
        if (peek() == '^') {
            p++;
            double exp = unary();
            double r = Math.pow(base, exp);
            if (Double.isNaN(r) || Double.isInfinite(r)) {
                throw new CalculatorException("Error: tak terdefinisi");
            }
            return r;
        }
        return base;
    }

    private double postfix() {
        double v = primary();
        while (peek() == '%') { p++; v /= 100.0; }
        return v;
    }

    private double primary() {
        char c = peek();
        if (Character.isDigit(c) || c == '.') {
            int start = p;
            while (Character.isDigit(peek()) || peek() == '.') p++;
            try {
                return Double.parseDouble(s.substring(start, p));
            } catch (NumberFormatException e) {
                throw new CalculatorException("Angka tidak valid");
            }
        }
        if (c == '(') {
            p++;
            double v = expression();
            if (peek() != ')') throw new CalculatorException("Kurung tidak seimbang");
            p++;
            return v;
        }
        if (c == '√') {
            p++;
            double v = primary();
            if (v < 0) throw new CalculatorException("Error: akar bilangan negatif");
            return Math.sqrt(v);
        }
        throw new CalculatorException("Sintaks tidak valid");
    }
}
