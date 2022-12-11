package tokens

import visitor.TokenVisitor

class Operation(c: Char) : Token {
    private var operationType: OperationType? = null
    val isPlus: Boolean
        get() = operationType == OperationType.PLUS
    val isMinus: Boolean
        get() = operationType == OperationType.MINUS
    val isMultiply: Boolean
        get() = operationType == OperationType.MULTIPLY
    val isDivide: Boolean
        get() = operationType == OperationType.DIVIDE
    val priority: Int
        get() = if (isPlus || isMinus) 1 else 2

    init {
        operationType = when (c) {
            '+' -> OperationType.PLUS
            '-' -> OperationType.MINUS
            '*' -> OperationType.MULTIPLY
            '/' -> OperationType.DIVIDE
            else -> error("Invalid character in Operation token $c")
        }
    }

    override fun accept(visitor: TokenVisitor?) {
        visitor?.visit(this)
    }
}