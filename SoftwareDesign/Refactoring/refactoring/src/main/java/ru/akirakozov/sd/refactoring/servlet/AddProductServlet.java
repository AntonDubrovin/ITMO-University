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
public class AddProductServlet extends HttpServlet {
    private final ProductDAO productDAO;

    public AddProductServlet(final ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(NAME_PARAMETER);
        long price = Long.parseLong(request.getParameter(PRICE_PARAMETER));

        try {
            productDAO.add_product(new Product(name, price));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
