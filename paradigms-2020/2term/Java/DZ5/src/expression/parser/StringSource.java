package expression.parser;

public class StringSource implements ExpressionSource {
    private final String source;
    private int index;

    public StringSource(String source) {
        index = 0;
        this.source = source;
    }

    public char next() {
        return source.charAt(index++);
    }

    public boolean hasNext() {
        return index < source.length();
    }

    public ParserException error(final String message) {
        return new ParserException(index + ": " + message);
    }
}
