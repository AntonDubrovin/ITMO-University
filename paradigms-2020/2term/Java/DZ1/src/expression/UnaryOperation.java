package expression;

public abstract class UnaryOperation implements CommonExpression {
    CommonExpression expression;

    protected UnaryOperation(CommonExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return getSign() + expression.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UnaryOperation exp = (UnaryOperation) obj;
        return expression.equals(exp);
    }

    @Override
    public int evaluate(int x) {
        return doOperation(expression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return doOperation(expression.evaluate(x, y, z));
    }

    @Override
    public int getPriority() {
        return 0;
    }

    protected abstract int doOperation(int value);
    protected abstract String getSign();
}
