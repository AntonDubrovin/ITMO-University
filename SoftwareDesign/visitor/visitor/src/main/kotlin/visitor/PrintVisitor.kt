package visitor

import tokens.Brace
import tokens.NumberToken
import tokens.Operation

class PrintVisitor : TokenVisitor {
    override fun visit(token: NumberToken) {
        print("NUMBER(${token.numberValue.toString()}) ")
    }

    override fun visit(token: Brace) {
        if (token.isOpen) {
            print("OPEN ")
        } else {
            print("CLOSED ")
        }
    }

    override fun visit(token: Operation) {
        with(token) {
            when {
                isPlus -> print("PLUS ")
                isMinus -> print("MINUS ")
                isMultiply -> print("MUL ")
                isDivide -> print("DIV ")
            }
        }
    }
}