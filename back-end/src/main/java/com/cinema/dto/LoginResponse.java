package com.cinema.dto;

public class LoginResponse {
    private boolean sucesso;
    private String mensagem;
    private String perfil;
    private String nomeUsuario;
    private String redirectUrl;
    
    public LoginResponse() {
        this.sucesso = false;
        this.mensagem = "";
    }
    
    // Getters e Setters
    public boolean isSucesso() { 
        return sucesso; 
    }
    
    public void setSucesso(boolean sucesso) { 
        this.sucesso = sucesso; 
    }
    
    public String getMensagem() { 
        return mensagem; 
    }
    
    public void setMensagem(String mensagem) { 
        this.mensagem = mensagem; 
    }
    
    public String getPerfil() { 
        return perfil; 
    }
    
    public void setPerfil(String perfil) { 
        this.perfil = perfil; 
    }
    
    public String getNomeUsuario() { 
        return nomeUsuario; 
    }
    
    public void setNomeUsuario(String nomeUsuario) { 
        this.nomeUsuario = nomeUsuario; 
    }
    
    public String getRedirectUrl() { 
        return redirectUrl; 
    }
    
    public void setRedirectUrl(String redirectUrl) { 
        this.redirectUrl = redirectUrl; 
    }
}