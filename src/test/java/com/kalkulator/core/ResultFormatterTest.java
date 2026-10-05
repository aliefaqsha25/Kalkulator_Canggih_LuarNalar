package com.kalkulator.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ResultFormatterTest {

    @Test
    void membuangNolDiBelakang() {
        assertEquals("13", ResultFormatter.format(13.0));
        assertEquals("100", ResultFormatter.format(100.0));
        assertEquals("-2.5", ResultFormatter.format(-2.5));
    }

    @Test
    void membulatkanKesalahanFloatingPoint() {
        assertEquals("0.3", ResultFormatter.format(0.1 + 0.2));
        assertEquals("0.333333333333", ResultFormatter.format(1.0 / 3));
    }

    @Test
    void nolDanAngkaBesar() {
        assertEquals("0", ResultFormatter.format(0));
        assertEquals("1E+20", ResultFormatter.format(1e20));
    }
}
