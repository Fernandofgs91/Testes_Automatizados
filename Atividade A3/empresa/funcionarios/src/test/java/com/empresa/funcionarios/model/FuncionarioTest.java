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
     // 1.1 Teste de Nome Vazio ou Nulo
    @Test
    void quandoCriarFuncionarioComNomeVazio_entaoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("", 30, SALARIO_MINIMO * 0.05);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario(null, 30, SALARIO_MINIMO * 0.05);
        });
    }

    // Teste de Valor Mínimo de Horas Trabalhadas
    @Test
    void quandoCriarFuncionarioComValorMinimoDeHoras_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new Funcionario("João Silva", 20, SALARIO_MINIMO * 0.05);
        });
    }

    // Teste de Valor Máximo de Horas Trabalhadas
    @Test
    void quandoCriarFuncionarioComValorMaximoDeHoras_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new Funcionario("João Silva", 40, SALARIO_MINIMO * 0.05);
        });
    }

    // Teste de Valor Mínimo de ValorHora (4% do salário mínimo)
    @Test
    void quandoCriarFuncionarioComValorHoraMinimo_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new Funcionario("João Silva", 30, SALARIO_MINIMO * 0.04);
        });
    }

    // Teste de Valor Máximo de ValorHora (10% do salário mínimo)
    @Test
    void quandoCriarFuncionarioComValorHoraMaximo_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new Funcionario("João Silva", 30, SALARIO_MINIMO * 0.10);
        });
    }

    // Teste de Pagamento Abaixo do Salário Mínimo
    @Test
    void quandoCriarFuncionarioComPagamentoAbaixoDoMinimo_entaoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("João Silva", 20, SALARIO_MINIMO * 0.01);
        });
    }

    // Teste de Pagamento Exatamente no Salário Mínimo
    @Test
    void quandoCriarFuncionarioComPagamentoExatamenteNoMinimo_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new Funcionario("João Silva", 20, SALARIO_MINIMO * 0.0759);
        });
    }

    // Teste de Pagamento Acima do Salário Mínimo
    @Test
    void quandoCriarFuncionarioComPagamentoAcimaDoMinimo_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new Funcionario("João Silva", 20, SALARIO_MINIMO * 0.08);
        });
    }
    // Teste de Pagamento Abaixo do Teto Salarial

    @Test

    void quandoCriarFuncionarioComPagamentoAbaixoDoTeto_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new Funcionario("João Silva", 30, TETO_SALARIAL / 2000);
        });
    }

    // Teste de Pagamento Exatamente no Teto Salarial

    @Test
    void quandoCriarFuncionarioComPagamentoExatamenteNoTeto_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new Funcionario("João Silva", 30, TETO_SALARIAL / 2000);
        });
    }

    // Teste de Pagamento Acima do Teto Salarial
    @Test
    void quandoCriarFuncionarioComPagamentoAcimaDoTeto_entaoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("João Silva", 30, TETO_SALARIAL / 1000);
        });
    }

    // Teste de Pagamento com Valor Hora Válido e Horas Trabalhadas Válidas
    @Test

    void quandoCriarFuncionarioComValorHoraEHorasValidas_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new Funcionario("João Silva", 30, SALARIO_MINIMO * 0.05);
        });
    }

    // Teste de Atualização de Nome
    @Test
    void quandoAtualizarNome_entaoNomeMudaCorretamente() {
        Funcionario func = new Funcionario("João Silva", 30, SALARIO_MINIMO * 0.05);
        func.setNome("João da Silva");
        assertEquals("João da Silva", func.getNome());
    }
    // Teste de Cálculo de Pagamento com Valores Extremos Válidos

    @Test
    void quandoCalcularPagamentoComValoresLimitesValidos_entaoRetornaCorreto() {
        Funcionario func1 = new Funcionario("Min", 20, SALARIO_MINIMO * 0.04);
        assertEquals(20 * SALARIO_MINIMO * 0.04, func1.calcularPagamento());

        Funcionario func2 = new Funcionario("Max", 40, SALARIO_MINIMO * 0.10);
        assertEquals(40 * SALARIO_MINIMO * 0.10, func2.calcularPagamento());
    }
    
}
