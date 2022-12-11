package state

import tokens.Token

class Space(input: String, pos: Int): State(input, pos) {
    override fun nextState(tokens: MutableList<Token>): State {
        if (pos >= input.length) {
            return End(input, pos)
        }
        val c: Char = input[pos]
        if (Character.isWhitespace(c)) {
            pos++
            return this
        }
        return if (Character.isDigit(c)) {
            Number(input, pos)
        } else Start(input, pos)
    }
}