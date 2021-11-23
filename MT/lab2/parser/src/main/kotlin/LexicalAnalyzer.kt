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
        var i = 0
        while (currentChar in "a".."z") {
            i++
            try {
                currentChar = inputStream.read().toChar().toString()
                currentPosition++
            } catch (e: IOException) {
                throw ParseException(e.message, currentPosition)
            }
        }
        return i > 1
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
                previousChar = "o"
                currentToken = if (nextString()) {
                    Token.OR
                } else {
                    Token.LETTER
                }
            }
            "a" -> {
                currentToken = if (nextString()) {
                    Token.AND
                } else {
                    Token.LETTER
                }
                previousChar = "a"
            }
            "x" -> {
                currentToken = if (nextString()) {
                    Token.XOR
                } else {
                    Token.LETTER
                }
                previousChar = "x"
            }
            "n" -> {
                currentToken = if (nextString()) {
                    Token.NOT
                } else {
                    Token.LETTER
                }
                previousChar = "n"
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