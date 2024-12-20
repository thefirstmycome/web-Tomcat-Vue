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

@WebServlet(name = "RegisterServlet", value = {"/register"})
public class RegisterServlet extends HttpServlet {
    private PersonDAO personDAO = new PersonDAO();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void init() {
        // 初始化操作
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>RegisterServlet</h1>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonNode rootNode = objectMapper.readTree(request.getReader());
        String account = rootNode.get("Account").asText();
        Map<String, Object> result = new HashMap<>();

        String username = rootNode.get("UserName").asText();
        String password1 = rootNode.get("Password").asText();
        if (personDAO.register(account,  username, password1)) {
            result.put("code",  0);
            result.put("msg",  "注册成功");
        } else {
            result.put("code",  1);
            result.put("msg",  "注册信息不符合条件规范或账号已存在");
        }

        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    public void destroy() {
        // 销毁操作
    }
}