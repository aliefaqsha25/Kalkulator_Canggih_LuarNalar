package com.kalkulator.cli;

import com.kalkulator.core.CalculatorEngine;
import com.kalkulator.ui.Keypad;
import java.util.Collections;

/** Menggambar tampilan kalkulator & menu sebagai teks (kotak simetris). */
public final class CliRenderer {

    private static final int CELL = 7;                 // lebar tiap tombol

    private final int cols = Keypad.cols();
    private final int width = CELL * cols + (cols - 1); // lebar dalam bingkai (31)

    // ------------------------------------------------------------ kalkulator
    public String render(CalculatorEngine engine) {
        StringBuilder sb = new StringBuilder();
        sb.append(Ansi.CLEAR_SCREEN).append("\n");

        sb.append("  ").append(full("╭", "╮")).append("\n");
        sb.append("  │").append(Ansi.CYAN).append(Ansi.BOLD)
          .append(center("KALKULATOR  CLI", width)).append(Ansi.RESET).append("│\n");
        sb.append("  ").append(full("├", "┤")).append("\n");

        // Layar ekspresi
        String e = engine.getExpression();
        int max = width - 2;
        if (e.length() > max) e = "…" + e.substring(e.length() - (max - 1));
        sb.append("  │").append(Ansi.BOLD).append(alignRight(e, width)).append(Ansi.RESET).append("│\n");

        // Layar hasil
        String r = engine.getResult();
        sb.append("  │").append(engine.isError() ? Ansi.RED : Ansi.GREEN).append(Ansi.BOLD)
          .append(alignRight(r, width)).append(Ansi.RESET).append("│\n");

        // Papan tombol
        sb.append("  ").append(hline("├", "┬", "┤")).append("\n");
        int rows = Keypad.rows();
        for (int i = 0; i < rows; i++) {
            StringBuilder lab = new StringBuilder("  │");
            StringBuilder hin = new StringBuilder("  │");
            for (int j = 0; j < cols; j++) {
                String t = Keypad.label(i, j);
                String color = "";
                if (t.equals("AC") || t.equals("DEL")) color = Ansi.RED;
                else if (t.equals("=")) color = Ansi.GREEN;
                else if (j == cols - 1) color = Ansi.YELLOW;
                lab.append(color).append(Ansi.BOLD).append(center(t, CELL)).append(Ansi.RESET).append("│");
                hin.append(Ansi.DIM).append(center(Keypad.hint(i, j), CELL)).append(Ansi.RESET).append("│");
            }
            sb.append(lab).append("\n").append(hin).append("\n");
            sb.append("  ").append(i < rows - 1 ? hline("├", "┼", "┤") : hline("╰", "┴", "╯")).append("\n");
        }

        // Petunjuk
        int fw = width + 2;
        sb.append(Ansi.DIM);
        sb.append("  ").append(center("Ketik tombol lalu tekan Enter", fw)).append("\n");
        sb.append("  ").append(center("Contoh : 2+3*(4-1)=", fw)).append("\n");
        sb.append("  ").append(center("r=√  n=±  c=AC  d=DEL  q=keluar", fw)).append("\n");
        sb.append(Ansi.RESET).append("\n");
        return sb.toString();
    }

    // ------------------------------------------------------------ menu mode
    public String menu() {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("  ").append(full("╭", "╮")).append("\n");
        sb.append("  │").append(Ansi.CYAN).append(Ansi.BOLD)
          .append(center("KALKULATOR", width)).append(Ansi.RESET).append("│\n");
        sb.append("  ").append(full("├", "┤")).append("\n");
        sb.append("  │").append(padRight("   [1]  Mode CLI  (terminal)", width)).append("│\n");
        sb.append("  │").append(padRight("   [2]  Mode GUI  (jendela)", width)).append("│\n");
        sb.append("  │").append(padRight("   [q]  Keluar", width)).append("│\n");
        sb.append("  ").append(full("╰", "╯")).append("\n");
        return sb.toString();
    }

    // ------------------------------------------------------------ util garis & teks
    private String hline(String l, String m, String r) {
        return l + String.join(m, Collections.nCopies(cols, rep("─", CELL))) + r;
    }

    private String full(String l, String r) {
        return l + rep("─", width) + r;
    }

    private static String rep(String s, int n) {
        return s.repeat(Math.max(0, n));
    }

    private static String center(String s, int w) {
        if (s.length() >= w) return s.substring(0, w);
        int left = (w - s.length()) / 2;
        return rep(" ", left) + s + rep(" ", w - s.length() - left);
    }

    private static String padRight(String s, int w) {
        return s + rep(" ", w - s.length());
    }

    private static String alignRight(String s, int w) {
        if (s.length() > w - 2) s = s.substring(0, w - 2);
        return rep(" ", w - 1 - s.length()) + s + " ";
    }
}
