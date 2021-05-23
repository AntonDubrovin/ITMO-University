package info.kgeorgiy.ja.dubrovin.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RemoteBank implements Bank {
    private final int port;
    private final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();

    public RemoteBank(final int port) {
        this.port = port;
    }

    @Override
    public Account createAccount(final String id, final String passport) throws RemoteException {
        System.out.println("Creating account with id " + id + " and passport " + passport);
        final Account account = new RemoteAccount(id);
        if (accounts.putIfAbsent(passport + ":" + id, account) == null) {
            UnicastRemoteObject.exportObject(account, port);
            return account;
        } else {
            return getAccount(id, passport);
        }
    }

    @Override
    public Account getAccount(final String id, final String passport) {
        if (accounts.get(passport + ":" + id) == null) {
            System.out.println("No account with id " + id + " and passport " + passport);
            return null;
        }

        System.out.println("Retrieving account with id " + id + " and passport " + passport);
        return accounts.get(passport + ":" + id);
    }

    @Override
    public Person getLocalPerson(String passport) {
        if (passport == null) {
            return null;
        }

        System.out.println("Retrieving person with passport " + passport);

        return persons.get(passport);
    }

    @Override
    public Person getRemotePerson(String passport) {
        if (passport == null) {
            return null;
        }

        System.out.println("Retrieving person with passport " + passport);

        return persons.get(passport);
    }

    @Override
    public RemotePerson createPerson(String name, String surname, String passport) throws RemoteException {
        if (name == null || surname == null || passport == null || persons.get(passport) != null) {
            System.out.println("Something went wrong with creating person with name " + name +
                    ", surname " + surname + " and passport " + passport);
            return null;
        }

        System.out.println("Creating person with name " + name + ", surname " + surname + " and passport " + passport);

        RemotePerson person = new RemotePerson(name, surname, passport);
        persons.put(passport, person);
        UnicastRemoteObject.exportObject(person, port);
        return person;
    }
}