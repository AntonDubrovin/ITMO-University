package tokenizer

import state.End
import state.Start
import state.State
import tokens.Token

class Tokenizer(input: String) {
    private var state: State
    fun tokenize(): MutableList<Token> {
        val tokens: MutableList<Token> = arrayListOf()
        while (state !is End) {
            state = state.nextState(tokens)
        }
        return tokens
    }

    init {
        state = Start(input, 0)
    }
}