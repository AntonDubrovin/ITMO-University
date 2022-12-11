package ru.akirakozov.sd.refactoring.entity;

public final class Product {
    private final String name;
    private final long price;

    public Product(final String name, final long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
