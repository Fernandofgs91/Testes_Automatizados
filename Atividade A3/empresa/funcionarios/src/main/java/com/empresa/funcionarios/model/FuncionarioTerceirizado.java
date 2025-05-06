package com.empresa.funcionarios.model;

public class FuncionarioTerceirizado extends Funcionario {
    private double despesaAdicional;
    private static final double LIMITE_DESPESA = 1000.00;
    private static final double BONUS_DESPESA = 1.1; // 110%
    
    public FuncionarioTerceirizado(String nome, int horasTrabalhadas, 
                                 double valorHora, double despesaAdicional) {
        super(nome, horasTrabalhadas, valorHora);
        this.despesaAdicional = validarDespesaAdicional(despesaAdicional);
        validarPagamento(calcularPagamento());
    }
    
    // Getter e Setter para despesa adicional
    public double getDespesaAdicional() { return despesaAdicional; }
    
    public void setDespesaAdicional(double despesaAdicional) {
        this.despesaAdicional = validarDespesaAdicional(despesaAdicional);
        validarPagamento(calcularPagamento());
    }
    
    // Sobrescrita do método de cálculo de pagamento
    @Override
    public double calcularPagamento() {
        return super.calcularPagamento() + (despesaAdicional * BONUS_DESPESA);
    }
    
    // Método de validação da despesa adicional
    private double validarDespesaAdicional(double despesa) {
        if (despesa < 0 || despesa > LIMITE_DESPESA) {
            throw new IllegalArgumentException(
                String.format("Despesa adicional deve estar entre R$ 0.00 e R$ %.2f", 
                LIMITE_DESPESA));
        }
        return despesa;
    }
}