package tokens

import visitor.TokenVisitor

class NumberToken(val numberValue: Int): Token {
    override fun accept(visitor: TokenVisitor?) {
        visitor?.visit(this)
    }
}