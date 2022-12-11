package ru.akirakozov.sd.refactoring.productAns;

import ru.akirakozov.sd.refactoring.entity.Product;

import java.util.List;

import static ru.akirakozov.sd.refactoring.utils.Constants.CLOSE_TAGS;
import static ru.akirakozov.sd.refactoring.utils.Constants.OPEN_TAGS;

public class ProductAns {
    public static String generateProductAns(List<Product> products) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product product : products) {
            stringBuilder.append(get_name_price(product));
        }
        return wrapToTags(stringBuilder.toString());
    }

    public static String generateProductAns(Product product, String message) {
        return wrapToTags("<h1>" + message + ": </h1>\r\n" + get_name_price(product));
    }

    public static String generateProductAns(Integer value, String message) {
        return wrapToTags(message + ": \r\n" + (value == null ? "" : value));
    }

    private static String get_name_price(Product product) {
        return product == null ? "" : product.getName() + "\t" + product.getPrice() + "</br>";
    }

    private static String wrapToTags(String ans) {
        return OPEN_TAGS + "\r\n" + ans + (ans.equals("") ? "" : "\r\n") + CLOSE_TAGS;
    }
}
