package com.cinema.servlet;

import com.cinema.dao.GeneroDAO;
import com.cinema.model.Genero;
import com.cinema.util.DatabaseConnection;
import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.Connection;
import java.util.List;

@WebServlet("/api/generos")
public class GeneroServlet extends HttpServlet {
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
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            GeneroDAO generoDAO = new GeneroDAO(connection);
            List<Genero> generos = generoDAO.listarTodos();
            response.getWriter().write(gson.toJson(generos));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
        }
    }
}