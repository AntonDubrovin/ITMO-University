package expression.operations;

import expression.TripleExpression;
import expression.type.Type;

public class Minimum <T extends Number> extends MathOperation <T> {
    public Minimum(TripleExpression<T> expression1, TripleExpression<T> expression2) {
        super(expression1, expression2);
    }

    @Override
    protected Type<T> doOperation(Type<T> value1, Type<T> value2) {
        return value1.min(value2);
    }
}
