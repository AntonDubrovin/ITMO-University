package info.kgeorgiy.ja.dubrovin.rmi;

import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class BankTest {
    private static Bank bank;
    private static final int PORT = 8888;
    private static final String HOST_NAME = "//localhost/bank";
    private static final int PEOPLE = 100;

    @BeforeClass
    public static void beforeClass() throws Exception {
        Naming.rebind(HOST_NAME, new RemoteBank(PORT));
        bank = (Bank) Naming.lookup(HOST_NAME);

        System.out.println("Bank created");
    }

    @Test
    public void getPersonTest() throws RemoteException {

    }
}
