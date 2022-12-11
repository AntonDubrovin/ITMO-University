package ru.akirakozov.sd.refactoring.command;

import ru.akirakozov.sd.refactoring.dao.ProductDAO;
import ru.akirakozov.sd.refactoring.productAns.ProductAns;

import java.sql.SQLException;

public abstract class Products {

    protected final ProductDAO productDAO;
    protected final ProductAns productAns;

    protected Products() {
        this.productDAO = new ProductDAO();
        this.productAns = new ProductAns();
    }

    public abstract String product_ans() throws SQLException;
}