package expression.parser;

import expression.TripleExpression;
import expression.operations.*;
import expression.primary.*;
import expression.type.Type;

import java.util.function.Function;

public class ExpressionParser <T extends Number> implements Parser <T> {
    private ExpressionSource source;
    private Function<String, Type> typeParser;
    private char ch;

    public ExpressionParser(Function<String, Type> typeParser) {
        this.typeParser = typeParser;
    }

    private void nextChar() {
        if (source.hasNext()) {
            ch = source.next();
        } else {
            ch = '\0';
        }
    }

    private void skipAllWhiteSpace() {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
    }

    public TripleExpression<T> parse(String expression) {
        source = new StringSource(expression);
        nextChar();
        skipAllWhiteSpace();
        return parseMax();
    }

    private boolean check(char c) {
        skipAllWhiteSpace();
        if (ch == c) {
            nextChar();
            return true;
        }
        return false;
    }

    private TripleExpression <T> parseMax() {
        TripleExpression <T> result = parseAdd();
        while (true) {
            skipAllWhiteSpace();
            if (between('a', 'z')) {
                String var = parseVariable();
                if (var.equals("max")) {
                    result =  new Maximum(result, parseAdd());
                } else if (var.equals("min")) {
                    result = new Minimum(result, parseAdd());
                } else {
                    throw error("Illegal operation");
                }
            } else if (!source.hasNext() || check(')')){
                return result;
            }
        }
    }
    
    private TripleExpression <T> parseAdd() {
        TripleExpression <T> result = parseMultiply();
        skipAllWhiteSpace();
        while (true) {
            if (check('+')) {
                result =  new Add(result, parseMultiply());
            } else if (check('-')) {
                result = new Subtract(result, parseMultiply());
            } else { ;
                return result;
            }
        }
    }

    private TripleExpression <T> parseMultiply() {
        TripleExpression <T> result = parsePrimary();
        skipAllWhiteSpace();
        while (true) {
            if (check('*')) {
                result = new Multiply(result, parsePrimary());
            } else if (check('/')) {
                result = new Divide(result, parsePrimary());
            } else {
                return result;
            }
        }
    }

    private boolean between(char a, char b) {
        return (ch >= a && ch <= b);
    }

    private Const parseConst(String prefix) {
        StringBuilder num = new StringBuilder(prefix);
        while (Character.isDigit(ch)) {
            num.append(ch);
            nextChar();
        }
        return new Const<>(typeParser.apply(num.toString()));
    }

    private String parseVariable() {
        skipAllWhiteSpace();
        StringBuilder var = new StringBuilder();
        while (between('a', 'z')) {
            var.append(ch);
            nextChar();
        }
        return var.toString();
    }

    private TripleExpression <T> parsePrimary() {
        skipAllWhiteSpace();
        if (check('(')) {
            return parseMax();
        } else if (check('-')) {
            return Character.isDigit(ch) ? parseConst("-") : new UnaryMinus(parsePrimary());
        } else if (Character.isDigit(ch)) {
            return parseConst("");
        } else if (between('a', 'z')) {
            String var = parseVariable();
            if (var.equals("count")) {
                return new Count(parsePrimary());
            }
            return new Variable(var);
        } else {
            throw error("Invalid input");
        }
    }

    private ParserException error(final String message) {
        return source.error(message);
    }
}
