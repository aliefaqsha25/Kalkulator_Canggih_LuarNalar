package com.kalkulator.core;

import java.math.BigDecimal;
import java.math.MathContext;

/** Memformat angka hasil: dibulatkan 12 digit signifikan, tanpa nol di belakang. */
public final class ResultFormatter {

    private static final MathContext MC = new MathContext(12);

    private ResultFormatter() { }

    private static BigDecimal rounded(double v) {
        return new BigDecimal(v).round(MC).stripTrailingZeros();
    }

    /** Format polos tanpa notasi ilmiah (aman dipakai kembali sebagai ekspresi). */
    public static String plain(double v) {
        return v == 0 ? "0" : rounded(v).toPlainString();
    }

    /** Format tampilan; angka sangat besar/kecil memakai notasi ilmiah. */
    public static String format(double v) {
        if (v == 0) return "0";
        double a = Math.abs(v);
        BigDecimal bd = rounded(v);
        return (a >= 1e15 || a < 1e-9) ? bd.toString() : bd.toPlainString();
    }
}
