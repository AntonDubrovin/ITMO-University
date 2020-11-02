package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageServlet extends HttpServlet {
    List<Object> messages = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        Object answer = null;
        Object user, text;
        HttpSession session = request.getSession();

        switch (uri) {
            case "/message/auth":
                user = request.getParameter("user");
                if (user != null) {
                    session.setAttribute("user", user);
                    answer = user;
                } else if (session.getAttribute("user") == null) {
                    answer = "";
                } else {
                    answer = session.getAttribute("user");
                }
                break;
            case "/message/findAll":
                answer = messages;
                break;
            case "/message/add":
                text = request.getParameter("text");
                user = session.getAttribute("user");
                if (text != null && user != null) {
                    messages.add(Map.of("user", user, "text", text));
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        response.setContentType("application/json");
        String json = new Gson().toJson(answer);
        response.getWriter().print(json);
        response.getWriter().flush();
    }
}