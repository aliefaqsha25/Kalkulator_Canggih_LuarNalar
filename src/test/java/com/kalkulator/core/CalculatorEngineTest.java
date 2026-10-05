package com.kalkulator.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorEngineTest {

    private CalculatorEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CalculatorEngine();
    }

    private void type(String keys) {
        for (char c : keys.toCharArray()) engine.press(c);
    }

    @Test
    void hitungDasar() {
        type("2+3*(4-1)=");
        assertEquals("= 11", engine.getResult());
        assertEquals("11", engine.getExpression());
    }

    @Test
    void kesalahanFloatingPointDibulatkan() {
        type("0.1+0.2=");
        assertEquals("= 0.3", engine.getResult());
    }

    @Test
    void akarDenganTombolR() {
        type("r16=");
        assertEquals("= 4", engine.getResult());
    }

    @Test
    void pangkat() {
        type("2^10=");
        assertEquals("= 1024", engine.getResult());
    }

    @Test
    void deleteDanClearAll() {
        type("123dd");
        assertEquals("1", engine.getExpression());
        type("c");
        assertEquals("", engine.getExpression());
    }

    @Test
    void tombolPlusMinusMengubahTanda() {
        type("5n");
        assertEquals("(-5)", engine.getExpression());
        type("n");
        assertEquals("5", engine.getExpression());
    }

    @Test
    void angkaNegatifDalamPerhitungan() {
        type("5n+3=");
        assertEquals("= -2", engine.getResult());
        assertEquals("(-2)", engine.getExpression());
    }

    @Test
    void desimalTidakBolehGanda() {
        type("1.2.3");
        assertEquals("1.23", engine.getExpression());
    }

    @Test
    void desimalDiAwalMenjadiNolTitik() {
        type(".5");
        assertEquals("0.5", engine.getExpression());
    }

    @Test
    void operatorGandaDigantiYangTerakhir() {
        type("5+*3");
        assertEquals("5×3", engine.getExpression());
    }

    @Test
    void kurungTutupTanpaPembukaDiabaikan() {
        type("5)");
        assertEquals("5", engine.getExpression());
    }

    @Test
    void kurungOtomatisDitutupSaatSamaDengan() {
        type("(2+3=");
        assertEquals("= 5", engine.getResult());
    }

    @Test
    void lanjutDariHasilDenganOperator() {
        type("2+3=*4=");
        assertEquals("= 20", engine.getResult());
    }

    @Test
    void angkaSetelahSamaDenganMemulaiBaru() {
        type("2+3=7");
        assertEquals("7", engine.getExpression());
    }

    @Test
    void errorDibagiNolTidakMenghentikanProgram() {
        type("8/0=");
        assertTrue(engine.isError());
        assertEquals("Error: dibagi nol", engine.getResult());
        type("c");
        assertFalse(engine.isError());
    }

    @Test
    void pratinjau() {
        assertEquals("0", engine.preview());
        type("2+3");
        assertEquals("5", engine.preview());
        type("+");
        assertEquals(" ", engine.preview());
    }

    @Test
    void tombolTidakDikenal() {
        type("z");
        assertTrue(engine.isError());
    }
}
