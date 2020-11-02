package expression.type;

public abstract class AbstractType <T extends Number> implements Type <T> {
    protected T value;

    public AbstractType(T value) {
        this.value = value;
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public Type<T> add(Type<T> v) {
        return valueOf(addImpl(v.value()));
    }

    @Override
    public Type<T> subtract(Type<T> v) {
        return valueOf(subtractImpl(v.value()));
    }

    @Override
    public Type<T> multiply(Type<T> v) {
        return valueOf(multiplyImpl(v.value()));
    }

    @Override
    public Type<T> divide(Type<T> v) {
        return valueOf(divideImpl(v.value()));
    }

    @Override
    public Type<T> negate() {
        return valueOf(negateImpl());
    }

    @Override
    public Type <T> count() {
        return valueOf(countImpl());
    }

    @Override
    public Type<T> max(Type<T> v) {
        return valueOf(maxImpl(v.value()));
    }

    @Override
    public Type<T> min(Type<T> v) {
        return valueOf(minImpl(v.value()));
    }

    protected abstract T negateImpl();
    protected abstract T addImpl(T value);
    protected abstract T subtractImpl(T value);
    protected abstract T multiplyImpl(T value);
    protected abstract T divideImpl(T value);
    protected abstract Type<T> valueOf(T value);
    protected abstract T countImpl();
    protected abstract T maxImpl(T v);
    protected abstract T minImpl(T v);
}