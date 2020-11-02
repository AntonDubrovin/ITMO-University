package expression;

import expression.exceptions.IllegalArgumentException;

public class Log extends MathOperation {
    public Log(CommonExpression expression1, CommonExpression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected Pair cover(CommonExpression expression1, CommonExpression expression2) {
        return new Pair(expression1.getPriority() >= 0 && expression1.getPriority() < 10,
                expression2.getPriority() < 10 && expression2.getPriority() >= 0);
    }

    @Override
    protected String getSign() {
        return "//";
    }

    @Override
    protected int doOperation(int value1, int value2) {
        int res = 0;
        if (value2 <= 0 || value2 == 1) {
            throw new IllegalArgumentException("Invalid second argument in log: " + value2);
        }
        if (value1 <= 0) {
            throw new IllegalArgumentException("Invalid first argument in log: " + value1);
        }
        while (value1 >= value2) {
            res++;
            value1 /= value2;
        }
        return res;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
