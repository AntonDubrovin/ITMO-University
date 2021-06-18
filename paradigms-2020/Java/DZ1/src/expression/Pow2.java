package expression;

import expression.exceptions.IllegalArgumentException;
import expression.exceptions.Overflow;

public class Pow2 extends UnaryOperation {

    public Pow2(CommonExpression expression) {
        super(expression);
    }
    @Override
    protected int doOperation(int value) {
        int res = 1;
        if (value < 0) {
            throw new IllegalArgumentException("Invalid argument in pow2: " + value);
        }
        int tmp;
        int x = value;
        while (x > 0) {
            tmp =  res * 2;
            if (tmp / res != 2 || tmp / 2 != res) {
                throw new Overflow("Overflow: pow2(" + value + ")");
            }
            res = tmp;
            x--;
        }
        return res;
    }

    @Override
    protected String getSign() {
        return "pow2 ";
    }
}
