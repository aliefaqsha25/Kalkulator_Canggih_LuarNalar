package com.kalkulator.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ExpressionParserTest {

    private static final double EPS = 1e-9;

    @Test
    void operasiDasar() {
        assertEquals(11, ExpressionParser.evaluate("2+3×(4-1)"), EPS);
        assertEquals(11, ExpressionParser.evaluate("2+3*(4-1)"), EPS);
        assertEquals(2.5, ExpressionParser.evaluate("10÷4"), EPS);
        assertEquals(-1, ExpressionParser.evaluate("2-3"), EPS);
    }

    @Test
    void pangkatBersifatRightAssociative() {
        assertEquals(512, ExpressionParser.evaluate("2^3^2"), EPS);
    }

    @Test
    void minusUnerDanPangkat() {
        assertEquals(-4, ExpressionParser.evaluate("-2^2"), EPS);
        assertEquals(0.125, ExpressionParser.evaluate("2^-3"), EPS);
    }

    @Test
    void akar() {
        assertEquals(4, ExpressionParser.evaluate("√16"), EPS);
        assertEquals(5, ExpressionParser.evaluate("√(9+16)"), EPS);
    }

    @Test
    void persen() {
        assertEquals(0.5, ExpressionParser.evaluate("50%"), EPS);
        assertEquals(50.1, ExpressionParser.evaluate("50+10%"), EPS);
    }

    @Test
    void perkalianImplisit() {
        assertEquals(6, ExpressionParser.evaluate("2(3)"), EPS);
    }

    @Test
    void desimal() {
        assertEquals(3.75, ExpressionParser.evaluate("1.5×2.5"), EPS);
    }

    @Test
    void errorDibagiNol() {
        CalculatorException ex = assertThrows(CalculatorException.class, () -> ExpressionParser.evaluate("5÷0"));
        assertEquals("Error: dibagi nol", ex.getMessage());
    }

    @Test
    void errorAkarNegatif() {
        assertThrows(CalculatorException.class, () -> ExpressionParser.evaluate("√(0-4)"));
    }

    @Test
    void errorSintaks() {
        assertThrows(CalculatorException.class, () -> ExpressionParser.evaluate("3+"));
        assertThrows(CalculatorException.class, () -> ExpressionParser.evaluate("(2+3"));
        assertThrows(CalculatorException.class, () -> ExpressionParser.evaluate("()"));
    }
}
