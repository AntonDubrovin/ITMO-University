package expression.type;

public class ShortType extends AbstractType<Short> {
    public ShortType(Short value) {
        super(value);
    }

    public static ShortType parse(String s) {
        return new ShortType((short)Integer.parseInt(s));
    }

    @Override
    protected Type<Short> valueOf(Short value) {
        return new ShortType(value);
    }

    @Override
    protected Short countImpl() {
        return (short) Integer.bitCount(value & 0xffff);
    }

    @Override
    protected Short maxImpl(Short v) {
        return (short) Math.max(value, v);
    }

    @Override
    protected Short minImpl(Short v) {
        return (short) Math.min(value, v);
    }

    @Override
    public Short addImpl(Short v) {
        return (short) (value + v);
    }

    @Override
    public Short subtractImpl(Short v) {
        return (short) (value - v);
    }

    @Override
    public Short multiplyImpl(Short v) {
        return (short) (value * v);
    }

    @Override
    public Short divideImpl(Short v) {
        return (short) (value / v);
    }

    @Override
    public Short negateImpl() {
        return (short) (-value);
    }
}
