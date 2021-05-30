package info.kgeorgiy.ja.dubrovin.rmi;

public class RemotePerson implements Person {
    private final String name, surname, passport;

    public RemotePerson(final String name, final String surname, final String passport) {
        this.name = name;
        this.surname = surname;
        this.passport = passport;
    }

    @Override
    public synchronized String getName() {
        return name;
    }

    @Override
    public synchronized String getSurname() {
        return surname;
    }

    @Override
    public synchronized String getPassport() {
        return passport;
    }
}
