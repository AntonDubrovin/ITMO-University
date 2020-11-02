package expression.parser;

import expression.exceptions.FormatException;

public interface ExpressionSource {
    boolean hasNext();
    char next();
    FormatException error(final String message);
    int getPos();
    char prev();
    boolean hasPrev();
}