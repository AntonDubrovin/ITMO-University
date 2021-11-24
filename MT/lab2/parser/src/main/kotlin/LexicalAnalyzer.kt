import java.io.IOException
import java.io.InputStream
import java.text.ParseException

class LexicalAnalyzer(private val inputStream: InputStream) {
    var previousChar = ""
    var currentChar = ""
        private set
    var currentPosition = 0
        private set
    var currentToken: Token = Token.START
        private set

    init {
        nextChar()
    }

    private fun nextChar() {
        currentPosition++
        try {
            currentChar = inputStream.read().toChar().toString()
        } catch (e: IOException) {
            throw ParseException(e.message, currentPosition)
        }
    }

    private fun nextString(): Boolean {
        var operation = ""
        while (currentChar in "a".."z") {
            operation += currentChar
            try {
                currentChar = inputStream.read().toChar().toString()
                currentPosition++
            } catch (e: IOException) {
                throw ParseException(e.message, currentPosition)
            }
        }
        val operations = listOf("and", "or", "xor", "not")
        return operations.contains(operation)
    }

    fun isBlank(char: String): Boolean {
        return char == " " || char == "\r" || char == "\n" || char == "\t"
    }

    fun nextToken() {
        while (isBlank(currentChar)) {
            nextChar()
        }
        when (currentChar) {
            "(" -> {
                nextChar()
                previousChar = "("
                currentToken = Token.LBRACKET
            }
            ")" -> {
                previousChar = ")"
                nextChar()
                currentToken = Token.RBRACKET
            }
            "o" -> {
                currentToken = if (nextString()) {
                    previousChar = "or"
                    Token.OR
                } else {
                    previousChar = "o"
                    Token.LETTER
                }
            }
            "a" -> {
                currentToken = if (nextString()) {
                    previousChar = "and"
                    Token.AND
                } else {
                    previousChar = "a"
                    Token.LETTER
                }
            }
            "x" -> {
                currentToken = if (nextString()) {
                    previousChar = "xor"
                    Token.XOR
                } else {
                    previousChar = "x"
                    Token.LETTER
                }
            }
            "n" -> {
                currentToken = if (nextString()) {
                    previousChar = "not"
                    Token.NOT
                } else {
                    previousChar = "n"
                    Token.LETTER
                }
            }
            in "a".."z" -> {
                previousChar = currentChar
                nextChar()
                currentToken = Token.LETTER
            }
            "￿" -> {
                currentToken = Token.END
            }
            else -> {
                throw ParseException("Illegal character $currentChar", currentPosition)
            }
        }
    }
}