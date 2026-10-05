package com.kalkulator.core;

/** Kesalahan perhitungan yang pesannya siap ditampilkan ke pengguna. */
public class CalculatorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CalculatorException(String message) {
        super(message);
    }
}
