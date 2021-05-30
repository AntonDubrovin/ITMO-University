package info.kgeorgiy.ja.dubrovin.rmi;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Set;

@RunWith(JUnit4.class)
public class ServerTest {
    private static Bank bank;
    private static final String HOST = "//localhost/bank";
    private static final int COUNT_OF_PEOPLE = 10;
    private static final int COUNT_OF_ACCOUNTS = 5;
    private static final int PORT = 8888;

    @BeforeClass
    public static void beforeClass() throws Exception {
        bank = new RemoteBank(PORT);
        LocateRegistry.createRegistry(PORT);
        UnicastRemoteObject.exportObject(bank, PORT);
    }

    @Test
    public void getParametersEqualsTest() throws RemoteException {
        Assert.assertNull(bank.getLocalPerson(Integer.toString(-1)));
        Assert.assertNull(bank.getRemotePerson(Integer.toString(-1)));

        for (int i = 0; i < COUNT_OF_PEOPLE; i++) {
            final String testName = "getPerson";
            final String testSurname = String.valueOf(i);
            final String testPassport = "getPerson" + i;

            Assert.assertTrue(bank.createPerson(testName, testSurname, testPassport));

            final Person remotePerson = bank.getRemotePerson(testPassport);
            final Person localPerson = bank.getLocalPerson(testPassport);
            Assert.assertNotNull(remotePerson);
            Assert.assertNotNull(localPerson);

            final String remoteName = remotePerson.getName();
            final String remoteSurname = remotePerson.getSurname();
            final String remotePassport = remotePerson.getPassport();
            Assert.assertEquals(testName, remoteName);
            Assert.assertEquals(testSurname, remoteSurname);
            Assert.assertEquals(testPassport, remotePassport);

            final String localName = localPerson.getName();
            final String localSurname = localPerson.getSurname();
            final String localPassport = localPerson.getPassport();
            Assert.assertEquals(testName, localName);
            Assert.assertEquals(testSurname, localSurname);
            Assert.assertEquals(testPassport, localPassport);
        }
    }

    @Test
    public void searchAndCreatePersonTest() throws RemoteException {
        for (int i = 0; i < COUNT_OF_PEOPLE; i++) {
            final String testName = "searchingAndCreatePerson";
            final String testSurname = String.valueOf(i);
            final String testPassport = "searchAndCreatePerson" + i;

            Assert.assertFalse(bank.searchPerson(testName, testSurname, testPassport));
            Assert.assertTrue(bank.createPerson(testName, testSurname, testPassport));
            Assert.assertTrue(bank.searchPerson(testName, testSurname, testPassport));
        }
    }

    @Test
    public void accountsCountTest() throws RemoteException {
        for (int i = 0; i < COUNT_OF_PEOPLE; i++) {
            final String testName = "getAccounts";
            final String testSurname = String.valueOf(i);
            final String testPassport = "getAccounts" + i;

            Assert.assertTrue(bank.createPerson(testName, testSurname, testPassport));
            final Person remotePerson = bank.getRemotePerson(testPassport);
            Assert.assertNotNull(remotePerson);

            final Random random = new Random();
            int accountsCount = 0;
            for (int j = 0; j < random.nextInt(COUNT_OF_ACCOUNTS); j++) {
                if (bank.createAccount(Integer.toString(j), remotePerson)) {
                    accountsCount++;
                }
            }

            final Set<String> accounts = bank.getPersonAccounts(remotePerson);
            Assert.assertNotNull(accounts);
            Assert.assertEquals(accountsCount, accounts.size());
        }
    }

    @Test
    public void creatingAccountTest() throws RemoteException {
        final String testName = "creatingAccount";
        final String testSurname = "0";
        final String testPassport = "creatingAccount0";

        Assert.assertTrue(bank.createPerson(testName, testSurname, testPassport));

        final Person localPerson = bank.getLocalPerson(testPassport);
        final Person remotePerson = bank.getRemotePerson(testPassport);
        Assert.assertNotNull(localPerson);
        Assert.assertNotNull(remotePerson);

        Assert.assertTrue(bank.createAccount("firstAccount", remotePerson));
        Assert.assertNotNull(bank.getAccount("firstAccount", remotePerson));
        Assert.assertNull(bank.getAccount("firstAccount", localPerson));

        final int remoteAccountsSize = bank.getPersonAccounts(remotePerson).size();
        final int localAccountsSize = bank.getPersonAccounts(localPerson).size();
        Assert.assertEquals(1, remoteAccountsSize);
        Assert.assertNotEquals(localAccountsSize, remoteAccountsSize);
    }

