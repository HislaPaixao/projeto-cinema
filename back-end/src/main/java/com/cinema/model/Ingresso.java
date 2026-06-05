package com.cinema.model;

public class Ingresso {
    private int id;
    private String tipoIngresso;
    private double valorPago;
    private String dataVenda;
    private int sessaoId;
    private int assentoId;
    private String assentoNumero;
    
    public Ingresso() {}
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTipoIngresso() { return tipoIngresso; }
    public void setTipoIngresso(String tipoIngresso) { this.tipoIngresso = tipoIngresso; }
    
    public double getValorPago() { return valorPago; }
    public void setValorPago(double valorPago) { this.valorPago = valorPago; }
    
    public String getDataVenda() { return dataVenda; }
    public void setDataVenda(String dataVenda) { this.dataVenda = dataVenda; }
    
    public int getSessaoId() { return sessaoId; }
    public void setSessaoId(int sessaoId) { this.sessaoId = sessaoId; }
    
    public int getAssentoId() { return assentoId; }
    public void setAssentoId(int assentoId) { this.assentoId = assentoId; }
    
    public String getAssentoNumero() { return assentoNumero; }
    public void setAssentoNumero(String assentoNumero) { this.assentoNumero = assentoNumero; }
}