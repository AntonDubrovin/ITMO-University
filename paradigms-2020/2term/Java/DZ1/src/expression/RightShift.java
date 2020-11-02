package expression;

public class RightShift extends MathOperation {

    public RightShift(CommonExpression expression1, CommonExpression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected String getSign() {
        return ">>";
    }

    @Override
    protected int doOperation(int value1, int value2) {
        int res = value1 >> value2;
        return res;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    protected Pair cover(CommonExpression expression1, CommonExpression expression2) {
        return new Pair(expression1.getPriority() > 4 && expression1.getPriority() < 10, expression1.getPriority() > 4 && expression1.getPriority() < 10);
    }

}
