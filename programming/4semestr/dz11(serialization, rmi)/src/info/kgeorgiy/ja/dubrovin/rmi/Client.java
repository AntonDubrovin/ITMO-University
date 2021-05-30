package info.kgeorgiy.ja.dubrovin.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.text.ParsePosition;

public final class Client {
    /**
     * Utility class.
     */
    private Client() {
    }

    public static void main(final String... args) {
        if (!checkArguments(args)) {
            throw new IllegalArgumentException("Wrong arguments");
        }

        try {
            final String name = args[0];
            final String surname = args[1];
            final String passport = args[2];
            final String accountId = args[3];
            final int amountChange = Integer.parseInt(args[4]);
            final Person remotePerson = new RemotePerson(name, surname, passport);

            final Bank bank = (Bank) Naming.lookup("//localhost/bank");
            final Account account = bank.getAccount(accountId, remotePerson);
            if (account != null) {
                final int currentAmount = account.getAmount();
                System.out.println("Old balance: " + currentAmount);
                account.setAmount(currentAmount + amountChange);
                System.out.println("New balance: " + account.getAmount());
            } else {
                System.out.println("Account not found");
            }

        } catch (final MalformedURLException e) {
            System.out.println("Invalid bank url");
        } catch (final NotBoundException e) {
            System.out.println("Bank is not bound");
        } catch (final RemoteException e) {
            System.out.println("Something went wrong with connection to bank");
        }
    }

    private static boolean checkArguments(final String... args) {
        if (args == null || args.length != 5) {
            return false;
        }

        for (final String arg : args) {
            if (arg == null) {
                return false;
            }
        }

        final NumberFormat formatter = NumberFormat.getInstance();
        final ParsePosition pos = new ParsePosition(0);
        formatter.parse(args[4], pos);
        return args[4].length() == pos.getIndex();
    }
}