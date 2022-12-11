package visitor

import tokens.Brace
import tokens.NumberToken
import tokens.Operation
import tokens.Token
import java.util.*

class ParseVisitor : TokenVisitor {
    private val tokens: MutableList<Token> = arrayListOf()
    private val stack: Stack<Token> = Stack()

    fun endParse(): MutableList<Token> {
        while (!stack.empty()) {
            tokens.add(stack.pop())
        }
        return tokens
    }

    fun reset() {
        tokens.clear()
        stack.clear()
    }

    override fun visit(token: NumberToken) {
        tokens.add(token)
    }

    override fun visit(token: Brace) {
        if (token.isOpen) {
            stack.push(token)
            return
        }
        assert(token.isClosed)
        while (!stack.empty()) {
            val top: Token = stack.peek()
            if (top is Brace) {
                assert((top).isOpen)
                stack.pop()
                return
            }
            tokens.add(stack.pop())
        }
        error("No ) brace to ( brace")
    }

    override fun visit(token: Operation) {
        while (!stack.empty()) {
            val top: Token = stack.peek()
            if (top !is Operation || (top).priority < token.priority) {
                break
            }
            tokens.add(stack.pop())
        }
        stack.push(token)
    }
}