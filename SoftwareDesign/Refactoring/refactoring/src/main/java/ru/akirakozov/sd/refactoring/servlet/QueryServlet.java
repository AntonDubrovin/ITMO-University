package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.command.*;
import ru.akirakozov.sd.refactoring.dao.ProductDAO;
import ru.akirakozov.sd.refactoring.entity.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static ru.akirakozov.sd.refactoring.utils.Constants.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final ProductDAO productDAO;

    public QueryServlet(final ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter(COMMAND_PARAMETER);

        Products ans;
        if (MAX_COMMAND.equals(command)) {
            ans = new Max();
        } else if (MIN_COMMAND.equals(command)) {
            ans = new Min();
        } else if (SUM_COMMAND.equals(command)) {
            ans = new Sum();
        } else if (COUNT_COMMAND.equals(command)) {
            ans = new Count();
        } else {
            ans = new Unknown(command);
        }

        try {
            response.getWriter().println(ans.product_ans());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
