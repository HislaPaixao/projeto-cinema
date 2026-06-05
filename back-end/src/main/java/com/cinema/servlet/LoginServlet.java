package com.cinema.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cinema.dto.LoginRequest;
import com.cinema.dto.LoginResponse;
import com.cinema.service.LoginService;
import com.cinema.util.DatabaseConnection;
import com.google.gson.Gson;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private Gson gson = new Gson();
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Ler JSON do corpo da requisição
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        LoginRequest loginRequest = gson.fromJson(sb.toString(), LoginRequest.class);
        
        // Validar campos
        if (loginRequest.getUsuario() == null || loginRequest.getUsuario().trim().isEmpty() ||
            loginRequest.getSenha() == null || loginRequest.getSenha().trim().isEmpty()) {
            
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setSucesso(false);
            errorResponse.setMensagem("Por favor, preencha todos os campos.");
            
            response.getWriter().write(gson.toJson(errorResponse));
            return;
        }
        
        // Processar login
        try (Connection connection = DatabaseConnection.getConnection()) {
            LoginService loginService = new LoginService(connection);
            LoginResponse loginResponse = loginService.autenticar(
                loginRequest.getUsuario().trim().toLowerCase(),
                loginRequest.getSenha()
            );
            
            if (loginResponse.isSucesso()) {
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", loginResponse.getNomeUsuario());
                session.setAttribute("perfil", loginResponse.getPerfil());
                session.setMaxInactiveInterval(30 * 60);
            }
            
            String jsonResponse = gson.toJson(loginResponse);
            response.getWriter().write(jsonResponse);
            
        } catch (Exception e) {
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setSucesso(false);
            errorResponse.setMensagem("Erro no servidor: " + e.getMessage());
            
            response.getWriter().write(gson.toJson(errorResponse));
            e.printStackTrace();
        }
    }
}