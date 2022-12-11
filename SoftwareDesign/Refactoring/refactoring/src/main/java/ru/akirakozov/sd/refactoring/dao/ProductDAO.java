package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.entity.Product;
import ru.akirakozov.sd.refactoring.utils.DatabasesUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.akirakozov.sd.refactoring.utils.Constants.*;

public class ProductDAO {

    public void add_product(Product product) throws SQLException {
        try (Connection c = DatabasesUtils.get_connection()) {
            DatabasesUtils.execute_update("INSERT INTO PRODUCT " + "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")");
        }
    }

    public List<Product> get_products() throws SQLException {
        return execute_query("SELECT * FROM PRODUCT", this::returnedProduct);
    }

    public Optional<Product> getMaxProduct() throws SQLException {
        return nullOrLastProduct(execute_query("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", this::returnedProduct));
    }

    public Optional<Product> getMinProduct() throws SQLException {
        return nullOrLastProduct(execute_query("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", this::returnedProduct));
    }

    public Optional<Integer> getSumProducts() throws SQLException {
        return nullOrLastProduct(execute_query("SELECT SUM(price) FROM PRODUCT", this::getResult));
    }

    public Optional<Integer> getCountProducts() throws SQLException {
        return nullOrLastProduct(execute_query("SELECT COUNT(*) FROM PRODUCT", this::getResult));
    }

    public static <T> List<T> execute_query(String sql_request, SQLFunction<ResultSet, T> rsFunction) throws SQLException {
        try (Connection c = DatabasesUtils.get_connection()) {
            try (Statement statement = c.createStatement()) {
                try (ResultSet rs = statement.executeQuery(sql_request)) {
                    List<T> res = new ArrayList<>();
                    while (rs.next()) {
                        res.add(rsFunction.apply(rs));
                    }

                    return res;
                }
            }
        }
    }

    private <T> Optional<T> nullOrLastProduct(List<T> products) {
        return products.isEmpty() ? Optional.empty() : Optional.of(products.get(products.size() - 1));
    }

    interface SQLFunction<T, R> {
        R apply(T arg) throws SQLException;
    }

    private Product returnedProduct(ResultSet rs) throws SQLException {
        String name = rs.getString(NAME_PARAMETER);
        int price = rs.getInt(PRICE_PARAMETER);
        return new Product(name, price);
    }

    private Integer getResult(ResultSet rs) throws SQLException {
        return rs.getInt(1);
    }
}
