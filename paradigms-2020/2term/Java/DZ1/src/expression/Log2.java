package expression;

import expression.exceptions.IllegalArgumentException;

public class Log2 extends UnaryOperation {
    public Log2(CommonExpression expression){
        super(expression);
    }

    @Override
    protected int doOperation(int value) {
        int res = 0;
        if (value <= 0) {
            throw new IllegalArgumentException("Invalid argument in log2: " + value);
        }
        while (value > 1) {
            res++;
            value /= 2;
        }
        return res;
    }

    @Override
    protected String getSign() {
        return "log2 ";
    }
}
