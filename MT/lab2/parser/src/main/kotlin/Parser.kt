import java.lang.AssertionError

class Parser(private val lexicalAnalyzer: LexicalAnalyzer) {
    fun parse(): Tree {
        return E()
            .also {
                if (lexicalAnalyzer.currentToken != Token.END) {
                    throw AssertionError(
                        "Assertion error in the end or expression," +
                                "token = ${lexicalAnalyzer.currentToken}"
                    )
                }
            }
    }

    private fun E(): Tree {
        return when (lexicalAnalyzer.currentToken) {
            Token.LETTER, Token.LBRACKET, Token.NOT, Token.BOOLEAN -> Tree("E", T(), O())
            else -> throw AssertionError(assertionErrorMessage("E"))
        }
    }

    private fun T(): Tree {
        return when (lexicalAnalyzer.currentToken) {
            Token.LETTER, Token.LBRACKET, Token.NOT, Token.BOOLEAN -> Tree("T", F(), A())
            else -> throw AssertionError(assertionErrorMessage("T"))
        }
    }

    // equals to E' in grammar
    private fun O(): Tree {
        return when (lexicalAnalyzer.currentToken) {
            Token.OR -> {
                lexicalAnalyzer.nextToken()
                Tree("O", Tree("or"), T(), O())
            }
            Token.RBRACKET, Token.END -> Tree("") // eps
            else -> throw AssertionError(assertionErrorMessage("O"))
        }
    }

    private fun F(): Tree {
        return when (lexicalAnalyzer.currentToken) {
            Token.LETTER, Token.LBRACKET, Token.NOT, Token.BOOLEAN -> Tree("F", G(), X())
            else -> throw AssertionError(assertionErrorMessage("F"))
        }
    }

    // equals to T' in grammar
    private fun A(): Tree {
        return when (lexicalAnalyzer.currentToken) {
            Token.AND -> {
                lexicalAnalyzer.nextToken()
                Tree("A", Tree("and"), F(), A())
            }
            Token.RBRACKET, Token.OR, Token.END -> Tree("") // eps
            else -> throw AssertionError(assertionErrorMessage("A"))
        }
    }

    private fun G(): Tree {
        return when (lexicalAnalyzer.currentToken) {
            Token.LETTER, Token.BOOLEAN -> Tree(lexicalAnalyzer.previousChar)
                .also {
                    lexicalAnalyzer.nextToken()
                }
            Token.LBRACKET -> {
                lexicalAnalyzer.nextToken()
                val e = E()
                if (lexicalAnalyzer.currentToken != Token.RBRACKET) {
                    throw AssertionError(assertionErrorMessage("G"))
                }
                lexicalAnalyzer.nextToken()
                Tree("G", Tree("("), e, Tree(")"))
            }
            Token.NOT -> {
                lexicalAnalyzer.nextToken()
                Tree("G", Tree("not"), E())
            }
            else -> throw AssertionError(assertionErrorMessage("G"))
        }
    }

    // equals to F' in grammar
    private fun X(): Tree {
        return when (lexicalAnalyzer.currentToken) {
            Token.XOR -> {
                lexicalAnalyzer.nextToken()
                Tree("X", Tree("xor"), G(), X())
            }
            Token.RBRACKET, Token.AND, Token.OR, Token.END -> Tree("") // eps
            else -> throw AssertionError(assertionErrorMessage("X"))
        }
    }

    private fun assertionErrorMessage(state: String): String {
        return "Assertion error in state $state, token = ${lexicalAnalyzer.currentToken}"
    }
}
