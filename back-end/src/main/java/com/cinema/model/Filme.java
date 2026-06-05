package com.cinema.model;

public class Filme {
    private int id;
    private String titulo;
    private int duracao;
    private String classificacaoIndicativa;
    private int generoId;
    private String generoNome; // Para exibição
    
    public Filme() {}
    
    public Filme(int id, String titulo, int duracao, String classificacaoIndicativa, 
                 int generoId, String generoNome) {
        this.id = id;
        this.titulo = titulo;
        this.duracao = duracao;
        this.classificacaoIndicativa = classificacaoIndicativa;
        this.generoId = generoId;
        this.generoNome = generoNome;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public int getDuracao() { return duracao; }
    public void setDuracao(int duracao) { this.duracao = duracao; }
    
    public String getClassificacaoIndicativa() { return classificacaoIndicativa; }
    public void setClassificacaoIndicativa(String classificacaoIndicativa) { 
        this.classificacaoIndicativa = classificacaoIndicativa; 
    }
    
    public int getGeneroId() { return generoId; }
    public void setGeneroId(int generoId) { this.generoId = generoId; }
    
    public String getGeneroNome() { return generoNome; }
    public void setGeneroNome(String generoNome) { this.generoNome = generoNome; }
}