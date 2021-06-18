package info.kgeorgiy.ja.dubrovin.rmi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ClientTest {
    private final String name = "Anton";
    private final String surname = "Dubrovin";
    private final String passport = "9216 020387";
    private final String accountId = "1";
    private final String amountChange = "100";

    public ClientTest() {
    }

    @Test
    public void nullArgsTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> Client.main((String) null));
    }

    @Test
    public void incorrectCountArgumentsTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> Client.main(name, surname, passport));
    }

    @Test
    public void oneNullArgumentTest() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> Client.main(null, surname, passport, accountId, amountChange));
    }

    @Test
    public void incorrectAmountTest() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> Client.main(name, surname, passport, accountId, "123qwe456-=*"));
    }
}
