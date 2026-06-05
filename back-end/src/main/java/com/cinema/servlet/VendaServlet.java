package com.cinema.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cinema.dao.IngressoDAO;
import com.cinema.model.Ingresso;
import com.cinema.util.DatabaseConnection;
import com.google.gson.Gson;

@WebServlet("/api/vendas/*")
public class VendaServlet extends HttpServlet {
    private Gson gson = new Gson();
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    // GET - Listar ingressos vendidos
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            IngressoDAO ingressoDAO = new IngressoDAO(connection);
            List<Ingresso> ingressos = ingressoDAO.listarVendidos();
            response.getWriter().write(gson.toJson(ingressos));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // POST - Confirmar venda
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
    
    System.out.println("📥 Recebido: " + sb.toString());
    
    try {
        Map<String, Object> vendaData = gson.fromJson(sb.toString(), Map.class);
        
        int sessaoId = ((Number) vendaData.get("sessaoId")).intValue();
        int salaId = ((Number) vendaData.get("salaId")).intValue();
        List<Map<String, Object>> ingressos = (List<Map<String, Object>>) vendaData.get("ingressos");
        
        System.out.println("Sessão: " + sessaoId + " | Sala: " + salaId);
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            IngressoDAO ingressoDAO = new IngressoDAO(connection);
            
            for (Map<String, Object> ing : ingressos) {
                String assento = (String) ing.get("assento");
                String tipo = (String) ing.get("tipo");
                double valor = ((Number) ing.get("valor")).doubleValue();
                
                System.out.println("  Assento: " + assento + " | Tipo: " + tipo + " | Valor: " + valor);
                
                int assentoId = ingressoDAO.buscarAssentoId(assento, salaId);
                System.out.println("  Assento ID: " + assentoId);
                
                if (assentoId == -1) {
                    System.out.println("❌ Assento não encontrado: " + assento + " na sala " + salaId);
                    continue;
                }
                
                Ingresso ingresso = new Ingresso();
                ingresso.setTipoIngresso(tipo);
                ingresso.setValorPago(valor);
                ingresso.setSessaoId(sessaoId);
                ingresso.setAssentoId(assentoId);
                
                ingressoDAO.vender(ingresso);
                System.out.println("✅ Ingresso salvo!");
            }
            
            response.getWriter().write("{\"sucesso\":true,\"mensagem\":\"Venda confirmada!\"}");
            
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
        }
        
    } catch (Exception e) {
        System.err.println("❌ JSON inválido: " + e.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"erro\":\"JSON inválido\"}");
    }
}
    
    // DELETE - Estornar ingresso
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
                IngressoDAO ingressoDAO = new IngressoDAO(connection);
                boolean estornado = ingressoDAO.estornar(id);
                
                if (estornado) {
                    response.getWriter().write("{\"sucesso\":true,\"mensagem\":\"Ingresso estornado!\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"erro\":\"Ingresso não encontrado\"}");
                }
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}