package state

import tokens.Brace
import tokens.Operation
import tokens.Token

class Start(input: String, pos: Int) : State(input, pos) {
    override fun nextState(tokens: MutableList<Token>): State {
        val checked: State? = checks()
        if (checked != null) {
            return checked
        }
        val c: Char = input[pos]
        if (Character.isDigit(c)) {
            return Number(input, pos)
        }
        val token = when (c) {
            '(', ')' -> Brace(c)
            '+', '-', '*', '/' -> Operation(c)
            else -> error("Incorrect character $c")
        }
        tokens.add(token)
        pos++
        return this
    }
}