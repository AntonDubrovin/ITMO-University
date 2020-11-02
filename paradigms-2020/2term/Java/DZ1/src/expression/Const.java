package expression;

public class Const implements CommonExpression {
	private int  value;
	
	public Const(int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value + "";
	}

	@Override
	public int hashCode() {
		return value;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Const other = (Const) obj;
		return value == other.value;
	}
	
	@Override
	public int evaluate(int value) {
		return this.value;
	}

	@Override
	public int evaluate(int x, int y, int z) {
		return this.value;
	}

	@Override
	public int getPriority() {
		return 10;
	}
}
