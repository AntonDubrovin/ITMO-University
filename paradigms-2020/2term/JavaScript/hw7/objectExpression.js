"use strict";


class Const {
    constructor(value) {
        this.value = value;
    }

    toString() {
        return '' + this.value;
    }

    evaluate() {
        return this.value;
    }

    diff(name) {
        return cnst0;
    }
}

const cnst0 = new Const(0);
const cnst1 = new Const(1);

const VARS = ['x', 'y', 'z'];
class Variable {
    constructor(name) {
        this.name = name;
    }

    toString() {
        return this.name;
    }

    evaluate() {
        let ind = VARS.indexOf(this.name);
        return arguments[ind];
    }

    diff(name) {
        if (this.name === name) {
            return cnst1;
        } else {
            return cnst0;
        }
    }
}

class BinaryOperation {
    constructor(...exprs) {
        this.exprs = exprs;
    }

    evaluate(...args) {
        const result = this.exprs.map(function (expr) {
            return expr.evaluate(...args);
        });
        return this.doOperation(...result);
    }

    toString() {
        return this.exprs.join(' ') + ' ' + this.getSign();
    }

    prefix() {
        return '(' + this.exprs.reduce((str, cur) => str + ' ' + cur.prefix(), this.getSign()) + ')';
    }
}

class Add extends BinaryOperation {
    constructor(...exprs) {
        super(...exprs);
    }

    doOperation(expr1, expr2) {
        return expr1 + expr2;
    }

    getSign() {
        return '+';
    }

    diff(name) {
        return new Add(
            this.exprs[0].diff(name),
            this.exprs[1].diff(name)
        );
    }
}

class Multiply extends BinaryOperation {
    constructor(...exprs) {
        super(...exprs);
    }

    doOperation(expr1, expr2) {
        return expr1 * expr2;
    }

    getSign() {
        return '*';
    }

    diff(name) {
        return new Add(
            new Multiply(this.exprs[0].diff(name), this.exprs[1]),
            new Multiply(this.exprs[0], this.exprs[1].diff(name))
        );
    }
}

class Subtract extends BinaryOperation {
    constructor(...exprs) {
        super(...exprs);
    }

    doOperation(expr1, expr2) {
        return expr1 - expr2;
    }

    getSign() {
        return '-';
    }

    diff(name) {
        return new Subtract(
            this.exprs[0].diff(name),
            this.exprs[1].diff(name)
        );
    }
}

class Divide extends BinaryOperation {
    constructor(...exprs) {
        super(...exprs);
    }

    doOperation(expr1, expr2) {
        return expr1 / expr2;
    }

    getSign() {
        return '/';
    }

    diff(name) {
        return new Divide(
            new Subtract(
                new Multiply(
                    this.exprs[0].diff(name),
                    this.exprs[1]
                ),
                new Multiply(
                    this.exprs[0],
                    this.exprs[1].diff(name)
                )
            ),
            new Multiply(
                this.exprs[1],
                this.exprs[1]
            )
        );
    }
}

class Negate extends BinaryOperation {
    constructor(...exprs) {
        super(...exprs);
    }

    doOperation(expr) {
        return -expr;
    }

    getSign() {
        return 'negate';
    }

    diff(name) {
        return new Negate(this.exprs[0].diff(name));
    }
}

class Sinh extends BinaryOperation {
    constructor(...exprs) {
        super(...exprs);
    }

    doOperation(expr) {
        return Math.sinh(expr);
    }

    getSign() {
        return 'sinh';
    }

    diff(name) {
        return new Multiply(new Cosh(this.exprs[0]), this.exprs[0].diff(name));
    }
}

class Cosh extends BinaryOperation {
    constructor(...exprs) {
        super(...exprs);
    }

    doOperation(expr) {
        return Math.cosh(expr);
    }

    getSign() {
        return 'cosh';
    }

    diff(name) {
        return new Multiply(new Sinh(this.exprs[0]), this.exprs[0].diff(name));
    }
}

function parse(expr) {
    let stack = [];
    const unaryOperations = ['negate'];
    const binaryOperations = ['+', '-', '*', '/'];
    const hyperbolic = ['sinh', 'cosh'];
    expr.split(' ').map((curToken) => {
        if (binaryOperations.includes(curToken)) {
            let rightPart = stack.pop();
            let leftPart = stack.pop();
            switch (curToken) {
                case '+' :
                    stack.push(new Add(leftPart, rightPart));
                    break;
                case '-' :
                    stack.push(new Subtract(leftPart, rightPart));
                    break;
                case '*' :
                    stack.push(new Multiply(leftPart, rightPart));
                    break;
                case '/' :
                    stack.push(new Divide(leftPart, rightPart));
                    break;
            }
        } else if (curToken[0] >= '0' && curToken[0] <= '9' || (curToken[0] === '-' && curToken.length > 1)) {
            stack.push(new Const(parseInt(curToken)));
        } else if (VARS.includes(curToken)) {
            stack.push(new Variable(curToken));
        } else if (unaryOperations.includes(curToken)) {
            let unaryArgument = stack.pop();
            stack.push(new Negate(unaryArgument));
        } else if (hyperbolic.includes(curToken)) {
            switch (curToken) {
                case 'sinh':
                    stack.push(new Sinh(stack.pop()));
                    break;
                case 'cosh':
                    stack.push(new Cosh(stack.pop()));
                    break;
            }
        }
    });
    return stack.pop();
}