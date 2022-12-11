package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.dao.ProductDAO;
import ru.akirakozov.sd.refactoring.utils.Constants;
import ru.akirakozov.sd.refactoring.utils.DatabasesUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ServletAddProductTest {
    private AddProductServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private final String NAME_PARAMETER = "name";
    private final String PRICE_PARAMETER = "price";
    private final String OK_STATUS = "OK";

    @BeforeEach
    public void init() throws SQLException {
        MockitoAnnotations.openMocks(this);
        servlet = new AddProductServlet(new ProductDAO());
        DatabasesUtils.create_products();
    }


    private void init_add_request() {
        when(request.getParameter(NAME_PARAMETER)).thenReturn("name_parameter");
        when(request.getParameter(PRICE_PARAMETER)).thenReturn("10");
    }

    private void init_response(PrintWriter writer) throws IOException {
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void check_ok_response() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        init_add_request();
        init_response(printWriter);

        servlet.doGet(request, response);

        printWriter.flush();
        assertEquals(OK_STATUS, stringWriter.toString().trim());
    }

    @Test
    public void check_add() throws IOException, SQLException {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        init_add_request();
        init_response(printWriter);

        servlet.doGet(request, response);

        try (Connection c = DriverManager.getConnection(Constants.CONNECTION_DB)) {
            try (Statement statement = c.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT");

                assertTrue(resultSet.next());
                String name = resultSet.getString("name");
                String price = resultSet.getString("price");
                assertEquals(name, "name_parameter");
                assertEquals(price, "10");
            }
        }
    }

    @AfterEach
    public void delete_from_products() throws SQLException {
        DatabasesUtils.delete_from_products();
    }
}

