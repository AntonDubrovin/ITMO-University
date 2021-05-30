package info.kgeorgiy.ja.dubrovin.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class RemoteBank implements Bank {
    private final int port;
    private final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Set<String>> accountsByPassport = new ConcurrentHashMap<>();

    public RemoteBank(final int port) {
        this.port = port;
    }

    @Override
    public synchronized boolean createAccount(final String id, final Person person) throws RemoteException {
        final String passport = person.getPassport();
        if (accounts.containsKey(passport + ":" + id)) {
            return false;
        }

        System.out.println("Creating account with id " + id + " and passport " + passport);
        final Account account = new RemoteAccount(id);
        UnicastRemoteObject.exportObject(account, port);

        accounts.put(passport + ":" + id, account);
        if (accountsByPassport.get(person.getPassport()) == null)
            accountsByPassport.put(person.getPassport(), new ConcurrentSkipListSet<>());
        accountsByPassport.get(person.getPassport()).add(id);

        return true;
    }

    @Override
    public synchronized Account getAccount(final String id, final Person person) throws RemoteException {
        final String passport = person.getPassport();
        if (accounts.get(passport + ":" + id) == null) {
            return null;
        }
        final Account account = accounts.get(passport + ":" + id);

        System.out.println("Retrieving account with id " + id + " and passport " + passport);

        if (person instanceof LocalPerson) {
            return ((LocalPerson) person).getAccount(id);
        }
        return account;
    }

    public Map<String, LocalAccount> getLocalAccountsMap(final Person person) throws RemoteException {
        final Map<String, LocalAccount> localAccounts = new HashMap<>();
        for (final String curAccount : getPersonAccounts(person)) {
            final Account account = getAccount(curAccount, person);
            localAccounts.put(curAccount, new LocalAccount(account.getId(), account.getAmount()));
        }
        return localAccounts;
    }

    @Override
    public synchronized Person getLocalPerson(final String passport) throws RemoteException {
        if (passport == null || persons.get(passport) == null) {
            return null;
        }

        System.out.println("Retrieving person with passport " + passport);

        final Person person = persons.get(passport);
        final Map<String, LocalAccount> localAccounts = getLocalAccountsMap(person);
        return new LocalPerson(person.getName(), person.getSurname(), person.getPassport(), localAccounts);
    }

    @Override
    public synchronized Person getRemotePerson(final String passport) {
        if (passport == null) {
            return null;
        }

        System.out.println("Retrieving person with passport " + passport);
        return persons.get(passport);
    }

    @Override
    public synchronized boolean createPerson(final String name, final String surname,
                                             final String passport) throws RemoteException {
        if (name == null || surname == null || passport == null || persons.get(passport) != null) {
            System.out.println("Something went wrong with creating person with name " + name +
                    ", surname " + surname + " and passport " + passport);
            return false;
        }

        System.out.println("Creating person with name " + name + ", surname " + surname + " and passport " + passport);

        final RemotePerson person = new RemotePerson(name, surname, passport);
        persons.put(passport, person);
        accountsByPassport.put(passport, new ConcurrentSkipListSet<>());
        UnicastRemoteObject.exportObject(person, port);
        return true;
    }

    @Override
    public synchronized boolean searchPerson(final String name, final String surname, final String passport) throws RemoteException {
        if (name == null || surname == null || passport == null) {
            return false;
        }

        final Person person = persons.get(passport);
        return person != null && person.getName().equals(name) && person.getSurname().equals(surname);
    }

    @Override
    public synchronized Set<String> getPersonAccounts(final Person person) throws RemoteException {
        if (person != null) {
            if (person instanceof LocalPerson) {
                return ((LocalPerson) person).getAccounts();
            } else {
                return accountsByPassport.get(person.getPassport());
            }
        } else {
            return null;
        }
    }
}