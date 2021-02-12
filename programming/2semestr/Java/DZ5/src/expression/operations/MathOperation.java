package expression.operations;

import expression.TripleExpression;
import expression.type.Type;

public abstract class MathOperation <T extends Number> implements TripleExpression<T> {
	private TripleExpression <T> expression1, expression2;

	protected MathOperation (TripleExpression <T> expression1, TripleExpression <T> expression2) {
		this.expression1 = expression1;
		this.expression2 = expression2;
	}

	@Override
	public Type <T> evaluate(Type <T> x, Type<T> y, Type <T> z) {
		Type <T> resultExpression1 = expression1.evaluate(x, y, z);
		Type <T> resultExpression2 = expression2.evaluate(x, y, z);
		return doOperation(resultExpression1, resultExpression2);
	}

	protected abstract Type <T> doOperation(Type <T> value1, Type <T> value2);
}
