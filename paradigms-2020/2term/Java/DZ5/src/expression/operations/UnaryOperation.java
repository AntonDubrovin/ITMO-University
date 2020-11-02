package expression.operations;

import expression.TripleExpression;
import expression.type.Type;

public abstract class UnaryOperation <T extends Number> implements TripleExpression<T> {
    TripleExpression <T> expression;

    protected UnaryOperation(TripleExpression <T> expression) {
        this.expression = expression;
    }

    @Override
    public Type <T> evaluate(Type <T> x, Type <T> y, Type <T> z) {
        return doOperation(expression.evaluate(x, y, z));
    }

    protected abstract Type <T> doOperation(Type <T> value);
}
