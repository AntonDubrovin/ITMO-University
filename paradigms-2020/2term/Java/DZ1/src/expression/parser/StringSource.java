package expression.parser;

import expression.exceptions.FormatException;

public class StringSource implements ExpressionSource {
    private final String source;
    private int pos;

    public StringSource(String source) {
        pos = 0;
        this.source = source;
    }

    public char next() {
        return source.charAt(pos++);
    }

    public boolean hasPrev() {
        return pos > 0;
    }

    public char prev() {
        return source.charAt(--pos);
    }

    public boolean hasNext() {
        return pos < source.length();
    }

    public int getPos() {
        return pos; // Count from 1
    }

    public FormatException error(final String message) {
        return new FormatException(pos + ": " + message);
    }
}
