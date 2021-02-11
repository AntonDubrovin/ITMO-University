package expression.parser;

import expression.CommonExpression;

public interface Parser {
    CommonExpression parse(String expression);
}
