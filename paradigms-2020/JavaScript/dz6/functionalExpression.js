"use strict";

//Dubrovin Anton M3136
function cnst(constant) {
    return function () {
        return constant;
    }
}

const VARS = ['x', 'y', 'z'];

function variable(name) {
    let ind = VARS.indexOf(name);
    return function () {
        return arguments[ind];
    }
}

function negate(f) {
    return function () {
        return -f.apply(this, arguments);
    }
}

function pi() {
    return Math.PI;
}

function e() {
    return Math.E;
}

function unaryOperation(op) {
    return function (f) {
        return function () {
            return op(f.apply(this, arguments));
        }
    }
}

const sin = unaryOperation(val => Math.sin(val));
const cos = unaryOperation(val => Math.cos(val));

function binaryOperation(f) {
    return function () {
        let operands = arguments;
        return function () {
            let res = [];
            for (let i = 0; i < operands.length; i++) {
                res.push(operands[i].apply(this, arguments));
            }
            return f.apply(this, res);  //fixed
        }
    }
}

const add = binaryOperation((x, y) => x + y);
const subtract = binaryOperation((x, y) => x - y);
const multiply = binaryOperation((x, y) => x * y);
const divide = binaryOperation((x, y) => x / y);

function parse(expr) {
    let stack = [];
    const CONSTANTS = {
        'pi': Math.PI,
        'e': Math.E
    };
    const unaryOperations = {
        'sin': sin,
        'cos': cos,
        'negate': negate
    };
    const binaryOperations = {
        '+': add,
        '-': subtract,
        '*': multiply,
        '/': divide
    };
    expr.split(' ').map((curToken) => {  // fixed
        if (curToken in binaryOperations) {
            let rightPart = stack.pop();
            let leftPart = stack.pop();
            stack.push(binaryOperations[curToken].apply(this, [leftPart, rightPart]))
        } else if (curToken[0] >= '0' && curToken[0] <= '9' || (curToken[0] === '-' && curToken.length > 1)) {
            stack.push(cnst(parseInt(curToken)));
        } else if (VARS.includes(curToken)) {
            stack.push(variable(curToken));
        } else if (curToken in CONSTANTS) {
            stack.push(cnst(CONSTANTS[curToken]));
        } else if (curToken in unaryOperations) {
            let unaryArgument = stack.pop();
            stack.push(unaryOperations[curToken].call(this, unaryArgument));
        }
    });
    return stack.pop();
}