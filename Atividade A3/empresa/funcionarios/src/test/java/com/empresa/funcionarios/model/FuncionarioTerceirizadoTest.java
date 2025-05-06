package com.empresa.funcionarios.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioTerceirizadoTest {
    // Constante usada como base para os cálculos nos testes
    private static final double SALARIO_MINIMO = 1518.00;
    
    // Teste 1: Verifica a criação válida de um funcionário terceirizado
    @Test
    void quandoCriarFuncTerceirizadoComDadosValidos_entaoNaoLancaExcecao() {
        // Testa se a criação do funcionário terceirizado com parâmetros válidos não lança exceção
        // Parâmetros: nome, horas trabalhadas, valor hora e despesa adicional (dentro do limite)
        assertDoesNotThrow(() -> 
            new FuncionarioTerceirizado("Maria Souza", 30, SALARIO_MINIMO * 0.05, 500.00));
    }
    
    // Teste 2: Verifica validação de despesa adicional inválida
    @Test
    void quandoCriarFuncTerceirizadoComDespesaInvalida_entaoLancaExcecao() {
        // Testa se uma despesa adicional acima do permitido (1500.00) lança exceção
        assertThrows(IllegalArgumentException.class, 
            () -> new FuncionarioTerceirizado("Maria Souza", 30, SALARIO_MINIMO * 0.05, 1500.00));
    }
    
    // Continue com os outros testes conforme mostrado anteriormente
}