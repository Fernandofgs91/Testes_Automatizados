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

    
    // Teste de Despesa Adicional Zero
    @Test
    void quandoDespesaAdicionalZero_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new FuncionarioTerceirizado("Maria Souza", 30, SALARIO_MINIMO * 0.05, 0.00);
        });
    }

    // Teste de Despesa Adicional no Limite Máximo
    @Test
    void quandoDespesaAdicionalNoLimite_entaoNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            new FuncionarioTerceirizado("Maria Souza", 30, SALARIO_MINIMO * 0.05, 1000.00);
        });
    }

    // Teste de Despesa Adicional Negativa
    @Test
    void quandoDespesaAdicionalNegativa_entaoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FuncionarioTerceirizado("Maria Souza", 30, SALARIO_MINIMO * 0.05, -100.00);
        });
    }

    // Teste de Pagamento com Despesa Adicional
    @Test
    void quandoCalcularPagamentoComDespesaAdicional_entaoRetornaValorCorreto() {
        FuncionarioTerceirizado func = new FuncionarioTerceirizado("Maria Souza", 30, SALARIO_MINIMO * 0.05, 500.00);
        double pagamentoEsperado = (30 * (SALARIO_MINIMO * 0.05)) + (500.00 * 1.1); // 10% a mais na despesa
        assertEquals(pagamentoEsperado, func.calcularPagamento(), 0.01);
    }

    // Teste de Mensagem de Exceção para Despesa Inválida
    @Test
    void quandoDespesaInvalida_entaoMensagemExcecaoCorreta() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new FuncionarioTerceirizado("Maria", 30, SALARIO_MINIMO * 0.05, 1001.00);
        });

        assertTrue(exception.getMessage().contains("Despesa adicional deve estar entre R$ 0.00 e R$ 1000.00"));
    }

    // Teste de Herança (Verificar se é Instância de Funcionario)
    @Test
    void quandoCriarFuncTerceirizado_entaoEhInstanciaDeFuncionario() {
        FuncionarioTerceirizado func = new FuncionarioTerceirizado(
                "Maria Souza", 30, SALARIO_MINIMO * 0.05, 500.00);
        assertTrue(func instanceof Funcionario);
    }

    // Teste de Sobrescrita do Método calcularPagamento(),
    @Test
    void quandoCalcularPagamento_entaoUsaImplementacaoEspecifica() {
        FuncionarioTerceirizado funcTerc = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 500.00);
        Funcionario func = new Funcionario("João", 30, SALARIO_MINIMO * 0.05);

        assertNotEquals(func.calcularPagamento(), funcTerc.calcularPagamento());
    }

    // Teste de Pagamento com Despesa Adicional no Limite
    @Test
    void quandoDespesaNoLimite_entaoCalculoPagamentoCorreto() {
        FuncionarioTerceirizado func = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 1000.00);

        double pagamentoEsperado = (30 * SALARIO_MINIMO * 0.05) + (1000.00 * 1.1);
        assertEquals(pagamentoEsperado, func.calcularPagamento());
    }
    // Teste de Atualização de Despesa Adicional

    @Test
    void quandoAtualizarDespesa_entaoRecalculaPagamento() {
        FuncionarioTerceirizado func = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 500.00);

        double pagamentoOriginal = func.calcularPagamento();
        func.setDespesaAdicional(600.00);

        assertNotEquals(pagamentoOriginal, func.calcularPagamento());

    }

    // Teste de Consistência Após Modificações
    @Test
    void quandoModificarVariosAtributos_entaoMantemConsistencia() {
        FuncionarioTerceirizado func = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 500.00);

        func.setHorasTrabalhadas(35);
        func.setValorHora(SALARIO_MINIMO * 0.06);
        func.setDespesaAdicional(600.00);

        double pagamentoEsperado = (35 * SALARIO_MINIMO * 0.06) + (600.00 * 1.1);
        assertEquals(pagamentoEsperado, func.calcularPagamento());
    }

    // Teste 1: Verificação do bônus de 110% na despesa adicional
    @Test
    void quandoCalcularPagamento_entaoIncluiBonusCorretoNaDespesa() {
        FuncionarioTerceirizado func = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 200.00);
        double pagamentoBase = 30 * SALARIO_MINIMO * 0.05;
        double bonusEsperado = 200.00 * 1.1;
        assertEquals(pagamentoBase + bonusEsperado, func.calcularPagamento(), 0.001);
    }

    // Teste 2: Verificação do limite inferior do pagamento com despesa
    @Test
    void quandoPagamentoAposBonusIgualSalarioMinimo_entaoNaoLancaExcecao() {
        double valorHora = (SALARIO_MINIMO - 550.00) / 30; // 550 = 500 * 1.1
        assertDoesNotThrow(() -> {
            new FuncionarioTerceirizado("Maria", 30, valorHora, 500.00);
        });
    }

    // Teste 3: Verificação do limite superior do pagamento com despesa
    @Test
    void quandoPagamentoAposBonusIgualTetoSalarial_entaoNaoLancaExcecao() {
        double valorHora = (TETO_SALARIAL - 1100.00) / 40; // 1100 = 1000 * 1.1
        assertDoesNotThrow(() -> {
            new FuncionarioTerceirizado("Maria", 40, valorHora, 1000.00);
        });
    }

    // Teste 4: Verificação de despesa adicional zero não afeta pagamento base
    @Test
    void quandoDespesaAdicionalZero_entaoPagamentoIgualFuncionarioNormal() {
        FuncionarioTerceirizado funcTerc = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 0.00);
        Funcionario func = new Funcionario("João", 30, SALARIO_MINIMO * 0.05);

        assertEquals(func.calcularPagamento(), funcTerc.calcularPagamento(), 0.001);
    }

    // Teste 5: Verificação de alteração de despesa adicional para zero
    @Test
    void quandoAlterarDespesaParaZero_entaoPagamentoIgualFuncionarioNormal() {
        FuncionarioTerceirizado funcTerc = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 500.00);
        Funcionario func = new Funcionario("João", 30, SALARIO_MINIMO * 0.05);

        funcTerc.setDespesaAdicional(0.00);
        assertEquals(func.calcularPagamento(), funcTerc.calcularPagamento(), 0.001);
    }

    // Teste 6: Verificação de mensagem de erro para despesa negativa
    @Test
    void quandoDespesaNegativa_entaoMensagemExcecaoContemValorLimite() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new FuncionarioTerceirizado("Maria", 30, SALARIO_MINIMO * 0.05, -1.00);
        });

        assertTrue(exception.getMessage().contains("0.00"));
        assertTrue(exception.getMessage().contains("1000.00"));
    }

    // Teste 7: Verificação de que despesa adicional não afeta horas trabalhadas
    @Test
    void quandoModificarDespesa_entaoHorasTrabalhadasPermanecemIguais() {
        FuncionarioTerceirizado func = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 500.00);
        int horasIniciais = func.getHorasTrabalhadas();
        func.setDespesaAdicional(600.00);
        assertEquals(horasIniciais, func.getHorasTrabalhadas());
    }

    // Teste 8: Verificação de que despesa adicional não afeta valor hora
    @Test
    void quandoModificarDespesa_entaoValorHoraPermaneceIgual() {
        FuncionarioTerceirizado func = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 500.00);
        double valorHoraInicial = func.getValorHora();
        func.setDespesaAdicional(600.00);
        assertEquals(valorHoraInicial, func.getValorHora(), 0.001);
    }

    // Teste 9: Verificação de consistência após múltiplas alterações
    @Test
    void quandoAlterarMultiplasVezes_entaoPagamentoSempreConsistente() {
        FuncionarioTerceirizado func = new FuncionarioTerceirizado(
                "Maria", 30, SALARIO_MINIMO * 0.05, 500.00);

        // Primeira alteração
        func.setDespesaAdicional(600.00);
        double pagamento1 = (30 * SALARIO_MINIMO * 0.05) + (600.00 * 1.1);
        assertEquals(pagamento1, func.calcularPagamento(), 0.001);

        // Segunda alteração
        func.setHorasTrabalhadas(35);
        double pagamento2 = (35 * SALARIO_MINIMO * 0.05) + (600.00 * 1.1);
        assertEquals(pagamento2, func.calcularPagamento(), 0.001);

        // Terceira alteração
        func.setValorHora(SALARIO_MINIMO * 0.06);
        double pagamento3 = (35 * SALARIO_MINIMO * 0.06) + (600.00 * 1.1);
        assertEquals(pagamento3, func.calcularPagamento(), 0.001);
    }

    // Teste 10: Verificação de que o bônus é sempre 110% da despesa
    @Test
    void quandoDespesaVariar_entaoBonusSempre110Porcento() {
        double[] despesas = { 0.00, 100.00, 500.00, 1000.00 };

        for (double despesa : despesas) {
            FuncionarioTerceirizado func = new FuncionarioTerceirizado(
                    "Maria", 30, SALARIO_MINIMO * 0.05, despesa);
            double bonusCalculado = func.calcularPagamento() - (30 * SALARIO_MINIMO * 0.05);
            assertEquals(despesa * 1.1, bonusCalculado, 0.001);
        }
    }
}
