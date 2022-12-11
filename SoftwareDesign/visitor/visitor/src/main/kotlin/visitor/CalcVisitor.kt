package visitor

import tokens.Brace
import tokens.NumberToken
import tokens.Operation
import java.util.Stack

class CalcVisitor : TokenVisitor {
    private val stack: Stack<Double> = Stack()

    fun endCalc(): Double {
        if (stack.size != 1) {
            error("Incorrect expression")
        }
        return stack.pop()
    }

    override fun visit(token: NumberToken) {
        stack.push(token.numberValue.toDouble())
    }

    override fun visit(token: Brace) {
        error("Unexpected token brace")
    }

    override fun visit(token: Operation) {
        if (stack.size < 2) {
            error("Incorrect expression")
        }

        val x: Double = stack.pop()
        val y: Double = stack.pop()
        with(token) {
            when {
                isPlus -> stack.add(x + y)
                isMinus -> stack.add(y - x)
                isMultiply -> stack.add(x * y)
                isDivide -> stack.add(y / x)
                else -> error("Incorrect token")
            }
        }
    }
}