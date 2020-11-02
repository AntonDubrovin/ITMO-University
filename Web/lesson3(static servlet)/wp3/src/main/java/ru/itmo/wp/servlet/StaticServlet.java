package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class StaticServlet extends HttpServlet {
    public static final String STATIC_DIR = "/home/anton/Рабочий стол/University/Web/lesson3/wp3/src/main/webapp/static";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String[] parsedUri = uri.split("\\+");
        for (int i = 0; i < parsedUri.length; i++) {
            String curUri = parsedUri[i];
            if (curUri.charAt(0) != '/') {
                curUri = '/' + curUri;
            }
            File file = new File(STATIC_DIR + curUri);
            if (!file.isFile()) {
                file = new File(getServletContext().getRealPath("/static" + curUri));
            }
            if (file.isFile()) {
                if (i == 0) {
                    response.setContentType(getContentTypeFromName(curUri));
                }
                OutputStream outputStream = response.getOutputStream();
                Files.copy(file.toPath(), outputStream);
                outputStream.flush();
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }



    private String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        }

        if (name.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (name.endsWith(".html")) {
            return "text/html";
        }

        if (name.endsWith(".css")) {
            return "text/css";
        }

        if (name.endsWith(".js")) {
            return "application/javascript";
        }

        throw new IllegalArgumentException("Can't find content type for '" + name + "'.");
    }
}
