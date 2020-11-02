package expression;

public class Reverse extends UnaryOperation {
    public Reverse(CommonExpression expression) {
        super(expression);
    }

    @Override
    protected int doOperation(int x) {
        int ans = 0;
        while (x != 0) {
            ans = ans * 10 + (x % 10);
            x /= 10;
        }
        return ans;
    }

    @Override
    protected String getSign() {
        return "reverse ";
    }
}
