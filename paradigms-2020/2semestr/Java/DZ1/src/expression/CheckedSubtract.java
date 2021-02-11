package expression;

import expression.exceptions.Overflow;
import expression.exceptions.Underflow;

public class CheckedSubtract extends MathOperation {

	public CheckedSubtract(CommonExpression expression1, CommonExpression expression2) {
		super(expression1, expression2);
	}

	@Override
	protected String getSign() {
		return "-";
	}

	@Override
	protected int doOperation(int value1, int value2) {
		if (value2 > 0 && value1 < Integer.MIN_VALUE + value2) {
			throw new Overflow("Underflow: " + value1 + " - " + value2);
		} else if (value2 < 0 && value1 > Integer.MAX_VALUE + value2) {
			throw new Underflow("Overflow: " + value1 + " - (" + value2 + ")");
		}
		return value1 - value2;

	}

	@Override
	public int getPriority() {
		return 2;
	}

	@Override
	protected Pair cover(CommonExpression expression1, CommonExpression expression2) {
		return new Pair(false, 
				expression2.getPriority() >= 2 && expression2.getPriority() < 10);
	}
}
