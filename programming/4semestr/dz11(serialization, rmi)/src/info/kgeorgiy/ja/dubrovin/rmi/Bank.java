package info.kgeorgiy.ja.dubrovin.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Bank extends Remote {
    /**
     * Creates a new account with specified identifier if it is not already exists.
     *
     * @param id account id
     * @return created or existing account.
     */
    boolean createAccount(String id, Person person) throws RemoteException;

    /**
     * Returns account by identifier.
     *
     * @param id account id
     * @return account with specified identifier or {@code null} if such account does not exists.
     */
    Account getAccount(String id, Person person) throws RemoteException;

    Person getLocalPerson(String passport) throws RemoteException;

    Person getRemotePerson(String passport) throws RemoteException;

    boolean createPerson(String name, String surname, String passport) throws RemoteException;

    boolean searchPerson(String name, String surname, String passport) throws RemoteException;

    Set<String> getPersonAccounts(Person person) throws RemoteException;
}