    @Test
    public void remoteAndLocalAmountTest() throws RemoteException {
        final String testName = "remoteAfterLocal";
        final String testSurname = "0";
        final String testPassport = "remoteAfterLocal0";

        Assert.assertTrue(bank.createPerson(testName, testSurname, testPassport));

        final Person remotePerson = bank.getRemotePerson(testPassport);
        Assert.assertNotNull(remotePerson);

        Assert.assertTrue(bank.createAccount("secondAccount", remotePerson));
        final Account remoteAccount = bank.getAccount("secondAccount", remotePerson);
        Assert.assertNotNull(remoteAccount);

        final Person localPerson = bank.getLocalPerson(testPassport);
        Assert.assertNotNull(localPerson);
        final Account localAccount = bank.getAccount("secondAccount", localPerson);
        Assert.assertNotNull(localAccount);

        Assert.assertEquals(0, localAccount.getAmount());
        Assert.assertEquals(0, remoteAccount.getAmount());
        localAccount.setAmount(10);
        Assert.assertEquals(10, localAccount.getAmount());
        Assert.assertEquals(0, remoteAccount.getAmount());
    }

    @Test
    public void localAfterRemoteAmountTest() throws RemoteException {
        final String testName = "localAfterRemote";
        final String testSurname = "0";
        final String testPassport = "localAfterRemote0";

        Assert.assertTrue(bank.createPerson(testName, testSurname, testPassport));

        final Person remotePerson = bank.getRemotePerson(testPassport);
        Assert.assertNotNull(remotePerson);

        Assert.assertTrue(bank.createAccount("thirdAccount", remotePerson));
        final Account remoteAccount = bank.getAccount("thirdAccount", remotePerson);
        Assert.assertNotNull(remoteAccount);

        final Person firstLocalPerson = bank.getLocalPerson(testPassport);
        Assert.assertNotNull(firstLocalPerson);

        remoteAccount.setAmount(10);

        final Person secondLocalPerson = bank.getLocalPerson(testPassport);
        Assert.assertNotNull(secondLocalPerson);

        final int firstAccountAmount = bank.getAccount("thirdAccount", firstLocalPerson).getAmount();
        final int secondAccountAmount = bank.getAccount("thirdAccount", secondLocalPerson).getAmount();
        Assert.assertEquals(secondAccountAmount, remoteAccount.getAmount());
        Assert.assertEquals(firstAccountAmount + 10, secondAccountAmount);
    }

    @Test
    public void localAccountsTest() throws RemoteException {
        final String testName = "localAfterLocal";
        final String testSurname = "0";
        final String testPassport = "localAfterLocal0";

        bank.createPerson(testName, testSurname, testPassport);

        final Person localPerson1 = bank.getLocalPerson(testPassport);
        final Person localPerson2 = bank.getLocalPerson(testPassport);
        Assert.assertNotNull(localPerson1);
        Assert.assertNotNull(localPerson2);

        Assert.assertTrue(bank.createAccount("fourthAccount1", localPerson1));
        Assert.assertTrue(bank.createAccount("fourthAccount2", localPerson2));

        final Person localPerson3 = bank.getLocalPerson(testPassport);
        Assert.assertNotNull(localPerson3);

        Assert.assertEquals(2, bank.getPersonAccounts(localPerson3).size());
        Assert.assertEquals(0, bank.getPersonAccounts(localPerson1).size());
        Assert.assertEquals(bank.getPersonAccounts(localPerson1).size(), bank.getPersonAccounts(localPerson2).size());
    }

    @Test
    public void remoteAccountsTest() throws RemoteException {
        final String testName = "remoteAfterRemote";
        final String testSurname = "0";
        final String testPassport = "remoteAfterRemote0";

        bank.createPerson(testName, testSurname, testPassport);

        final Person firstRemotePerson = bank.getRemotePerson(testPassport);
        final Person secondRemotePerson = bank.getRemotePerson(testPassport);
        Assert.assertNotNull(firstRemotePerson);
        Assert.assertNotNull(secondRemotePerson);

        Assert.assertTrue(bank.createAccount("fifthAccount1", firstRemotePerson));
        Assert.assertTrue(bank.createAccount("fifthAccount2", secondRemotePerson));

        final int firstRemotePersonSize = bank.getPersonAccounts(firstRemotePerson).size();
        final int secondRemotePersonSize = bank.getPersonAccounts(secondRemotePerson).size();
        Assert.assertEquals(2, firstRemotePersonSize);
        Assert.assertEquals(firstRemotePersonSize, secondRemotePersonSize);
    }
}
