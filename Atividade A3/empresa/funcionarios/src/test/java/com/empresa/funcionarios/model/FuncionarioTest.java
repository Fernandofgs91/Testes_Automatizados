package com.empresa.funcionarios.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioTest {
    // Constantes usadas nos testes
    private static final double SALARIO_MINIMO = 1518.00;
    private static final double TETO_SALARIAL = 100000.00;
    
    // Teste 1: Verifica a criação válida de um funcionário
    @Test
    void quandoCriarFuncionarioComDadosValidos_entaoNaoLancaExcecao() {
        // Testa se a criação do funcionário com parâmetros válidos não lança exceção
        assertDoesNotThrow(() -> new Funcionario("João Silva", 30, SALARIO_MINIMO * 0.05));
    }
    
    // Teste 2: Verifica validação de horas inválidas
    @Test
    void quandoCriarFuncionarioComHorasInvalidas_entaoLancaExcecao() {
        // Testa se horas acima do limite (45) lançam exceção
        assertThrows(IllegalArgumentException.class, 
            () -> new Funcionario("João Silva", 45, SALARIO_MINIMO * 0.05));
        
        // Testa se horas abaixo do limite (15) lançam exceção
        assertThrows(IllegalArgumentException.class, 
            () -> new Funcionario("João Silva", 15, SALARIO_MINIMO * 0.05));
    }
    
    // Teste 3: Verifica validação de valor hora inválido
    @Test
    void quandoCriarFuncionarioComValorHoraInvalido_entaoLancaExcecao() {
        // Testa se valor hora abaixo do mínimo (3% do salário mínimo) lança exceção
        assertThrows(IllegalArgumentException.class, 
            () -> new Funcionario("João Silva", 30, SALARIO_MINIMO * 0.03));
        
        // Testa se valor hora acima do máximo (11% do salário mínimo) lança exceção
        assertThrows(IllegalArgumentException.class, 
            () -> new Funcionario("João Silva", 30, SALARIO_MINIMO * 0.11));
    }
    
    // Teste 4: Verifica validação de salário acima do teto
    @Test
    void quandoCriarFuncionarioComSalarioAcimaDoTeto_entaoLancaExcecao() {
        // Testa se um salário que ultrapassa o teto salarial lança exceção
        // (calculado como TETO_SALARIAL / 2000 horas)
        assertThrows(IllegalArgumentException.class, 
            () -> new Funcionario("João Silva", 30, TETO_SALARIAL / 2000));
    }
    
    // Continue com os outros testes conforme mostrado anteriormente
}