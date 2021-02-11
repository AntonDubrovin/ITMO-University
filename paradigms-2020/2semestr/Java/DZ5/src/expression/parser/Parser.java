package expression.parser;

import expression.TripleExpression;

public interface Parser <T extends Number> {
    TripleExpression <T> parse(String expression);
}
