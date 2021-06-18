package expression.operations;

import expression.TripleExpression;
import expression.type.SafeIntType;
import expression.type.Type;

public class Count <T extends Number> extends UnaryOperation<T> {
    public Count(TripleExpression<T> expression) {
        super(expression);
    }

    @Override
    protected Type<T> doOperation(Type<T> value) {
        return value.count();
    }
}
