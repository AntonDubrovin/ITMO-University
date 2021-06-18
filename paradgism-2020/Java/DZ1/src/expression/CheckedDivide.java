package expression;

import expression.exceptions.DivisionByZero;
import expression.exceptions.Overflow;

public class CheckedDivide extends MathOperation {

	public CheckedDivide(CommonExpression expression1, CommonExpression expression2) {
		super(expression1, expression2);
	}

	@Override
	protected String getSign() {
		return "/";
	}

	@Override
	protected int doOperation(int value1, int value2) {
		if (value1 == Integer.MIN_VALUE && value2 == -1) {
			throw new Overflow("Overflow: " + Integer.MIN_VALUE + " / " + "(-1)");
		} else if (value2 == 0) {
			throw new DivisionByZero("Division by zero");
		}
		return value1 / value2;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	protected Pair cover(CommonExpression expression1, CommonExpression expression2) {
		return new Pair(expression1.getPriority() > 1 && expression1.getPriority() < 10,
				expression2.getPriority() < 10);
	}
}
