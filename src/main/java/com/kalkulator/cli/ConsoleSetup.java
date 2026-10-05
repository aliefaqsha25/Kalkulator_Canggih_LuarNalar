package com.kalkulator.cli;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/** Menyiapkan konsol agar karakter kotak (╭ ─ │) dan simbol (√ ÷ ×) tampil benar. */
public final class ConsoleSetup {

    private ConsoleSetup() { }

    /** Di Windows, code page konsol diubah ke UTF-8; mengembalikan PrintStream UTF-8. */
    public static PrintStream utf8Out() throws UnsupportedEncodingException {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "chcp 65001 >nul").inheritIO().start().waitFor();
            } catch (Exception ignored) {
                // abaikan: tampilan tetap berjalan, hanya mungkin kurang rapi
            }
        }
        return new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8");
    }
}
