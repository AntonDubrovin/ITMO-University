package visitor

import tokens.Brace
import tokens.NumberToken
import tokens.Operation

interface TokenVisitor {
    fun visit(token: NumberToken)
    fun visit(token: Brace)
    fun visit(token: Operation)
}