package ru.akirakozov.sd.refactoring.utils;

import java.sql.*;
import java.util.Map;

public class DatabasesUtils {
    public static final Map<String, Integer> PRODUCTS = Map.of(
            "name_product_1", 1,
            "name_product_2", 2,
            "name_product_3", 3
    );
    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE IF NOT EXISTS PRODUCT"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + " NAME           TEXT    NOT NULL, "
                    + " PRICE          INT     NOT NULL)";
    private static final String DELETE_FROM_PRODUCT = "DELETE FROM PRODUCT";
    private static final String INSERT_INTO_PRODUCT =
            "INSERT INTO PRODUCT (NAME, PRICE) " +
                    "VALUES (?, ?)";

    public static Connection get_connection() throws SQLException {
        return DriverManager.getConnection(Constants.CONNECTION_DB);
    }

    public static void create_products() throws SQLException {
        execute_update(CREATE_TABLE_PRODUCT);
    }

    public static void delete_from_products() throws SQLException {
        execute_update(DELETE_FROM_PRODUCT);
    }

    public static void insert_into_product() throws SQLException {
        for (Map.Entry<String, Integer> entry : PRODUCTS.entrySet()) {
            try (Connection c = get_connection()) {
                try (PreparedStatement statement = c.prepareStatement(INSERT_INTO_PRODUCT)) {
                    statement.setString(1, entry.getKey());
                    statement.setInt(2, entry.getValue());
                    statement.executeUpdate();
                }
            }
        }
    }

    public static void execute_update(String sql_request) throws SQLException {
        try (Connection c = get_connection()) {
            try (Statement statement = c.createStatement()) {
                statement.executeUpdate(sql_request);
            }
        }
    }
}


