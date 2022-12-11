package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDAO;
import ru.akirakozov.sd.refactoring.entity.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.akirakozov.sd.refactoring.utils.Constants.*;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductDAO productDAO;

    public GetProductsServlet(final ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.getWriter().println(OPEN_TAGS);

            for (Product product : productDAO.get_products()) {
                response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
            }

            response.getWriter().println(CLOSE_TAGS);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
