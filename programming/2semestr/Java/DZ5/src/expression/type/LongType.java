package expression.type;

public class LongType extends AbstractType<Long> {
    public LongType(Long value) {
        super(value);
    }

    public static LongType parse(String s) {
        return new LongType(Long.parseLong(s));
    }

    @Override
    protected Type<Long> valueOf(Long value) {
        return new LongType(value);
    }

    @Override
    protected Long countImpl() {
        return (long) Long.bitCount(value);
    }

    @Override
    protected Long maxImpl(Long v) {
        return Long.max(value, v);
    }

    @Override
    protected Long minImpl(Long v) {
        return Long.min(value, v);
    }

    @Override
    public Long addImpl(Long v) {
        return value + v;
    }

    @Override
    public Long subtractImpl(Long v) {
        return value - v;
    }

    @Override
    public Long multiplyImpl(Long v) {
        return value * v;
    }

    @Override
    public Long divideImpl(Long v) {
        return value / v;
    }

    @Override
    public Long negateImpl() {
        return -value;
    }
}
