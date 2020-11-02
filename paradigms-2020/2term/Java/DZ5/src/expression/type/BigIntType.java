package expression.type;

import java.math.BigInteger;

public class BigIntType extends AbstractType<BigInteger> {
    public BigIntType(BigInteger value) {
        super(value);
    }

    public static Type<BigInteger> parse(String s) {
        return new BigIntType(new BigInteger(s));
    }

    @Override
    protected Type<BigInteger> valueOf(BigInteger value) {
        return new BigIntType(value);
    }

    @Override
    protected BigInteger countImpl() {
        return BigInteger.valueOf(value.bitCount());
    }

    @Override
    protected BigInteger maxImpl(BigInteger v) {
        return value.max(v);
    }

    @Override
    protected BigInteger minImpl(BigInteger v) {
        return value.min(v);
    }

    @Override
    public BigInteger addImpl(BigInteger v) {
        return value.add(v);
    }

    @Override
    public BigInteger subtractImpl(BigInteger v) {
        return value.subtract(v);
    }

    @Override
    public BigInteger multiplyImpl(BigInteger v) {
        return value.multiply(v);
    }

    @Override
    public BigInteger divideImpl(BigInteger v) {
        return value.divide(v);
    }

    @Override
    public BigInteger negateImpl() {
        return value.negate();
    }
}