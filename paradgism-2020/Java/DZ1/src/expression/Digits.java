package expression;

public class Digits extends UnaryOperation {
    public Digits(CommonExpression expression) {
        super(expression);
    }

    @Override
    protected int doOperation(int x) {
        boolean flag = x < 0;
        int sum = 0;
        while (x != 0) {
            sum += x % 10;
            x /= 10;
        }
        return flag ? -sum : sum;
    }

    @Override
    protected String getSign() {
        return "digits ";
    }
}
