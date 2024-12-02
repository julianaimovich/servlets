package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;

    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    private static final String BASE_ENDPOINT = "/api/posts";
    private static final String ENDPOINT_BY_ID = BASE_ENDPOINT + "/\\d+";

    @Override
    public void init() {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ru.netology");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final String path = req.getRequestURI();
            final String method = req.getMethod();
            // primitive routing
            if (method.equals(METHOD_GET) && path.equals(BASE_ENDPOINT)) {
                controller.all(resp);
                return;
            }
            if (method.equals(METHOD_GET) && path.matches(ENDPOINT_BY_ID)) {
                // easy way
                final long id = getPostId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(METHOD_POST) && path.equals(BASE_ENDPOINT)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(METHOD_DELETE) && path.matches(ENDPOINT_BY_ID)) {
                // easy way
                final long id = getPostId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private static long getPostId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/")));
    }
}