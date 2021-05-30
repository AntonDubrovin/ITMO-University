package info.kgeorgiy.ja.dubrovin.rmi;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class BankTests {

    public static void main(final String[] args) {
        final Result serverTestsResult = JUnitCore.runClasses(ServerTest.class);
        if (serverTestsResult.wasSuccessful()) {
            System.out.println("Server tests passed");
        } else {
            System.out.println("Server tests failed");
            System.exit(1);
        }

        final Result clientTestsResult = JUnitCore.runClasses(ClientTest.class);
        if (clientTestsResult.wasSuccessful()) {
            System.out.println("Client tests passed");
            System.exit(0);
        } else {
            System.out.println("Client tests failed");
            System.exit(1);
        }
    }
}
