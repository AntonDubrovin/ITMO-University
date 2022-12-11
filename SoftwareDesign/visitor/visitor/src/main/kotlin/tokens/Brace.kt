package tokens

import visitor.TokenVisitor

class Brace(c: Char) : Token {
    private var braceType: BraceType? = null
    val isOpen: Boolean
        get() = braceType == BraceType.OPEN
    val isClosed: Boolean
        get() = braceType == BraceType.CLOSE

    init {
        braceType = when (c) {
            '(' -> BraceType.OPEN
            ')' -> BraceType.CLOSE
            else -> error("Invalid character in Brace token $c")
        }
    }

    override fun accept(visitor: TokenVisitor?) {
        visitor?.visit(this)
    }
}