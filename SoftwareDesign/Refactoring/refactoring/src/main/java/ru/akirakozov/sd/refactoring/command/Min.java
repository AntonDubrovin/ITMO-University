package ru.akirakozov.sd.refactoring.command;

import ru.akirakozov.sd.refactoring.productAns.ProductAns;

import java.sql.SQLException;

public class Min extends Products {
    @Override
    public String product_ans() throws SQLException {
        return ProductAns.generateProductAns(productDAO.getMinProduct().get(), "Product with min price");
    }
}
