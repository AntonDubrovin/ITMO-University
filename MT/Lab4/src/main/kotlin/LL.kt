fun checkLL(
    nonTerminal: List<LexicalElement.NonTerminal>,
    first: Map<String, Set<String>>,
    follow: Map<String, Set<String>>
): Boolean {
    nonTerminal.forEach {
        for (i in it.nonTerminalRules.indices) {
            for (j in it.nonTerminalRules.indices) {
                if (i != j) {
                    val firstRule = returnFirstRule(first, follow, it.nonTerminalRules[i].rulesAndArgument)
                    val secondRule = returnFirstRule(first, follow, it.nonTerminalRules[j].rulesAndArgument)
                    firstRule.forEach { f ->
                        if (secondRule.contains(f)) {
                            return false
                        }
                    }
                }
            }
        }
    }
    return true
}

fun returnFirstRule(
    first: Map<String, Set<String>>,
    follow: Map<String, Set<String>>,
    rule: List<Pair<String, String?>>
): List<String> {
    var i = 0
    val rules = mutableListOf<String>()
    do {
        if (i > 0) {
            rules.addAll(follow[rule[i - 1].first]!!)
        }
        rules.addAll(first[rule[i].first]!!)
        i++
    } while (i < rule.size && first[rule[i].first]!!.contains("EPS"))
    if (i != rule.size) {
        rules.remove("EPS")
    }
    return rules
}