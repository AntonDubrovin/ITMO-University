package expression.type;

import expression.exceptions.DivisionByZero;
import expression.exceptions.Overflow;
import expression.exceptions.Underflow;

public class SafeIntType extends UncheckedIntType {
    public SafeIntType(Integer v) {
        super(v);
    }

    public static Type<Integer> parse(String s) {
        return new SafeIntType(Integer.parseInt(s));
    }

    @Override
    protected Type<Integer> valueOf(Integer value) {
        return new SafeIntType(value);
    }

    @Override
    protected Integer countImpl() {
        return Integer.bitCount(value);
    }

    @Override
    protected Integer maxImpl(Integer v) {
        return Integer.max(value, v);
    }

    @Override
    protected Integer minImpl(Integer v) {
        return Integer.min(value, v);
    }

    @Override
    protected Integer addImpl(Integer v) throws ArithmeticException {
        if (v > 0 && Integer.MAX_VALUE - v < value) {
            throw new Overflow("Overflow: " + value + " + " + v);
        }
        if(v < 0 && Integer.MIN_VALUE - v > value) {
            throw new Underflow("Underflow: " + value + " + " + v);
        }
        return value + v;
    }

    @Override
    public Integer subtractImpl(Integer v) {
        if (v < 0 && Integer.MAX_VALUE + v < value) {
            throw new Overflow("Overflow: " + value + " - " + v);
        }
        if (v > 0 && Integer.MIN_VALUE + v > value) {
            throw new Underflow("Underflow: " + value + " - " + v);
        }
        return value - v;
    }

    @Override
    public Integer multiplyImpl(Integer v) {
        if (value > 0 && v > 0 && Integer.MAX_VALUE / v < value ||
                value < 0 && v < 0 && Integer.MAX_VALUE / v > value) {
            throw new Overflow("Overflow: " + value + " * " + v);
        }
        if (value > 0 && v < 0 && Integer.MIN_VALUE / value > v ||
                value < 0 && v > 0 && Integer.MIN_VALUE / v > value) {
            throw new Underflow("Underflow: " + value + " * " + v);
        }
        return value * v;
    }

    @Override
    public Integer divideImpl(Integer v) {
        if (value == Integer.MIN_VALUE && v == -1) {
            throw new Overflow("Overflow: " + Integer.MIN_VALUE + " / " + "(-1)");
        } else if (v == 0) {
            throw new DivisionByZero("Division by zero");
        }
        return value / v;
    }

    @Override
    public Integer negateImpl() {
        if (value == Integer.MIN_VALUE) {
            throw new Overflow("Overflow: -" + value);
        }
        return -value;
    }

}
