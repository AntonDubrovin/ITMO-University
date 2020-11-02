package expression;

import expression.parser.ExpressionParser;

public class Main {
    public static void main(String[] args) {
        String s = "  3*  z";
        CommonExpression expression = new ExpressionParser().parse(s);
        System.out.println(expression.toString());
        System.out.println(expression.evaluate(0, 1, 2));
    }
}
