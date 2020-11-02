package expression.type;

public class DoubleType extends AbstractType<Double> {
    public DoubleType(Double value) {
        super(value);
    }

    public static DoubleType parse(String s) {
        return new DoubleType(Double.parseDouble(s));
    }

    @Override
    protected Type<Double> valueOf(Double value) {
        return new DoubleType(value);
    }

    @Override
    protected Double countImpl() {
        return (double) Long.bitCount(Double.doubleToLongBits(value));
    }

    @Override
    protected Double maxImpl(Double v) {
        return Double.max(value, v);
    }

    @Override
    protected Double minImpl(Double v) {
        return Double.min(value, v);
    }

    @Override
    public Double addImpl(Double v) {
        return value + v;
    }

    @Override
    public Double subtractImpl(Double v) {
        return value - v;
    }

    @Override
    public Double multiplyImpl(Double v) {
        return value * v;
    }

    @Override
    public Double divideImpl(Double v) {
        return value / v;
    }

    @Override
    public Double negateImpl() {
        return -value;
    }
}
