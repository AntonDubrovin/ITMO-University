package info.kgeorgiy.ja.dubrovin.rmi;

public class RemoteAccount implements Account {
    private final String id;
    private int amount;

    public RemoteAccount(final String id) {
        this.id = id;
        this.amount = 0;
    }

    @Override
    public synchronized String getId() {
        System.out.println("Account id: " + id);
        return id;
    }

    @Override
    public synchronized int getAmount() {
        System.out.println("Amount for account " + id + " is " + amount);
        return amount;
    }

    @Override
    public synchronized void setAmount(final int amount) {
        System.out.println("New amount for account " + id + " is " + amount);
        this.amount = amount;
    }
}