package com.cinema.model;

public class Sessao {
    private int id;
    private String dataHora;
    private double precoBase;
    private int filmeId;
    private String filmeTitulo;
    private int salaId;
    private String salaNome;
    private String salaTipo;
    private String classificacao;
    
    public Sessao() {}
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }
    
    public double getPrecoBase() { return precoBase; }
    public void setPrecoBase(double precoBase) { this.precoBase = precoBase; }
    
    public int getFilmeId() { return filmeId; }
    public void setFilmeId(int filmeId) { this.filmeId = filmeId; }
    
    public String getFilmeTitulo() { return filmeTitulo; }
    public void setFilmeTitulo(String filmeTitulo) { this.filmeTitulo = filmeTitulo; }
    
    public int getSalaId() { return salaId; }
    public void setSalaId(int salaId) { this.salaId = salaId; }
    
    public String getSalaNome() { return salaNome; }
    public void setSalaNome(String salaNome) { this.salaNome = salaNome; }
    
    public String getSalaTipo() { return salaTipo; }
    public void setSalaTipo(String salaTipo) { this.salaTipo = salaTipo; }
    
    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }
}