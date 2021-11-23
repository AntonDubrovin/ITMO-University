import java.lang.AssertionError

class Parser(private val lexicalAnalyzer: LexicalAnalyzer) {
    fun E(): Tree {
        println("E ${lexicalAnalyzer.currentToken}")
        when (lexicalAnalyzer.currentToken) {
            Token.LETTER, Token.LBRACKET, Token.NOT -> {
                val t = T()
                val ehatch = Ehatch()
                return Tree("E", t, ehatch)
            }
            else -> {
                throw AssertionError(
                    "Assertion error in state E, token = ${lexicalAnalyzer.currentToken}, " +
                            "position = ${lexicalAnalyzer.currentPosition}"
                )
            }
        }
    }

    fun Ehatch(): Tree {
        println("E' ${lexicalAnalyzer.currentToken}")
        when (lexicalAnalyzer.currentToken) {
            Token.OR -> {
                lexicalAnalyzer.nextToken()
                val t = T()
                val ehatch = Ehatch()
                return Tree("E'", Tree("or"), t, ehatch)
            }
            Token.RBRACKET, Token.END -> {
                return Tree("eps")
            }
            else -> {
                throw AssertionError(
                    "Assertion error in state E', token = ${lexicalAnalyzer.currentToken}, " +
                            "position = ${lexicalAnalyzer.currentPosition}"
                )
            }
        }
    }

    fun T(): Tree {
        println("T ${lexicalAnalyzer.currentToken}")
        when (lexicalAnalyzer.currentToken) {
            Token.LETTER, Token.LBRACKET, Token.NOT -> {
                val f = F()
                val thatch = Thatch()
                return Tree("T", f, thatch)
            }
            else -> {
                throw AssertionError(
                    "Assertion error in state T, token = ${lexicalAnalyzer.currentToken}, " +
                            "position = ${lexicalAnalyzer.currentPosition}"
                )
            }
        }
    }

    fun Thatch(): Tree {
        println("T' ${lexicalAnalyzer.currentToken}")
        when (lexicalAnalyzer.currentToken) {
            Token.AND -> {
                lexicalAnalyzer.nextToken()
                val f = F()
                val thatch = Thatch()
                return Tree("T'", Tree("and"), f, thatch)
            }
            Token.RBRACKET, Token.OR, Token.END -> {
                return Tree("eps")
            }
            else -> {
                throw AssertionError(
                    "Assertion error in state Thatch, token = ${lexicalAnalyzer.currentToken}, " +
                            "position = ${lexicalAnalyzer.currentPosition}"
                )
            }
        }
    }

    fun F(): Tree {
        println("F ${lexicalAnalyzer.currentToken}")
        when (lexicalAnalyzer.currentToken) {
            Token.LETTER, Token.LBRACKET, Token.NOT -> {
                val g = G()
                lexicalAnalyzer.nextToken()
                val fhatch = Fhatch()
                return Tree("F", g, fhatch)
            }
            else -> {
                throw AssertionError(
                    "Assertion error in state F, token = ${lexicalAnalyzer.currentToken}, " +
                            "position = ${lexicalAnalyzer.currentPosition}"
                )
            }
        }
    }

    fun Fhatch(): Tree {
        println("F' ${lexicalAnalyzer.currentToken}")
        when (lexicalAnalyzer.currentToken) {
            Token.XOR -> {
                lexicalAnalyzer.nextToken()
                val g = G()
                lexicalAnalyzer.nextToken()
                val fhatch = Fhatch()
                return Tree("F'", Tree("xor"), g, fhatch)
            }
            Token.RBRACKET, Token.AND, Token.OR, Token.END -> {
                return Tree("eps")
            }
            else -> {
                throw AssertionError(
                    "Assertion error in state Fhatch, token = ${lexicalAnalyzer.currentToken}, " +
                            "position = ${lexicalAnalyzer.currentPosition}"
                )
            }
        }
    }

    fun G(): Tree {
        println("G ${lexicalAnalyzer.currentToken}")
        when (lexicalAnalyzer.currentToken) {
            Token.LETTER -> {
                return Tree(lexicalAnalyzer.previousChar)
            }
            Token.LBRACKET -> {
                lexicalAnalyzer.nextToken()
                val e = E()
                //println(lexicalAnalyzer.currentToken)
                if (lexicalAnalyzer.previousChar != ")") {
                    throw AssertionError(
                        "Assertion error in state G, token = ${lexicalAnalyzer.currentToken}, " +
                                "position = ${lexicalAnalyzer.currentPosition}"
                    )
                }
                return Tree("G", Tree("("), e, Tree(")"))
            }
            Token.NOT -> {
                lexicalAnalyzer.nextToken()
                println("NOT E    G, ${lexicalAnalyzer.currentToken}")
                val e = E()
                println("NOT  G, ${lexicalAnalyzer.currentToken}")
                return Tree("G", Tree("not"), e)
            }
            else -> {
                throw AssertionError(
                    "Assertion error in state G, token = ${lexicalAnalyzer.currentToken}, " +
                            "position = ${lexicalAnalyzer.currentPosition}"
                )
            }
        }
    }
}

//E  -> TE'
//E' -> or TE' | eps
//T  -> FT'
//T' -> and FT' | eps
//F  -> GF'
//F' -> xor GF' | eps
//G  -> letter | (E) | not (E)
