package expression.exceptions;

public class DivisionByZero extends ArithmeticException {
    public DivisionByZero(String message) {
        super(message);
    }
}
