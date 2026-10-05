package com.kalkulator.ui;

/** Tata letak papan tombol yang dipakai bersama oleh CLI dan GUI. */
public final class Keypad {

    private static final String[][] LABELS = {
        {"AC", "DEL", "%", "÷"},
        {"(",  ")",   "√", "^"},
        {"7",  "8",   "9", "×"},
        {"4",  "5",   "6", "-"},
        {"1",  "2",   "3", "+"},
        {"±",  "0",   ".", "="}
    };

    /** Tombol keyboard yang setara (ditampilkan sebagai petunjuk di CLI). */
    private static final String[][] HINTS = {
        {"c", "d", "%", "/"},
        {"(", ")", "r", "^"},
        {"7", "8", "9", "*"},
        {"4", "5", "6", "-"},
        {"1", "2", "3", "+"},
        {"n", "0", ".", "="}
    };

    /** Karakter yang dikirim ke CalculatorEngine.press(). */
    private static final char[][] KEYS = {
        {'c', 'd', '%', '/'},
        {'(', ')', 'r', '^'},
        {'7', '8', '9', '*'},
        {'4', '5', '6', '-'},
        {'1', '2', '3', '+'},
        {'n', '0', '.', '='}
    };

    private Keypad() { }

    public static int rows() { return LABELS.length; }
    public static int cols() { return LABELS[0].length; }
    public static String label(int row, int col) { return LABELS[row][col]; }
    public static String hint(int row, int col) { return HINTS[row][col]; }
    public static char key(int row, int col) { return KEYS[row][col]; }
}
