package state

import tokens.Token

class End(input: String, pos: Int) : State(input, pos) {
    override fun nextState(tokens: MutableList<Token>): State {
        return this
    }
}