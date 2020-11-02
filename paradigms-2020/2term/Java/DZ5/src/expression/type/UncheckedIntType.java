package expression.type;

public class UncheckedIntType extends AbstractType<Integer> {
    public UncheckedIntType(Integer v) {
        super(v);
    }

    public static Type<Integer> parse(String s) {
        return new UncheckedIntType(Integer.parseInt(s));
    }

    @Override
    protected Type<Integer> valueOf(Integer value) {
        return new UncheckedIntType(value);
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
    protected Integer addImpl(Integer v) {
        return value + v;
    }

    @Override
    public Integer subtractImpl(Integer v) {
        return value - v;
    }

    @Override
    public Integer multiplyImpl(Integer v) {
        return value * v;
    }

    @Override
    public Integer divideImpl(Integer v) {
        return value / v;
    }

    @Override
    public Integer negateImpl() {
        return -value;
    }

}
