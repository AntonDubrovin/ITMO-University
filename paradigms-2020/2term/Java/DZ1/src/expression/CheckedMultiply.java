package expression;

import expression.exceptions.Overflow;
import expression.exceptions.Underflow;

public class CheckedMultiply extends MathOperation {

	public CheckedMultiply(CommonExpression expression1, CommonExpression expression2) {
		super(expression1, expression2);
	}

	@Override
	protected String getSign() {
		return "*";
	}

	@Override
	protected int doOperation(int value1, int value2) {
		int res = value1 * value2;
		if (value1 != 0 && res / value1 != value2 || value2 != 0 &&  res / value2 != value1) {
			if (value1 > 0 && value2 > 0 || value1 < 0 && value2 < 0) {
				throw new Overflow("Overflow: " + value1 + " * " + value2);
			} else {
				throw new Underflow("Underflow: " + value1 + " * " + value2);
			}
		}
		return res;
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	protected Pair cover(CommonExpression expression1, CommonExpression expression2) {
		return new Pair(expression1.getPriority() > 1 && expression1.getPriority() < 10,
				expression2.getPriority() < 10 && expression2.getPriority() != 1);
	}
}
