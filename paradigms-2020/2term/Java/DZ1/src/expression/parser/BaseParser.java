package expression.parser;

import expression.exceptions.FormatException;

public class BaseParser {
    private final ExpressionSource expression;
    protected char ch;

    protected BaseParser(final ExpressionSource expression) {
        this.expression = expression;
    }

    protected void nextChar() {
        ch = expression.hasNext() ? expression.next() : '\0';
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean check(char expected) {
        if (ch == expected) {
            return true;
        }
        return false;
    }

    protected void expect(final char c) {
        if (ch != c) {
            throw error("Expected '" + c + "', found '" + ch + "'");
        }
        nextChar();
    }

    protected void expect(final String value) {
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected FormatException error(final String message) {
        return expression.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
