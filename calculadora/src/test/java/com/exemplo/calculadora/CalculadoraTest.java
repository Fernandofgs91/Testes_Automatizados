package com.exemplo.calculadora;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraTest {

    @Test
    public void testConstrutorSemParametro() {
        Calculadora calc = new Calculadora();
        assertEquals(0, calc.getMemoria());
    }

    @Test
    public void testConstrutorComValor3() {
        Calculadora calc = new Calculadora(3);
        assertEquals(3, calc.getMemoria());
    }

    @Test
    public void testConstrutorComValorNegativo() {
        Calculadora calc = new Calculadora(-3);
        assertEquals(-3, calc.getMemoria());
    }

    @Test
    public void testSomarPositivo() {
        Calculadora calc = new Calculadora(3);
        calc.somar(2);
        assertEquals(5, calc.getMemoria());
    }

    @Test
    public void testSomarNegativo() {
        Calculadora calc = new Calculadora(3);
        calc.somar(-2);
        assertEquals(1, calc.getMemoria());
    }

    @Test
    public void testSubtrairPositivo() {
        Calculadora calc = new Calculadora(3);
        calc.subtrair(2);
        assertEquals(1, calc.getMemoria());
    }

    @Test
    public void testSubtrairNegativo() {
        Calculadora calc = new Calculadora(3);
        calc.subtrair(-2);
        assertEquals(5, calc.getMemoria());
    }

    @Test
    public void testMultiplicarPositivo() {
        Calculadora calc = new Calculadora(3);
        calc.multiplicar(2);
        assertEquals(6, calc.getMemoria());
    }

    @Test
    public void testMultiplicarNegativo() {
        Calculadora calc = new Calculadora(3);
        calc.multiplicar(-2);
        assertEquals(-6, calc.getMemoria());
    }

    @Test
    public void testDividirPorZero() {
        Calculadora calc = new Calculadora(3);
        Exception exception = assertThrows(Exception.class, () -> {
            calc.dividir(0);
        });
        assertEquals("Divisão por zero!!!", exception.getMessage());
    }

    @Test
    public void testDividirPositivo() throws Exception {
        Calculadora calc = new Calculadora(6);
        calc.dividir(2);
        assertEquals(3, calc.getMemoria());
    }

    @Test
    public void testDividirNegativo() throws Exception {
        Calculadora calc = new Calculadora(6);
        calc.dividir(-2);
        assertEquals(-3, calc.getMemoria());
    }

    @Test
    public void testExponenciar1() throws Exception {
        Calculadora calc = new Calculadora(3);
        calc.exponenciar(1);
        assertEquals(3, calc.getMemoria());
    }

    @Test
    public void testExponenciar10() throws Exception {
        Calculadora calc = new Calculadora(2);
        calc.exponenciar(10);
        assertEquals(1024, calc.getMemoria());
    }

    @Test
    public void testExponenciarErro() {
        Calculadora calc = new Calculadora(2);
        Exception exception = assertThrows(Exception.class, () -> {
            calc.exponenciar(20);
        });
        assertEquals("Expoente incorreto, valor máximo é 10.", exception.getMessage());
    }

    @Test
    public void testZerarMemoria() {
        Calculadora calc = new Calculadora(10);
        calc.zerarMemoria();
        assertEquals(0, calc.getMemoria());
    }
}
