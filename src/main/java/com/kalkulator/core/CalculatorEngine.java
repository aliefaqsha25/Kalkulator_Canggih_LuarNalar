package com.kalkulator.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mesin kalkulator: menyimpan state ekspresi dan memproses penekanan tombol.
 * Kelas ini TIDAK bergantung pada tampilan, sehingga dipakai bersama oleh CLI dan GUI.
 *
 * <p>Tombol: 0-9 . + - * / x ^ % ( ) r(√) n(±) d(DEL) c(AC) =
 */
public final class CalculatorEngine {

    private static final Pattern NEGATIVE_AT_END = Pattern.compile("\\(-(\\d+\\.?\\d*)\\)$");
    private static final Pattern NUMBER_AT_END = Pattern.compile("(\\d+\\.?\\d*)$");
    private static final String OPERATORS = "+-×÷^";

    private final StringBuilder expr = new StringBuilder();
    private String result = "";
    private boolean error;
    private boolean justEvaluated;

    // ------------------------------------------------------------ state
    public String getExpression() { return expr.toString(); }

    /** Teks hasil ("= 11"), pesan error, atau string kosong. */
    public String getResult() { return result; }

    public boolean isError() { return error; }

    /** Pratinjau hasil langsung; "0" jika kosong, " " jika ekspresi belum lengkap. */
    public String preview() {
        if (expr.length() == 0) return "0";
        try {
            return ResultFormatter.format(ExpressionParser.evaluate(autoClose(expr.toString())));
        } catch (CalculatorException ex) {
            return " ";
        }
    }

    // ------------------------------------------------------------ input
    public void press(char ch) {
        if (Character.isWhitespace(ch)) return;
        char k = Character.toLowerCase(ch);

        if (k == '=') { evaluate(); return; }

        result = "";
        error = false;

        switch (k) {
            case 'c':
                expr.setLength(0);
                justEvaluated = false;
                return;
            case 'd':
                if (expr.length() > 0) expr.setLength(expr.length() - 1);
                justEvaluated = false;
                return;
            case 'n':
                toggleSign();
                justEvaluated = false;
                return;
            default:
                break;
        }

        boolean startsNew = Character.isDigit(k) || k == '.' || k == '(' || k == 'r' || k == 'v' || k == '√';
        if (justEvaluated && startsNew) expr.setLength(0);
        justEvaluated = false;

        if (Character.isDigit(k)) {
            expr.append(k);
            return;
        }

        switch (k) {
            case '.': addDecimal(); break;
            case '+': case '-': case '−': addOperator(k == '−' ? '-' : k); break;
            case '*': case 'x': case '×': addOperator('×'); break;
            case '/': case '÷': addOperator('÷'); break;
            case '^': addOperator('^'); break;
            case '%': if (endsWithValue()) expr.append('%'); break;
            case '(': expr.append('('); break;
            case ')': if (openParens() > 0 && endsWithValue()) expr.append(')'); break;
            case 'r': case 'v': case '√': expr.append('√'); break;
            default:
                result = "Tombol '" + ch + "' tidak dikenal";
                error = true;
        }
    }

    // ------------------------------------------------------------ helpers
    private char last() {
        return expr.length() == 0 ? '\0' : expr.charAt(expr.length() - 1);
    }

    private static boolean isOperator(char c) {
        return OPERATORS.indexOf(c) >= 0;
    }

    private boolean endsWithValue() {
        char c = last();
        return Character.isDigit(c) || c == ')' || c == '%';
    }

    private int openParens() {
        int n = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') n++;
            else if (c == ')') n--;
        }
        return n;
    }

    private String autoClose(String s) {
        int open = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') open++;
            else if (s.charAt(i) == ')') open--;
        }
        StringBuilder sb = new StringBuilder(s);
        for (; open > 0; open--) sb.append(')');
        return sb.toString();
    }

    private String currentNumber() {
        int i = expr.length();
        while (i > 0 && (Character.isDigit(expr.charAt(i - 1)) || expr.charAt(i - 1) == '.')) i--;
        return expr.substring(i);
    }

    private void addDecimal() {
        String num = currentNumber();
        if (num.contains(".")) return;
        if (num.isEmpty()) expr.append('0');
        expr.append('.');
    }

    private void addOperator(char op) {
        char l = last();
        if (expr.length() == 0) {                    // awal ekspresi: hanya minus
            if (op == '-') expr.append(op);
            return;
        }
        if (l == '(' || l == '√') {                  // setelah '(' atau '√': hanya minus
            if (op == '-') expr.append(op);
            return;
        }
        if (isOperator(l)) {                         // ganti operator terakhir
            if (expr.length() == 1 && op != '-' && op != '+') return;
            expr.setCharAt(expr.length() - 1, op);
            return;
        }
        expr.append(op);
    }

    /** Tombol ±: angka terakhir menjadi negatif "(-5)" atau kembali positif. */
    private void toggleSign() {
        String s = expr.toString();
        Matcher neg = NEGATIVE_AT_END.matcher(s);
        if (neg.find()) {
            expr.replace(neg.start(), s.length(), neg.group(1));
            return;
        }
        Matcher pos = NUMBER_AT_END.matcher(s);
        if (pos.find()) expr.replace(pos.start(), s.length(), "(-" + pos.group(1) + ")");
    }

    private void evaluate() {
        if (expr.length() == 0) return;
        try {
            double v = ExpressionParser.evaluate(autoClose(expr.toString()));
            result = "= " + ResultFormatter.format(v);
            error = false;
            String plain = ResultFormatter.plain(v);
            expr.setLength(0);
            if (plain.length() <= 25) expr.append(v < 0 ? "(" + plain + ")" : plain);
            justEvaluated = true;
        } catch (CalculatorException ex) {
            result = ex.getMessage();
            error = true;
        }
    }
}
