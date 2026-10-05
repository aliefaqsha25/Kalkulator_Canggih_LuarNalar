package com.kalkulator.cli;

import com.kalkulator.core.CalculatorEngine;
import java.io.PrintStream;
import java.util.Scanner;

/** Aplikasi kalkulator berbasis terminal (CLI). */
public final class CliApp {

    private final CalculatorEngine engine = new CalculatorEngine();
    private final CliRenderer renderer = new CliRenderer();
    private final Scanner in;
    private final PrintStream out;

    public CliApp(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    /** Menu pilih mode. @return "cli", "gui", atau "" jika pengguna keluar. */
    public static String chooseMode(Scanner in, PrintStream out) {
        CliRenderer renderer = new CliRenderer();
        while (true) {
            out.print(renderer.menu());
            out.print("  " + Ansi.CYAN + "Pilih ▸ " + Ansi.RESET);
            out.flush();
            if (!in.hasNextLine()) return "";
            String s = in.nextLine().trim().toLowerCase();
            if (s.equals("1") || s.equals("cli")) return "cli";
            if (s.equals("2") || s.equals("gui")) return "gui";
            if (s.equals("q")) return "";
        }
    }

    public void run() {
        while (true) {
            out.print(renderer.render(engine));
            out.print("  " + Ansi.CYAN + "▸ " + Ansi.RESET);
            out.flush();
            if (!in.hasNextLine()) break;

            boolean quit = false;
            for (char ch : in.nextLine().toCharArray()) {
                if (Character.toLowerCase(ch) == 'q') { quit = true; break; }
                engine.press(ch);
            }
            if (quit) break;
        }
        out.println("\n  Terima kasih telah menggunakan Kalkulator CLI!\n");
    }
}
