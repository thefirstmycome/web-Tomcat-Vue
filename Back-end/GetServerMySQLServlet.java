package org.example.demo;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "GetServerMySQLServlet", value = {"/GetServerMySQL"})
public class GetServerMySQLServlet extends HttpServlet {
    private PersonDAO personDAO = new PersonDAO();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void init() {
        // 初始化操作
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>GetServerMySQLServlet</h1>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonNode rootNode = objectMapper.readTree(request.getReader());
        String account = rootNode.get("Account").asText();
        Map<String, Object> result = new HashMap<>();

        response.setContentType("application/json");
        response.getWriter().write(personDAO.GetServerMySQL(account));
    }

    public void destroy() {
        // 销毁操作
    }
}