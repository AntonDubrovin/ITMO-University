package expression.primary;

import expression.TripleExpression;
import expression.type.Type;

public class Const <T extends Number> implements TripleExpression<T> {
	private Type <T>  value;
	
	public Const(Type<T> value) {
		this.value = value;
	}

	@Override
	public Type <T> evaluate(Type <T> x, Type <T> y, Type <T> z) {
		return this.value;
	}
}
