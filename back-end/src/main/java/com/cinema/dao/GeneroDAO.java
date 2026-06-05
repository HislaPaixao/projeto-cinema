package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.model.Genero;

public class GeneroDAO {
    private Connection connection;
    
    public GeneroDAO(Connection connection) {
        this.connection = connection;
    }
    
    public List<Genero> listarTodos() throws SQLException {
        List<Genero> generos = new ArrayList<>();
        String sql = "SELECT id_genero, genero FROM genero ORDER BY genero";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Genero genero = new Genero(
                    rs.getInt("id_genero"),
                    rs.getString("genero")
                );
                generos.add(genero);
            }
        }
        return generos;
    }
}