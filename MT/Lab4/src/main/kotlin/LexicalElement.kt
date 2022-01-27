sealed class LexicalElement {
    abstract val name: String

    data class Terminal(override val name: String, val regex: Regex) : LexicalElement()
    data class NonTerminal(override var name: String, var nonTerminalRules: MutableList<Rule>, var returnType: String?, var arguments: String?) : LexicalElement()

    data class Rule(var rulesAndArgument: List<Pair<String, String?>>, var returnCode: String?)
}