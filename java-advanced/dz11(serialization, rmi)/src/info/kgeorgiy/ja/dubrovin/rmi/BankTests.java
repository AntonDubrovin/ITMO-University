package info.kgeorgiy.ja.dubrovin.rmi;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class BankTests {

    public static void main(final String[] args) {
        final Result serverTestsResult = JUnitCore.runClasses(ServerTest.class);
        if (serverTestsResult.wasSuccessful()) {
            System.out.println("SERVER TESTS PASSED");
        } else {
            System.out.println("SERVER TESTS FAILED");
            System.exit(1);
        }

        final Result clientTestsResult = JUnitCore.runClasses(ClientTest.class);
        if (clientTestsResult.wasSuccessful()) {
            System.out.println("CLIENT TESTS PASSED");
            System.exit(0);
        } else {
            System.out.println("CLIENT TESTS FAILED");
            System.exit(1);
        }
    }
}
