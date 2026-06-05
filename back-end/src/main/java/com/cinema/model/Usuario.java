package com.cinema.model;

public class Usuario {
    private int id;
    private String nomeCompleto;
    private String nomeUsuario;
    private String senhaHash;
    private String perfil; // "ADMIN" ou "OPERADOR"
    private boolean ativo;
    
    // Construtores
    public Usuario() {}
    
    public Usuario(int id, String nomeCompleto, String nomeUsuario, 
                   String senhaHash, String perfil, boolean ativo) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.ativo = ativo;
    }
    
    // Getters e Setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getNomeCompleto() { 
        return nomeCompleto; 
    }
    
    public void setNomeCompleto(String nomeCompleto) { 
        this.nomeCompleto = nomeCompleto; 
    }
    
    public String getNomeUsuario() { 
        return nomeUsuario; 
    }
    
    public void setNomeUsuario(String nomeUsuario) { 
        this.nomeUsuario = nomeUsuario; 
    }
    
    public String getSenhaHash() { 
        return senhaHash; 
    }
    
    public void setSenhaHash(String senhaHash) { 
        this.senhaHash = senhaHash; 
    }
    
    public String getPerfil() { 
        return perfil; 
    }
    
    public void setPerfil(String perfil) { 
        this.perfil = perfil; 
    }
    
    public boolean isAtivo() { 
        return ativo; 
    }
    
    public void setAtivo(boolean ativo) { 
        this.ativo = ativo; 
    }
}