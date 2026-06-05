package com.cinema.service;

import com.cinema.dao.UsuarioDAO;
import com.cinema.dto.LoginResponse;
import com.cinema.model.Usuario;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginService {
    private UsuarioDAO usuarioDAO;
    
    public LoginService(Connection connection) {
        this.usuarioDAO = new UsuarioDAO(connection);
    }
    
    public LoginResponse autenticar(String nomeUsuario, String senha) {
        LoginResponse response = new LoginResponse();
        
        try {
            Usuario usuario = usuarioDAO.autenticar(nomeUsuario, senha);
            
            if (usuario != null) {
                response.setSucesso(true);
                response.setMensagem("Login realizado com sucesso!");
                response.setPerfil(usuario.getPerfil());
                response.setNomeUsuario(usuario.getNomeCompleto());
                
                // Definir URL de redirecionamento baseado no perfil
                if ("ADMIN".equals(usuario.getPerfil())) {
                    response.setRedirectUrl("../filmes/index.html");
                } else {
                    response.setRedirectUrl("../bilheteria/index.html");
                }
            } else {
                response.setSucesso(false);
                response.setMensagem("Usuário ou senha inválidos!");
            }
            
        } catch (SQLException e) {
            response.setSucesso(false);
            response.setMensagem("Erro no sistema. Tente novamente.");
            e.printStackTrace();
        }
        
        return response;
    }
}