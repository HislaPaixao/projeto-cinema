package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cinema.model.Filme;

public class FilmeDAO {
    private Connection connection;
    
    public FilmeDAO(Connection connection) {
        this.connection = connection;
    }
    
    // LISTAR TODOS OS FILMES
    public List<Filme> listarTodos() throws SQLException {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT f.id_filme, f.titulo, f.duracao, f.classificacao_indicativa, " +
                     "f.fk_genero_id_genero, g.genero " +
                     "FROM filme f " +
                     "INNER JOIN genero g ON f.fk_genero_id_genero = g.id_genero " +
                     "ORDER BY f.titulo";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Filme filme = new Filme(
                    rs.getInt("id_filme"),
                    rs.getString("titulo"),
                    rs.getInt("duracao"),
                    rs.getString("classificacao_indicativa"),
                    rs.getInt("fk_genero_id_genero"),
                    rs.getString("genero")
                );
                filmes.add(filme);
            }
        }
        return filmes;
    }
    
    // BUSCAR POR ID
    public Filme buscarPorId(int id) throws SQLException {
        String sql = "SELECT f.id_filme, f.titulo, f.duracao, f.classificacao_indicativa, " +
                     "f.fk_genero_id_genero, g.genero " +
                     "FROM filme f " +
                     "INNER JOIN genero g ON f.fk_genero_id_genero = g.id_genero " +
                     "WHERE f.id_filme = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Filme(
                        rs.getInt("id_filme"),
                        rs.getString("titulo"),
                        rs.getInt("duracao"),
                        rs.getString("classificacao_indicativa"),
                        rs.getInt("fk_genero_id_genero"),
                        rs.getString("genero")
                    );
                }
            }
        }
        return null;
    }
    
    // BUSCAR POR TÍTULO (para pesquisa)
    public List<Filme> buscarPorTitulo(String titulo) throws SQLException {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT f.id_filme, f.titulo, f.duracao, f.classificacao_indicativa, " +
                     "f.fk_genero_id_genero, g.genero " +
                     "FROM filme f " +
                     "INNER JOIN genero g ON f.fk_genero_id_genero = g.id_genero " +
                     "WHERE f.titulo LIKE ? " +
                     "ORDER BY f.titulo";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Filme filme = new Filme(
                        rs.getInt("id_filme"),
                        rs.getString("titulo"),
                        rs.getInt("duracao"),
                        rs.getString("classificacao_indicativa"),
                        rs.getInt("fk_genero_id_genero"),
                        rs.getString("genero")
                    );
                    filmes.add(filme);
                }
            }
        }
        return filmes;
    }
    
    // INSERIR NOVO FILME
    public Filme inserir(Filme filme) throws SQLException {
        String sql = "INSERT INTO filme (titulo, duracao, classificacao_indicativa, fk_genero_id_genero) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, filme.getTitulo());
            stmt.setInt(2, filme.getDuracao());
            stmt.setString(3, filme.getClassificacaoIndicativa());
            stmt.setInt(4, filme.getGeneroId());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    filme.setId(rs.getInt(1));
                }
            }
        }
        return filme;
    }
    
    // ATUALIZAR FILME
    public void atualizar(Filme filme) throws SQLException {
        String sql = "UPDATE filme SET titulo = ?, duracao = ?, classificacao_indicativa = ?, " +
                     "fk_genero_id_genero = ? WHERE id_filme = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, filme.getTitulo());
            stmt.setInt(2, filme.getDuracao());
            stmt.setString(3, filme.getClassificacaoIndicativa());
            stmt.setInt(4, filme.getGeneroId());
            stmt.setInt(5, filme.getId());
            
            stmt.executeUpdate();
        }
    }
    
    // EXCLUIR FILME (com verificação de sessões)
    public boolean excluir(int id) throws SQLException {
        // Verificar se tem sessões vinculadas
        String sqlVerificar = "SELECT COUNT(*) FROM sessao WHERE fk_filme_id_filme = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlVerificar)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Tem sessões, não pode excluir
                }
            }
        }
        
        // Excluir o filme
        String sqlExcluir = "DELETE FROM filme WHERE id_filme = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlExcluir)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        return true;
    }
}