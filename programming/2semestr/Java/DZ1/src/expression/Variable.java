package expression;

public class Variable implements CommonExpression {
	private String name;
	
	public Variable(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
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
		Variable other = (Variable) obj;
		return name.equals(other.name);
	}
	
	@Override
	public int evaluate(int value) {
		return value;
	}

	@Override
	public int evaluate(int x, int y, int z) {
		if (name.equals("x")) {
			return x;
		} 
		if (name.equals("y")) {
			return y;
		}
		return z;
	}

	@Override
	public int getPriority() {
		return 10;
	}
}
