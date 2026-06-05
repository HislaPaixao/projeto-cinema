package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cinema.model.Usuario;

public class UsuarioDAO {
    private Connection connection;
    
    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }
    
    public Usuario autenticar(String nomeUsuario, String senha) throws SQLException {
        String sql = "SELECT id_usuario, nome_completo, nome_usuario, senha_hash, perfil, ativo " +
                     "FROM usuario WHERE nome_usuario = ? AND senha_hash = ? AND ativo = true";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeUsuario);
            stmt.setString(2, senha); // Comparação direta, sem criptografia
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome_completo"),
                        rs.getString("nome_usuario"),
                        rs.getString("senha_hash"),
                        rs.getString("perfil"),
                        rs.getBoolean("ativo")
                    );
                }
            }
        }
        return null;
    }
}

