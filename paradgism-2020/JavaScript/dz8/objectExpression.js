"use strict";

class Const {
    constructor(val) {
        this.val = val;
    }

    evaluate() {
        return this.val;
    }

    toString() {
        return this.val.toString();
    }

    prefix() {
        return this.val.toString();
    }

    postfix() {
        return this.val.toString();
    }

    diff() {
        return cnst0;
    }
}

const cnst0 = new Const(0);
const cnst1 = new Const(1);
const e = new Const(Math.E);

const variables = ['x', 'y', 'z'];

class Variable {
    constructor(name) {
        this.name = name;
    }

    evaluate() {
        return arguments[variables.indexOf(this.name)];
    }

    toString() {
        return this.name;
    }

    prefix() {
        return this.name;
    }

    postfix() {
        return this.name;
    }

    diff(name) {
        if (name === this.name) {
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
        if (this.exprs.length === 0) {
            return '(' + this.getSign() + ' )';
        }
        return '(' + this.exprs.reduce((str, cur) => str + ' ' + cur.prefix(), this.getSign()) + ')';
    }

    postfix() {
        return '(' + this.exprs.reduce((str, cur) => str + (str !== '' ? ' ' : '') + cur.postfix(), '') + ' ' + this.getSign() + ')';
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


class Sum extends BinaryOperation {
    constructor(...exprs) {
        super(...exprs);
    }

    doOperation(...args) {
        return args.reduce((sum, cur) => sum + cur, 0);
    }

    getSign() {
        return 'sum';
    }

    diff(name) {
        return new Sum(...this.exprs.map((cur) => cur.diff(name)));
    }
}

class Avg extends BinaryOperation {
    constructor(...exprs) {
        super(...exprs);
    }

    doOperation(...args) {
        let sum = args.reduce((sum, cur) => sum + cur, 0);
        return sum / args.length;
    }

    getSign() {
        return 'avg';
    }

    diff(name) {
        return (new Divide(new Sum(...this.exprs), new Const(this.exprs.length))).diff(name);
    }
}


const unOperations = ['cosh', 'sinh', 'negate'];

const binOperations = ['+', '-', '*', '/'];

const otherOperations = ['sum', 'avg'];

function binOperation(sign, ...exprs) {
    switch (sign) {
        case '+' :
            return new Add(...exprs);
        case '-' :
            return new Subtract(...exprs);
        case '*' :
            return new Multiply(...exprs);
        case '/' :
            return new Divide(...exprs);
        case 'log' :
            return new Log(...exprs);
        case 'pow' :
            return new Power(...exprs);
        case 'cosh' :
            return new Cosh(...exprs);
        case 'sinh' :
            return new Sinh(...exprs);
        case 'negate' :
            return new Negate(...exprs);
        case 'atan' :
            return new ArcTan(...exprs);
        case 'exp' :
            return new Exp(...exprs);
        case 'sum' :
            return new Sum(...exprs);
        case 'avg' :
            return new Avg(...exprs);
    }
}

function parse(expr) {
    let stack = [];
    expr.split(' ').map((cur) => {
        if (variables.includes(cur)) {
            stack.push(new Variable(cur));
        } else if (cur[0] >= '0' && cur[0] <= '9' || (cur[0] === '-' && cur.length > 1)) {
            stack.push(new Const(parseInt(cur)));
        } else if (binOperations.includes(cur)) {
            let expr2 = stack.pop();
            let expr1 = stack.pop();
            stack.push(binOperation(cur, expr1, expr2));
        } else if (unOperations.includes(cur)) {
            stack.push(binOperation(cur, stack.pop()));
        }
    });
    return stack.pop();
}

function parsePostfix(expr) {
    let stack = [];
    let sizes = [];
    let br = 0;
    let pos = 0;
    while (pos != expr.length) {
        skipWS();
        if (expr[pos] === '(') {
            br++;
            pos++;
            sizes.push(0);
            continue;
        }
        if (expr[pos] === ')') {
            throw SyntaxError('Invalid expression: ' + expr);
        }
        let cur = expr.slice(pos, last());
        pos = last();
        if (variables.includes(cur)) {
            sizes[sizes.length - 1]++;
            stack.push(new Variable(cur));
        } else if (cur[0] >= '0' && cur[0] <= '9' || (cur[0] === '-' && cur.length > 1)) {
            let num = parseInt(cur);
            if (isNaN(num) || num.toString().length !== cur.length) {
                throw SyntaxError('Illegal const: ' + cur);
            } else {
                sizes[sizes.length - 1]++;
                stack.push(new Const(num));
            }
        } else if (otherOperations.includes(cur)) {
            let sz = sizes.pop();
            let exprs = [];
            for (let i = 0; i < sz; i++) {
                exprs.unshift(stack.pop());
            }
            stack.push(binOperation(cur, ...exprs));
            sizes[sizes.length - 1]++;
            checkBr();
        } else if (binOperations.includes(cur)) {
            let sz = sizes.pop();
            let exprs = [];
            for (let i = 0; i < sz; i++) {
                exprs.unshift(stack.pop());
            }
            if (sz != 2) {
                throw SyntaxError('Not correct number of arguments in binary operation ' + cur);
            }
            stack.push(binOperation(cur, ...exprs));
            sizes[sizes.length - 1]++;
            checkBr();
        } else if (unOperations.includes(cur)) {
            let sz = sizes.pop();
            let exprs = [];
            for (let i = 0; i < sz; i++) {
                exprs.unshift(stack.pop());
            }
            if (sz != 1) {
                throw SyntaxError('Not correct number of arguments in unary operation ' + cur);
            }
            stack.push(binOperation(cur, ...exprs));
            sizes[sizes.length - 1]++;
            checkBr();
        } else if (cur !== '') {
            throw SyntaxError('Invalid expression: ' + expr);
        }
    }
    if (br < 0) {
        throw SyntaxError('Missing open bracket');
    } else if (br > 0) {
        throw SyntaxError('Missing close bracket');
    } else if (stack.length !== 1) {
        throw SyntaxError('Illegal expression: ' + expr);
    } else {
        return stack.pop();
    }

    function skipWS() {
        while (expr[pos] === ' ') {
            pos = (pos === expr.length ? -1 : pos + 1);
        }
    }

    function checkBr() {
        skipWS();
        if (expr[pos] === ')') {
            br--;
            if (br < 0) {
                throw SyntaxError('Missing open bracket');
            }
            pos++;
        }
    }

    function find(sym) {
        let last = expr.indexOf(sym, pos);
        last = last === -1 ? expr.length : last;
        return last;
    }

    function last() {
        return Math.min(find('('), find(')'), find(' '));
    }
}

function parsePrefix(expr) {
    let br = 0;
    let pos = 0;
    let wait = -1; // 0 - operand, 1 - argument
    let res = parseOperation();
    checkBr();
    if (br < 0) {
        throw SyntaxError('Missing open bracket');
    } else if (br > 0) {
        throw SyntaxError('Missing close bracket');
    } else if (pos < expr.length) {
        throw SyntaxError('Illegal expression: ' + expr);
    } else {
        return res;
    }

    function parseOperation() {
        skipWS();
        if (pos === -1) {
            throw SyntaxError('Waiting operation in pos: ' + pos + ' in expression: ' + expr);
        } else if (expr[pos] === '(') {
            br++;
            pos++;
            wait = 0;
            let res = parseOperation();
            checkBr();
            return res;
        } else {
            let cur = expr.slice(pos, last());
            pos = last();
            if (unOperations.includes(cur)) {
                wait = 1;
                return binOperation(cur, parseOperation());
            } else if (otherOperations.includes(cur)) {
                wait = 1;
                let exprs = [];
                skipWS();
                while (expr[pos] !== ')') {
                    let tmp = expr.slice(pos, last());
                    if (unOperations.includes(tmp) || otherOperations.includes(tmp) || binOperations.includes(tmp) || tmp === 'max5' || tmp === 'min3') {
                        throw SyntaxError('Operation ' + tmp + ' without covering in ' + cur + ' operation');
                    }
                    exprs.push(parseOperation());
                    skipWS();
                }
                return binOperation(cur, ...exprs);
            } else if (binOperations.includes(cur)) {
                wait = 1;
                return binOperation(cur, parseOperation(), parseOperation());
            }
            if (cur[0] >= '0' && cur[0] <= '9' || (cur[0] === '-' && cur.length > 1)) {
                return checkNumber(cur);
            } else if (variables.includes(cur)) {
                checkWaiting(cur);
                return new Variable(cur);
            } else {
                throw SyntaxError('Illegal operation: ' + cur + ' in expression: ' + expr);
            }
        }
    }

    function skipWS() {
        while (expr[pos] === ' ') {
            pos = (pos === expr.length ? -1 : pos + 1);
        }
    }

    function checkBr() {
        skipWS();
        if (expr[pos] === ')') {
            br--;
            if (br < 0) {
                throw SyntaxError('Missing open bracket');
            }
            pos++;
        }
    }

    function checkWaiting(cur) {
        if (wait === 0) {
            throw SyntaxError('Waiting operation instead of ' + cur + ' in ' + expr);
        }
    }

    function find(sym) {
        let last = expr.indexOf(sym, pos);
        last = last === -1 ? expr.length : last;
        return last;
    }

    function checkNumber(cur) {
        checkWaiting(cur);
        let num = parseInt(cur);
        if (isNaN(num) || num.toString().length !== cur.length) {
            throw SyntaxError('Illegal const: ' + cur);
        } else {
            return new Const(num);
        }
    }

    function last() {
        return Math.min(find('('), find(')'), find(' '));
    }
}