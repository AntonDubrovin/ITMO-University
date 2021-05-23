package info.kgeorgiy.ja.dubrovin.rmi;

import java.io.*;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public class LocalPerson implements Person, Externalizable {
    private String name, surname, passport;
    private final Map<String, LocalAccount> accounts;

    public LocalPerson(String name, String surname, String passport, Map<String, LocalAccount> accounts) {
        this.name = name;
        this.surname = surname;
        this.passport = passport;
        this.accounts = accounts;
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

    public Account getAccount(String account) {
        return accounts.get(account);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(accounts.size());
        for (Map.Entry<String, LocalAccount> pair : accounts.entrySet()) {
            out.writeObject(pair.getKey());
            out.writeObject(pair.getValue());
        }

        out.writeObject(name);
        out.writeObject(surname);
        out.writeObject(passport);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int size = (int) in.readObject();
        for (int i = 0; i < size; i++) {
            String key = (String) in.readObject();
            LocalAccount value = (LocalAccount) in.readObject();
            accounts.put(key, value);
        }

        name = (String) in.readObject();
        surname = (String) in.readObject();
        passport = (String) in.readObject();
    }
}
