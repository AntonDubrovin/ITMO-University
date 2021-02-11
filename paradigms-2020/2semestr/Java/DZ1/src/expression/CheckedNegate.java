package expression;

import expression.exceptions.Overflow;

public class CheckedNegate extends UnaryOperation{
    public CheckedNegate(CommonExpression expression) {
        super(expression);
    }

    @Override
    protected int doOperation(int value) {
        if (value == Integer.MIN_VALUE) {
            throw new Overflow("Overflow");
        }
        return -value;
    }

    @Override
    protected String getSign() {
        return "-";
    }
}
