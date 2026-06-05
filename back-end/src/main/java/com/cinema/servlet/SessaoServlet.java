package com.cinema.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cinema.dao.SessaoDAO;
import com.cinema.model.Sessao;
import com.cinema.util.DatabaseConnection;
import com.google.gson.Gson;

@WebServlet("/api/sessoes/*")
public class SessaoServlet extends HttpServlet {
    private Gson gson = new Gson();
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            SessaoDAO sessaoDAO = new SessaoDAO(connection);
            
            if (pathInfo == null || pathInfo.equals("/")) {
                // Listar todas as sessões
                List<Sessao> sessoes = sessaoDAO.listarSessoes();
                response.getWriter().write(gson.toJson(sessoes));
                
            } else if (pathInfo.endsWith("/assentos")) {
                // Buscar assentos ocupados de uma sessão
                String[] parts = pathInfo.split("/");
                int sessaoId = Integer.parseInt(parts[1]);
                
                List<String> ocupados = sessaoDAO.buscarAssentosOcupados(sessaoId);
                response.getWriter().write(gson.toJson(ocupados));
                
            } else {
                // Buscar sessão por ID
                int id = Integer.parseInt(pathInfo.substring(1));
                Sessao sessao = sessaoDAO.buscarPorId(id);
                
                if (sessao != null) {
                    response.getWriter().write(gson.toJson(sessao));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"erro\":\"Sessão não encontrada\"}");
                }
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}