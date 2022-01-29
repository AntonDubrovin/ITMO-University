import antlr.grammar.GrammarBaseListener
import antlr.grammar.GrammarParser
import LexicalElement.Rule

class GrammarWalker : GrammarBaseListener() {
    var terminals: MutableList<LexicalElement.Terminal> = ArrayList()
    var nonTerminals: MutableList<LexicalElement.NonTerminal> = ArrayList()
    private var currentNoneTerminal: LexicalElement.NonTerminal =
        LexicalElement.NonTerminal("", mutableListOf(), "", "")
    private var currentRules: MutableList<Pair<String, String?>> = mutableListOf()
    private var currentRule: MutableList<Rule> = mutableListOf()

    override fun exitTerminal(ctx: GrammarParser.TerminalContext) {
        val name = ctx.WORD().symbol.text
        if (name.uppercase() != name) {
            throw IllegalArgumentException("Wrong terminal name")
        }

        when {
            ctx.STRING() != null -> {
                val string = ctx.STRING().symbol.text
                terminals.add(
                    LexicalElement.Terminal(
                        name, Regex(Regex.Companion.escape(string.substring(1, string.length - 1)))
                    )
                )
            }
            ctx.REGEX() != null -> {
                val rx = ctx.REGEX()
                terminals.add(LexicalElement.Terminal(name, Regex(rx.text)))
            }
            ctx.OPERATIONS() != null -> {
                val operation = ctx.OPERATIONS()
                terminals.add(LexicalElement.Terminal(name, Regex(Regex.Companion.escape(operation.toString()))))
            }
            else -> throw IllegalArgumentException("Wrong terminal value")
        }
    }

    override fun exitNonTerminal(ctx: GrammarParser.NonTerminalContext) {
        val name = ctx.WORD().symbol.text
        if (name.lowercase()[0] != name[0]) {
            throw IllegalArgumentException("wrong nonTerminal name")
        }
        currentNoneTerminal.name = name
        currentNoneTerminal.nonTerminalRules = currentRule.toMutableList()
        currentNoneTerminal.returnType =
            ctx.returnType()?.text?.substring(8, ctx.returnType()?.text!!.length).toString()
        currentNoneTerminal.arguments = ctx.arguments()?.text
        nonTerminals.add(currentNoneTerminal.copy())
        currentNoneTerminal = LexicalElement.NonTerminal("", mutableListOf(), "", "")
        currentRule.clear()
    }

    override fun exitRuleOnly(ctx: GrammarParser.RuleOnlyContext) {
        currentRule.add(
            Rule(
                currentRules.toList(), ctx.returnn()?.text?.substring(1, ctx.returnn()?.text?.length?.minus(1)!!)
            )
        )
        currentRules.clear()
    }

    override fun exitOneRule(ctx: GrammarParser.OneRuleContext) {
        currentRules.add(ctx.WORD().symbol.text to ctx.receivedArguments()?.children?.joinToString(""))
    }

    override fun exitStart(ctx: GrammarParser.StartContext) {
        terminals.add(LexicalElement.Terminal("EPS", Regex(Regex.Companion.escape("EPS"))))
        terminals.add(LexicalElement.Terminal("EOF", Regex(Regex.Companion.escape("EOF"))))
    }
}