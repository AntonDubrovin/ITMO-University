package expression.parser;

import expression.*;
import expression.exceptions.*;

import java.util.HashSet;
import java.util.Set;

public class ExpressionParser implements Parser {
    private ExpressionSource source;
    private char ch;
    private int bracketsBalance;
    private int last; // 0 - operation, 1 - const, 2 - variable
    private Set<Character> SIGNS = new HashSet<>();
    {
        SIGNS.add('+');
        SIGNS.add('-');
        SIGNS.add('*');
        SIGNS.add('/');
        SIGNS.add('>');
        SIGNS.add('<');
        SIGNS.add(')');
        SIGNS.add('(');
    }

    private void nextChar() {
        if (source.hasNext()) {
            ch = source.next();
        } else {
            ch = '\0';
        }
    }

    private void prevChar() {
        if (source.hasPrev()) {
            ch = source.prev();
        }
    }

    private void skipAllWhiteSpace() {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
    }

    public CommonExpression parse(String expression) {
        source = new StringSource(expression);
        nextChar();
        skipAllWhiteSpace();
        bracketsBalance = 0;
        last = -1;
        CommonExpression answer = parseShifts();
        if (bracketsBalance > 0) {
            throw new BracketException("Unnecessary open bracket");
        }
        return answer;
    }

    private boolean check(char c) {
        skipAllWhiteSpace();
        if (ch == c) {
            nextChar();
            return true;
        }
        return false;
    }

    private CommonExpression parseShifts() {
        CommonExpression result = parseAdd();
        while (true) {
            skipAllWhiteSpace();
            if (check('<')) {
                if (check('<')) {
                    last = 0;
                    result = new LeftShift(result, parseAdd());
                } else {
                    throw new IllegalOperationException("Invalid operation: <"  + ch + " on position: " + source.getPos());
                }
            } else if (check('>')) {
                if (check('>')) {
                    last = 0;
                    result = new RightShift(result, parseAdd());
                } else {
                    throw new IllegalOperationException("Invalid operation: >"  + ch + " on position: " + source.getPos());
                }
            } else if (!source.hasNext() || ch == ')'){
                checkBrackets();
                return result;
            } else {
                if (last == 1) {
                    throw new IllegalConstException("Invalid symbol in const: " + ch + " on position: " + source.getPos());
                } else {
                    throw new IllegalVariableException("Invalid symbol in variable: " + ch + " on position: " + source.getPos());
                }
            }
        }
    }

    private CommonExpression parseAdd() {
        CommonExpression result = parseMultiply();
        while (true) {
            skipAllWhiteSpace();
            if (check('+')) {
                last = 0;
                result =  new CheckedAdd(result, parseMultiply());
            } else if (check('-')) {
                last = 0;
                result = new CheckedSubtract(result, parseMultiply());
            } else {
                return result;
            }
        }
    }

    private CommonExpression parseMultiply() {
        CommonExpression result = parsePower();
        while (true) {
            skipAllWhiteSpace();
            if (check('*')) {
                check('*');
                last = 0;
                result = new CheckedMultiply(result, parsePower());
            } else if (check('/')) {
                check('/');
                last = 0;
                result = new CheckedDivide(result, parsePower());
            } else {
                return result;
            }
        }
    }

    private CommonExpression parsePower() {
        CommonExpression result = parsePrimary();
        while(true) {
            skipAllWhiteSpace();
            if (check('*')) {
                if (ch == '*') {
                    nextChar();
                    last = 0;
                    result = new Pow(result, parsePrimary());
                } else {
                    prevChar();
                    prevChar();
                    return result;
                }
            } else if (check('/')) {
                if (ch == '/') {
                    nextChar();
                    last = 0;
                    result = new Log(result, parsePrimary());
                } else {
                    prevChar();
                    prevChar();
                    return result;
                }
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
        while (between('0', '9')) {
            num.append(ch);
            nextChar();
        }
        return new Const(Integer.parseInt(num.toString()));
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

    private CommonExpression parsePrimary() {
        CommonExpression result;
        skipAllWhiteSpace();
        if (check('(')) {
            bracketsBalance++;
            result = parseShifts();
        } else if (check('-')) {
            last = 1;
            result =  between('0', '9') ? parseConst("-") : new CheckedNegate(parsePrimary());
        } else if (between('0', '9')) {
            last = 1;
            result =  parseConst("");
        } else if (between('a', 'z')) {
            String var = parseVariable();
            switch (var) {
                case "digits":
                    last = 0;
                    result = new Digits(parsePrimary());
                    break;
                case "reverse":
                    last = 0;
                    result = new Reverse(parsePrimary());
                    break;
                case "pow":
                    if (check('2')) {
                        if (!Character.isWhitespace(ch) && ch != '(' && ch != '-') {
                            throw new IllegalOperationException("Invalid operation's format: pow2" + ch);
                        }
                        last = 0;
                        result = new Pow2(parsePrimary());
                        break;
                    }
                    throw new IllegalOperationException("Invalid operation: " + var + ch + " on position: " + source.getPos());
                case "log":
                    if (check('2')) {
                        if (!Character.isWhitespace(ch) && ch != '(' && ch != '-') {
                            throw new IllegalOperationException("Invalid operation's format: log2" + ch);
                        }
                        last = 0;
                        result = new Log2(parsePrimary());
                        break;
                    }
                    throw new IllegalOperationException("Invalid operation: " + var + ch + " on position: " + source.getPos());
                case "x":
                case "y":
                case "z":
                    if (ch != '\0' && !Character.isWhitespace(ch) && !SIGNS.contains(ch)) {
                         throw new IllegalVariableException("Invalid symbol in variable: " + ch + " on position: " + source.getPos());
                    }
                    last = 2;
                    result = new Variable(var);
                    break;
                default:
                    if (last == 0) {
                        throw new IllegalVariableException("Invalid variable's name: " + var + " on position: " + source.getPos());
                    } else {
                        throw new IllegalOperationException("Invalid operation: " + var + ch + " on position: " + source.getPos());
                    }
            }
        } else {
            if (last == 0) {
                throw new WaitingExpressionException("Waiting argument in expression before symbol: " + ch + " on position: " + source.getPos());
            } else {
                throw new WaitingExpressionException("Waiting operation in expression before symbol: " + ch +" on position: " + source.getPos());
            }
        }
        return result;
    }

    private void checkBrackets() {
        if (check(')')) {
            bracketsBalance--;
        }
        if (bracketsBalance < 0) {
            throw new BracketException("Unnecessary close bracket on position: " + source.getPos());
        }
    }
}
