package info.kgeorgiy.ja.dubrovin.rmi;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Set;

public class BankTest {
    private static Bank bank;
    private static final int PORT = 8888;
    private static final String HOST = "//localhost/bank";
    private static final int COUNT_OF_PEOPLE = 100;

    @BeforeClass
    public static void beforeClass() throws Exception {
        Naming.rebind(HOST, new RemoteBank(PORT));
        bank = (Bank) Naming.lookup(HOST);

        System.out.println("Bank created");
    }

    @Test
    public void getPersonTest() throws RemoteException {
        Assert.assertNull(bank.getLocalPerson(Integer.toString(-1)));
        Assert.assertNull(bank.getRemotePerson(Integer.toString(-1)));

        for (int i = 0; i < COUNT_OF_PEOPLE; i++) {
            String name = "person";
            String surname = String.valueOf(i);
            String passport = "getPerson" + i;
            bank.createPerson(name, surname, passport);

            Person remotePerson = bank.getRemotePerson(passport);
            Assert.assertEquals(name, remotePerson.getName());
            Assert.assertEquals(surname, remotePerson.getSurname());
            Assert.assertEquals(passport, remotePerson.getPassport());

            Person localPerson = bank.getLocalPerson(passport);
            Assert.assertEquals(name, localPerson.getName());
            Assert.assertEquals(surname, localPerson.getSurname());
            Assert.assertEquals(passport, localPerson.getPassport());
        }
    }

    @Test
    public void searchAndCreatePersonTest() throws RemoteException {
        for (int i = 0; i < COUNT_OF_PEOPLE; i++) {
            String name = "searchingPerson";
            String surname = String.valueOf(i);
            String passport = "searchAndCreatePerson" + i;

            Assert.assertFalse(bank.searchPerson(name, surname, passport));
            Assert.assertTrue(bank.createPerson(name, surname, passport));
            Assert.assertTrue(bank.searchPerson(name, surname, passport));
        }
    }

    @Test
    public void getAccountsTest() throws RemoteException {
        for (int i = 0; i < COUNT_OF_PEOPLE; i++) {
            String name = "getAccounts";
            String surname = String.valueOf(i);
            String passport = "getAccounts" + i;

            Assert.assertTrue(bank.createPerson(name, surname, passport));
            Person remotePerson = bank.getRemotePerson(passport);

            Random random = new Random();
            int accountsCount = 0;
            for (int j = 0; j < random.nextInt(5); j++) {
                if (bank.createAccount(Integer.toString(j), passport, remotePerson) != null) {
                    accountsCount++;
                }
            }

            Set<String> accounts = bank.getPersonAccounts(remotePerson);
            Assert.assertNotNull(accounts);
            Assert.assertEquals(accountsCount, accounts.size());
        }
    }

    @Test
    public void creatingAccountTest() throws RemoteException {
        String name = "creatingAccount";
        String surname = "0";
        String passport = "creatingAccount0";

        bank.createPerson(name, surname, passport);
        Person localPerson = bank.getLocalPerson(passport);
        Person remotePerson = bank.getRemotePerson(passport);

        bank.createAccount("0", passport, remotePerson);
        Assert.assertNull(bank.getAccount("0", passport, localPerson));
        Assert.assertEquals(1, bank.getPersonAccounts(remotePerson).size());
        Assert.assertNotNull(bank.getAccount("0", passport, remotePerson));
        Assert.assertNotEquals(bank.getPersonAccounts(localPerson), bank.getPersonAccounts(remotePerson));
    }

    @Test
    public void remoteAfterLocalTest() throws RemoteException {
        String name = "remoteAfterLocal";
        String surname = "0";
        String passport = "remoteAfterLocal0";

        bank.createPerson(name, surname, passport);

        Person remotePerson = bank.getRemotePerson(passport);
        Assert.assertNotNull(remotePerson);
        Account remoteAccount = bank.getAccount("0", passport, remotePerson);
        Assert.assertNotNull(remoteAccount);
        Assert.assertEquals(0, remoteAccount.getAmount());

        Person localPerson = bank.getLocalPerson(passport);
        Assert.assertNotNull(localPerson);
        Account localAccount = bank.getAccount("0", passport, localPerson);
        localAccount.setAmount(10);
        Assert.assertNotNull(localAccount);
        Assert.assertEquals(10, localAccount.getAmount());
    }

    @Test
    public void localAfterRemoteTest() throws RemoteException {
        String name = "localAfterRemote";
        String surname = "0";
        String passport = "localAfterRemote0";

        bank.createPerson(name, surname, passport);
        Person remotePerson = bank.getRemotePerson(passport);

        Assert.assertNotNull(remotePerson);
        Assert.assertNotNull(bank.createAccount("0", passport, remotePerson));

        Account remoteAccount = bank.createAccount("0", passport, remotePerson);
        Assert.assertNotNull(remoteAccount);

        Person localPerson = bank.getLocalPerson(passport);
        Assert.assertNotNull(localPerson);

        remoteAccount.setAmount(100);

        Person localPerson2 = bank.getLocalPerson(passport);
        Assert.assertNotNull(localPerson2);

        Account localAccount = bank.createAccount("0", passport, localPerson);
        Assert.assertNotNull(localAccount);
        Account localAccount2 = bank.createAccount("0", passport, localPerson2);
        Assert.assertNotNull(localAccount2);

        Assert.assertEquals(localAccount.getAmount(), remoteAccount.getAmount());
        Assert.assertEquals(localAccount2.getAmount() + 100, localAccount.getAmount());
    }

    @Test
    public void localAfterLocalTest() throws RemoteException {
        String name = "localAfterLocal";
        String surname = "0";
        String passport = "localAfterLocal0";

        bank.createPerson(name, surname, passport);

        Person localPerson1 = bank.getLocalPerson(passport);
        Person localPerson2 = bank.getLocalPerson(passport);
        Assert.assertNotNull(localPerson1);
        Assert.assertNotNull(localPerson2);

        bank.createAccount("1", passport, localPerson1);
        bank.createAccount("2", passport, localPerson2);

        Person localPerson3 = bank.getLocalPerson(passport);

        Assert.assertEquals(2, bank.getPersonAccounts(localPerson3).size());
        Assert.assertEquals(0, bank.getPersonAccounts(localPerson1).size());
        Assert.assertEquals(bank.getPersonAccounts(localPerson1).size(), bank.getPersonAccounts(localPerson2).size());
    }

    @Test
    public void remoteAfterRemoteTest() throws RemoteException {
        String name = "remoteAfterRemote";
        String surname = "0";
        String passport = "remoteAfterRemote0";

        bank.createPerson(name, surname, passport);

        Person remotePerson1 = bank.getRemotePerson(passport);
        Person remotePerson2 = bank.getRemotePerson(passport);

        bank.createAccount("1", passport, remotePerson1);
        bank.createAccount("2", passport, remotePerson2);

        Assert.assertEquals(2, bank.getPersonAccounts(remotePerson1).size());
        Assert.assertEquals(bank.getPersonAccounts(remotePerson1).size(), bank.getPersonAccounts(remotePerson2).size());
    }
}
