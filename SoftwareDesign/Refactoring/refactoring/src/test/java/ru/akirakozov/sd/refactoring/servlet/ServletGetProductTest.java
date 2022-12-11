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

public class ServletGetProductTest {
    private static final String EMPTY_RESPONSE = "<html><body>\r\n" +
            "</body></html>";
    private GetProductsServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void init() throws SQLException {
        MockitoAnnotations.openMocks(this);
        servlet = new GetProductsServlet(new ProductDAO());
        DatabasesUtils.create_products();
    }

    private void init_response(PrintWriter writer) throws IOException {
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void test_empty_response() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        init_response(printWriter);

        servlet.doGet(request, response);

        assertEquals(EMPTY_RESPONSE, stringWriter.toString().trim());
    }

    @Test
    public void test_add_get_data() throws IOException, SQLException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        init_response(printWriter);

        DatabasesUtils.insert_into_product();

        servlet.doGet(request, response);

        assertEquals(stringWriter.toString(),
                "<html><body>\r\n" +
                        "name_product_1\t1</br>\r\n" +
                        "name_product_2\t2</br>\r\n" +
                        "name_product_3\t3</br>\r\n" +
                        "</body></html>\r\n");
    }

    @AfterEach
    public void delete_from_products() throws SQLException {
        DatabasesUtils.delete_from_products();
    }
}
