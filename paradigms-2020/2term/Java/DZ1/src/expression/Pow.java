package expression;

import expression.exceptions.IllegalArgumentException;
import expression.exceptions.Overflow;

public class Pow extends MathOperation {
    public Pow(CommonExpression expression1, CommonExpression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected Pair cover(CommonExpression expression1, CommonExpression expression2) {
        return new Pair(expression1.getPriority() >= 0 && expression1.getPriority() < 10,
                expression2.getPriority() < 10 && expression2.getPriority() >= 0);
    }

    @Override
    protected String getSign() {
        return "**";
    }

    @Override
    protected int doOperation(int value1, int value2) {
        int res = 1;
        if (value1 == 0 && value2 == 0) {
            throw new IllegalArgumentException("Invalid arguments in pow: pow(" + value1 + ", " + value2 + ")");
        }
        if (value2 < 0) {
            throw new IllegalArgumentException("Invalid second argument in pow: " + value2);
        }
        res = binPow(value1, value2);
        return res;
    }

    protected static int binPow(int value, int degree) {
        if (degree == 0) {
            return 1;
        }
        int left, right;
        if (degree % 2 != 0) {
            left = binPow(value, degree - 1);
            right = value;
        } else {
            left = binPow(value, degree / 2);
            right = left;
        }
        int res = left * right;
        if (left != 0 && res / left != right || right != 0 && res / right != left) {
            throw new Overflow("Overflow: " + value + " ** " + degree);
        }
        return left * right;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
