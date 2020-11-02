package ru.itmo.web.lesson4.web;

import freemarker.template.*;
import ru.itmo.web.lesson4.util.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerServlet extends HttpServlet {
    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    private final Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

    @Override
    public void init() throws ServletException {
        super.init();

        File dir = new File(getServletContext().getRealPath("."), "../../src/main/webapp/WEB-INF/templates");
        try {
            cfg.setDirectoryForTemplateLoading(dir);
        } catch (IOException e) {
            throw new ServletException("Unable to set template directory [dit=" + dir + "].", e);
        }

        cfg.setDefaultEncoding(UTF_8);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);

        String uri = request.getRequestURI();
        if (uri.equals("/")) {
            uri = "/home";
        }

        Template template;
        try {
            template = cfg.getTemplate(URLDecoder.decode(uri, UTF_8) + ".ftlh");
        } catch (TemplateNotFoundException | IllegalArgumentException ignored) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            template = cfg.getTemplate("error.ftlh");
        }

        Map<String, Object> data = getData(request);

        response.setContentType("text/html");
        try {
            template.process(data, response.getWriter());
        } catch (TemplateException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private HashMap<String, Object> getData(HttpServletRequest request) {
        HashMap<String, Object> data = new HashMap<>();

        for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
            if (e.getValue() != null && e.getValue().length == 1) {
                if (e.getKey().equals("user_id") || e.getKey().equals("post_id")) {
                    try {
                        data.put(e.getKey(), Long.parseLong(e.getValue()[0]));
                    } catch (NumberFormatException numberFormatException) {
                        data.put(e.getKey(), -1);
                    }
                } else {
                    data.put(e.getKey(), e.getValue()[0]);
                }
            }
        }

        data.put("uri", request.getRequestURI());

        DataUtil.addData(request, data);
        return data;
    }
}
