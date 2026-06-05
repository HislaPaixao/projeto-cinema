package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cinema.model.Ingresso;

public class IngressoDAO {
    private Connection connection;
    
    public IngressoDAO(Connection connection) {
        this.connection = connection;
    }
    
    // VENDER INGRESSO
    public Ingresso vender(Ingresso ingresso) throws SQLException {
        String sql = "INSERT INTO ingresso (tipo_ingresso, valor_pago, data_venda, " +
                     "fk_sessao_id_sessao, fk_assento_id_assento) VALUES (?, ?, NOW(), ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ingresso.getTipoIngresso());
            stmt.setDouble(2, ingresso.getValorPago());
            stmt.setInt(3, ingresso.getSessaoId());
            stmt.setInt(4, ingresso.getAssentoId());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ingresso.setId(rs.getInt(1));
                }
            }
        }
        return ingresso;
    }
    
    // BUSCAR ID DO ASSENTO
    public int buscarAssentoId(String numeroAssento, int salaId) throws SQLException {
        String sql = "SELECT id_assento FROM assento WHERE numero_assento = ? AND fk_sala_id_sala = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroAssento);
            stmt.setInt(2, salaId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_assento");
                }
            }
        }
        return -1;
    }
    
    // LISTAR INGRESSOS VENDIDOS
    public List<Ingresso> listarVendidos() throws SQLException {
        List<Ingresso> ingressos = new ArrayList<>();
        String sql = "SELECT i.id_ingresso, i.tipo_ingresso, i.valor_pago, i.data_venda, " +
                     "i.fk_sessao_id_sessao, a.numero_assento, f.titulo " +
                     "FROM ingresso i " +
                     "INNER JOIN assento a ON i.fk_assento_id_assento = a.id_assento " +
                     "INNER JOIN sessao s ON i.fk_sessao_id_sessao = s.id_sessao " +
                     "INNER JOIN filme f ON s.fk_filme_id_filme = f.id_filme " +
                     "ORDER BY i.data_venda DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Ingresso ingresso = new Ingresso();
                ingresso.setId(rs.getInt("id_ingresso"));
                ingresso.setTipoIngresso(rs.getString("tipo_ingresso"));
                ingresso.setValorPago(rs.getDouble("valor_pago"));
                ingresso.setDataVenda(rs.getString("data_venda"));
                ingresso.setSessaoId(rs.getInt("fk_sessao_id_sessao"));
                ingresso.setAssentoNumero(rs.getString("numero_assento"));
                // Não tem setFilme no modelo, mas o campo titulo vem na query
                ingressos.add(ingresso);
            }
        }
        return ingressos;
    }
    
    // ESTORNAR (CANCELAR) INGRESSO
    public boolean estornar(int id) throws SQLException {
        String sql = "DELETE FROM ingresso WHERE id_ingresso = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }
}