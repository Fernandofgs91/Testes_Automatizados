package com.empresa.funcionarios.model;

public class Funcionario {
    private String nome;
    private int horasTrabalhadas;
    private double valorHora;
    
    protected static final double SALARIO_MINIMO = 1518.00;
    protected static final double TETO_SALARIAL = 100000.00;
    protected static final int HORAS_MINIMAS = 20;
    protected static final int HORAS_MAXIMAS = 40;
    protected static final double PERCENTUAL_MINIMO_HORA = 0.04;
    protected static final double PERCENTUAL_MAXIMO_HORA = 0.10;
    
    public Funcionario(String nome, int horasTrabalhadas, double valorHora) {
        this.nome = nome;
        this.horasTrabalhadas = validarHorasTrabalhadas(horasTrabalhadas);
        this.valorHora = validarValorHora(valorHora);
        validarPagamento(calcularPagamento());
    }
    
    // Getters
    public String getNome() { return nome; }
    public int getHorasTrabalhadas() { return horasTrabalhadas; }
    public double getValorHora() { return valorHora; }
    
    // Setters com validação
    public void setHorasTrabalhadas(int horasTrabalhadas) {
        this.horasTrabalhadas = validarHorasTrabalhadas(horasTrabalhadas);
        validarPagamento(calcularPagamento());
    }
    
    public void setValorHora(double valorHora) {
        this.valorHora = validarValorHora(valorHora);
        validarPagamento(calcularPagamento());
    }
    
    // Método de cálculo do pagamento
    public double calcularPagamento() {
        return horasTrabalhadas * valorHora;
    }
    
    // Métodos de validação
    private int validarHorasTrabalhadas(int horas) {
        if (horas < HORAS_MINIMAS || horas > HORAS_MAXIMAS) {
            throw new IllegalArgumentException(
                String.format("Horas trabalhadas devem estar entre %d e %d.", 
                HORAS_MINIMAS, HORAS_MAXIMAS));
        }
        return horas;
    }
    
    private double validarValorHora(double valorHora) {
        double valorMinimo = SALARIO_MINIMO * PERCENTUAL_MINIMO_HORA;
        double valorMaximo = SALARIO_MINIMO * PERCENTUAL_MAXIMO_HORA;
        
        if (valorHora < valorMinimo || valorHora > valorMaximo) {
            throw new IllegalArgumentException(
                String.format("Valor por hora deve estar entre R$ %.2f e R$ %.2f.", 
                valorMinimo, valorMaximo));
        }
        return valorHora;
    }
    
    protected void validarPagamento(double pagamento) {
        if (pagamento < SALARIO_MINIMO) {
            throw new IllegalArgumentException(
                String.format("Pagamento R$ %.2f abaixo do salário mínimo R$ %.2f", 
                pagamento, SALARIO_MINIMO));
        }
        
        if (pagamento > TETO_SALARIAL) {
            throw new IllegalArgumentException(
                String.format("Pagamento R$ %.2f acima do teto salarial R$ %.2f", 
                pagamento, TETO_SALARIAL));
        }
    }
}