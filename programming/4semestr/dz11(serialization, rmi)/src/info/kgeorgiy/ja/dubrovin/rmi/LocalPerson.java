package info.kgeorgiy.ja.dubrovin.rmi;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class LocalPerson implements Person, Externalizable {
    private String name, surname, passport;
    private final Map<String, LocalAccount> accounts;

    public LocalPerson(final String name, final String surname,
                       final String passport, final Map<String, LocalAccount> accounts) {
        this.name = name;
        this.surname = surname;
        this.passport = passport;
        this.accounts = accounts;
    }

    public LocalPerson() {
        this(null, null, null, null);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public String getPassport() {
        return passport;
    }

    public Set<String> getAccounts() {
        return accounts.keySet();
    }

    public void addAccount(final String id, final int amount) {
        final LocalAccount account = new LocalAccount(id, amount);
        accounts.put(id, account);
    }

    public Account getAccount(final String id) {
        return accounts.get(id);
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeObject(accounts.size());
        for (final Map.Entry<String, LocalAccount> pair : accounts.entrySet()) {
            out.writeObject(pair.getKey());
            out.writeObject(pair.getValue());
        }

        out.writeObject(name);
        out.writeObject(surname);
        out.writeObject(passport);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        final int size = (int) in.readObject();
        for (int i = 0; i < size; i++) {
            final String key = (String) in.readObject();
            final LocalAccount value = (LocalAccount) in.readObject();
            accounts.put(key, value);
        }

        name = (String) in.readObject();
        surname = (String) in.readObject();
        passport = (String) in.readObject();
    }
}
