package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.dao.ProductDAO;
import ru.akirakozov.sd.refactoring.utils.DatabasesUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ServletQueryTest {
    private static final String COMMAND_PARAMETER = "command";
    private static final String MIN_COMMAND = "min";
    private static final String MAX_COMMAND = "max";
    private static final String SUM_PRICES_COMMAND = "sum";
    private static final String COUNT_PRODUCTS_COMMAND = "count";
    private QueryServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void init() throws SQLException {
        MockitoAnnotations.openMocks(this);
        servlet = new QueryServlet(new ProductDAO());
        DatabasesUtils.create_products();
        DatabasesUtils.insert_into_product();
    }

    private void init_response(PrintWriter writer) throws IOException {
        when(response.getWriter()).thenReturn(writer);
    }

    private void init_command(String command) throws IOException {
        when(request.getParameter(COMMAND_PARAMETER)).thenReturn(command);
    }

    @Test
    public void test_min() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        init_response(printWriter);
        init_command(MIN_COMMAND);

        servlet.doGet(request, response);

        assertEquals(stringWriter.toString(),
                "<html><body>\r\n" +
                        "<h1>Product with min price: </h1>\r\n" +
                        "name_product_1\t1</br>\r\n" +
                        "</body></html>\r\n");
    }

    @Test
    public void test_max() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        init_response(printWriter);
        init_command(MAX_COMMAND);

        servlet.doGet(request, response);

        assertEquals(stringWriter.toString(),
                "<html><body>\r\n" +
                        "<h1>Product with max price: </h1>\r\n" +
                        "name_product_3\t3</br>\r\n" +
                        "</body></html>\r\n");
    }

    @Test
    public void test_sum_prices() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        init_response(printWriter);
        init_command(SUM_PRICES_COMMAND);

        servlet.doGet(request, response);

        assertEquals(stringWriter.toString(),
                "<html><body>\r\n" +
                        "Summary price: \r\n" +
                        "6\r\n" +
                        "</body></html>\r\n");
    }

    @Test
    public void test_count_products() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        init_response(printWriter);
        init_command(COUNT_PRODUCTS_COMMAND);

        servlet.doGet(request, response);

        assertEquals(stringWriter.toString(),
                "<html><body>\r\n" +
                        "Number of products: \r\n" +
                        "3\r\n" +
                        "</body></html>\r\n");
    }

    @AfterEach
    public void delete_from_products() throws SQLException {
        DatabasesUtils.delete_from_products();
    }
}
