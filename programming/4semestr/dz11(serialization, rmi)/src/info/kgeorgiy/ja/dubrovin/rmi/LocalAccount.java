package info.kgeorgiy.ja.dubrovin.rmi;

import java.io.Serializable;

public class LocalAccount implements Account, Serializable {
    private final String id;
    private int amount;

    public LocalAccount(final String id, final int amount) {
        this.id = id;
        this.amount = amount;
    }

    public LocalAccount(final String id) {
        this(id, 0);
    }

    @Override
    public String getId() {
        System.out.println("Account id: " + id);
        return id;
    }

    @Override
    public int getAmount() {
        System.out.println("Amount for account " + id + " is " + amount);
        return amount;
    }

    @Override
    public void setAmount(final int amount) {
        System.out.println("New amount for account " + id + " is " + amount);
        this.amount = amount;
    }
}
