package expression;

public abstract class MathOperation implements CommonExpression {
	private CommonExpression expression1, expression2;

	protected MathOperation (CommonExpression expression1, CommonExpression expression2) {
		this.expression1 = expression1;
		this.expression2 = expression2;
	}

	public String toString() {
		return "(" + expression1 + " " + getSign() + " " + expression2 + ")";
	}

	// Cover a String
	private String coveredExpression(CommonExpression expression) {
		return "("  + expression.toMiniString() + ")";
	}

	@Override
	public String toMiniString() {
		StringBuilder ans = new StringBuilder();
		Pair cover = cover(expression1, expression2);
		if ((boolean) cover.getFirst()) {
			ans.append(coveredExpression(expression1));
		} else {
			ans.append(expression1.toMiniString());
		}
		ans.append(" " + getSign() + " ");
		if ((boolean) cover.getSecond()) {
			ans.append(coveredExpression(expression2));
		} else {
			ans.append(expression2.toMiniString());
		}
		return ans.toString();
	}

	public boolean equals(Object expression) {
		if (expression == null) {
			return false;
		}
		if (this == expression) {
			return true;
		}
		if (getClass() != expression.getClass()) {
			return false;
		}
		MathOperation other = (MathOperation) expression;
		return this.expression1.equals(other.expression1) && this.expression2.equals(other.expression2);
	}

	@Override
	public int hashCode() {
		int res = 1;
		int key = 29;
		res = expression1.hashCode() + res * key;
		res = expression2.hashCode() + res * key;
		res = getClass().hashCode() + res * key;
		return res;
	}

	@Override
	public int evaluate(int value) {
		int resultExpression1 = (int) expression1.evaluate(value);
		int resultExpression2 = (int) expression2.evaluate(value);
		return doOperation(resultExpression1, resultExpression2);
	}

	@Override
	public int evaluate(int x, int y, int z) {
		int resultExpression1 = expression1.evaluate(x, y, z);
		int resultExpression2 = expression2.evaluate(x, y, z);
		return doOperation(resultExpression1, resultExpression2);
	}

	protected abstract Pair cover(CommonExpression expression1, CommonExpression expression2);
	protected abstract String getSign();
	protected abstract int doOperation(int value1, int value2);
}
