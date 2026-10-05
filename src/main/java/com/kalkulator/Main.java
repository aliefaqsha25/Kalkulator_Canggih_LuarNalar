package com.kalkulator;

import com.kalkulator.cli.CliApp;
import com.kalkulator.cli.ConsoleSetup;
import com.kalkulator.gui.GuiApp;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Titik masuk aplikasi.
 *
 * <pre>
 * java -cp out com.kalkulator.Main        -> menu pilih mode
 * java -cp out com.kalkulator.Main cli    -> langsung mode CLI
 * java -cp out com.kalkulator.Main gui    -> langsung mode GUI
 * </pre>
 */
public final class Main {

    private Main() { }

    public static void main(String[] args) throws Exception {
        String mode = args.length > 0 ? args[0].toLowerCase() : "";

        if (mode.equals("-h") || mode.equals("--help") || mode.equals("help")) {
            System.out.println("Pemakaian: java -cp out com.kalkulator.Main [cli|gui]");
            return;
        }
        if (mode.equals("gui")) {
            GuiApp.launch();
            return;
        }

        PrintStream out = ConsoleSetup.utf8Out();
        Scanner in = new Scanner(System.in, "UTF-8");

        if (!mode.equals("cli")) {
            mode = CliApp.chooseMode(in, out);
            if (mode.isEmpty()) {
                out.println("\n  Terima kasih telah menggunakan Kalkulator!\n");
                return;
            }
            if (mode.equals("gui")) {
                GuiApp.launch();
                return;
            }
        }
        new CliApp(in, out).run();
    }
}
