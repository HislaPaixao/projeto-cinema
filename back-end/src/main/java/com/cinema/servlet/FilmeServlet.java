package com.cinema.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cinema.dao.FilmeDAO;
import com.cinema.model.Filme;
import com.cinema.util.DatabaseConnection;
import com.google.gson.Gson;

@WebServlet("/api/filmes/*")
public class FilmeServlet extends HttpServlet {
    private Gson gson = new Gson();
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    // GET - Listar todos ou buscar por ID/pesquisa
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            FilmeDAO filmeDAO = new FilmeDAO(connection);
            
            if (pathInfo == null || pathInfo.equals("/")) {
                String pesquisa = request.getParameter("q");
                List<Filme> filmes;
                
                if (pesquisa != null && !pesquisa.trim().isEmpty()) {
                    filmes = filmeDAO.buscarPorTitulo(pesquisa);
                } else {
                    filmes = filmeDAO.listarTodos();
                }
                
                response.getWriter().write(gson.toJson(filmes));
                
            } else {
                try {
                    int id = Integer.parseInt(pathInfo.substring(1));
                    Filme filme = filmeDAO.buscarPorId(id);
                    
                    if (filme != null) {
                        response.getWriter().write(gson.toJson(filme));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"erro\":\"Filme não encontrado\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"erro\":\"ID inválido\"}");
                }
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
    
    // POST - Criar novo filme + sessão automática
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        Filme filme = gson.fromJson(sb.toString(), Filme.class);
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            FilmeDAO filmeDAO = new FilmeDAO(connection);
            Filme novoFilme = filmeDAO.inserir(filme);
            
            // Criar sessão automática
            criarSessaoAutomatica(connection, novoFilme.getId());
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(novoFilme));
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
    
    // PUT - Atualizar filme
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        Filme filme = gson.fromJson(sb.toString(), Filme.class);
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            FilmeDAO filmeDAO = new FilmeDAO(connection);
            filmeDAO.atualizar(filme);
            
            response.getWriter().write("{\"mensagem\":\"Filme atualizado com sucesso!\"}");
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
    
    // DELETE - Excluir filme
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            
            try (Connection connection = DatabaseConnection.getConnection()) {
                FilmeDAO filmeDAO = new FilmeDAO(connection);
                boolean excluido = filmeDAO.excluir(id);
                
                if (excluido) {
                    response.getWriter().write("{\"mensagem\":\"Filme excluído com sucesso!\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("{\"erro\":\"Não é possível excluir. Filme possui sessões vinculadas.\"}");
                }
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\":\"ID inválido\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
    
    // Método para criar sessão automática
    private void criarSessaoAutomatica(Connection connection, int filmeId) {
        try {
            String sql = "INSERT INTO sessao (data_hora, preco_base, fk_filme_id_filme, fk_sala_id_sala) " +
                         "VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                // Data: amanhã às 19:00
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 19);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                Timestamp dataHora = new Timestamp(cal.getTimeInMillis());
                
                stmt.setTimestamp(1, dataHora);
                stmt.setDouble(2, 15.00); // Preço base padrão
                stmt.setInt(3, filmeId);
                stmt.setInt(4, 1); // Sala 1 padrão
                
                stmt.executeUpdate();
                System.out.println("✅ Sessão automática criada para o filme ID: " + filmeId);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar sessão automática: " + e.getMessage());
        }
    }
}