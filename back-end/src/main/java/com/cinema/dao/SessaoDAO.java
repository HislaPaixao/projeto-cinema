package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.model.Sessao;

public class SessaoDAO {
    private Connection connection;
    
    public SessaoDAO(Connection connection) {
        this.connection = connection;
    }
    
    // Listar sessões disponíveis
    public List<Sessao> listarSessoes() throws SQLException {
        List<Sessao> sessoes = new ArrayList<>();
        String sql = "SELECT s.id_sessao, s.data_hora, s.preco_base, " +
                     "f.titulo, f.classificacao_indicativa, " +
                     "sa.nome_sala, sa.tipo_sala, sa.id_sala " +
                     "FROM sessao s " +
                     "INNER JOIN filme f ON s.fk_filme_id_filme = f.id_filme " +
                     "INNER JOIN sala sa ON s.fk_sala_id_sala = sa.id_sala " +
                     "ORDER BY s.data_hora";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Sessao sessao = new Sessao();
                sessao.setId(rs.getInt("id_sessao"));
                sessao.setDataHora(rs.getString("data_hora"));
                sessao.setPrecoBase(rs.getDouble("preco_base"));
                sessao.setFilmeTitulo(rs.getString("titulo"));
                sessao.setClassificacao(rs.getString("classificacao_indicativa"));
                sessao.setSalaNome(rs.getString("nome_sala"));
                sessao.setSalaTipo(rs.getString("tipo_sala"));
                sessao.setSalaId(rs.getInt("id_sala"));
                
                sessoes.add(sessao);
            }
        }
        return sessoes;
    }
    
    // Buscar sessão por ID
    public Sessao buscarPorId(int id) throws SQLException {
        String sql = "SELECT s.id_sessao, s.data_hora, s.preco_base, " +
                     "f.titulo, f.classificacao_indicativa, " +
                     "sa.nome_sala, sa.tipo_sala, sa.id_sala " +
                     "FROM sessao s " +
                     "INNER JOIN filme f ON s.fk_filme_id_filme = f.id_filme " +
                     "INNER JOIN sala sa ON s.fk_sala_id_sala = sa.id_sala " +
                     "WHERE s.id_sessao = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Sessao sessao = new Sessao();
                    sessao.setId(rs.getInt("id_sessao"));
                    sessao.setDataHora(rs.getString("data_hora"));
                    sessao.setPrecoBase(rs.getDouble("preco_base"));
                    sessao.setFilmeTitulo(rs.getString("titulo"));
                    sessao.setClassificacao(rs.getString("classificacao_indicativa"));
                    sessao.setSalaNome(rs.getString("nome_sala"));
                    sessao.setSalaTipo(rs.getString("tipo_sala"));
                    sessao.setSalaId(rs.getInt("id_sala"));
                    return sessao;
                }
            }
        }
        return null;
    }
    
    // Buscar assentos ocupados de uma sessão
    public List<String> buscarAssentosOcupados(int sessaoId) throws SQLException {
        List<String> ocupados = new ArrayList<>();
        String sql = "SELECT a.numero_assento " +
                     "FROM ingresso i " +
                     "INNER JOIN assento a ON i.fk_assento_id_assento = a.id_assento " +
                     "WHERE i.fk_sessao_id_sessao = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sessaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ocupados.add(rs.getString("numero_assento"));
                }
            }
        }
        return ocupados;
    }
}