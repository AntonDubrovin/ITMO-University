package expression.operations;

import expression.TripleExpression;
import expression.type.Type;

public class UnaryMinus <T extends Number> extends UnaryOperation <T> {
    public UnaryMinus(TripleExpression<T> expression) {
        super(expression);
    }

    @Override
    protected Type <T> doOperation(Type<T> value) {
        return value.negate();
    }
}
