package generator

import LexicalElement

class FirstFollow {

    fun buildFirst(
        nonTerminals: List<LexicalElement.NonTerminal>, terminals: List<LexicalElement.Terminal>
    ): Map<String, Set<String>> {
        val first: MutableMap<String, MutableSet<String>> = mutableMapOf()
        nonTerminals.forEach { first[it.name] = mutableSetOf() }
        terminals.forEach { first[it.name] = mutableSetOf(it.name) }

        var changes: Boolean
        do {
            changes = false
            for (it in nonTerminals) {
                for (rule in it.nonTerminalRules) {
                    for (elem in rule.rulesAndArgument.indices) {
                        val curName = rule.rulesAndArgument.first().first
                        changes = changes || first[it.name]!!.addAll(first[curName]!!)

                        if (first[curName]!!.contains("EPS")) {
                            if (rule.rulesAndArgument.count() - 1 == elem) {
                                changes = changes || first[it.name]!!.add("EPS")
                            }
                        } else {
                            break
                        }
                    }
                }
            }
        } while (changes)
        return first
    }

    fun buildFollow(
        first: Map<String, Set<String>>,
        nonTerminals: List<LexicalElement.NonTerminal>,
        terminals: List<LexicalElement.Terminal>
    ): Map<String, Set<String>> {
        val follow: MutableMap<String, MutableSet<String>> = mutableMapOf()
        nonTerminals.forEach { follow[it.name] = mutableSetOf() }
        terminals.forEach { follow[it.name] = mutableSetOf() }

        follow[nonTerminals.first().name]!!.add("EOF")
        var changes: Boolean
        do {
            changes = false
            for (it in nonTerminals) {
                var eps = true
                for (rule in it.nonTerminalRules) {
                    for (elem in rule.rulesAndArgument.indices.reversed()) {
                        val curName = rule.rulesAndArgument[elem].first
                        if (rule.rulesAndArgument.count() - 1 == elem) {
                            changes = changes || follow[curName]!!.addAll(follow[it.name]!!)
                            continue
                        }

                        val nextName = rule.rulesAndArgument[elem + 1].first
                        eps = eps && first[nextName]!!.contains("EPS")

                        if (eps) {
                            changes = changes || follow[curName]!!.addAll(follow[it.name]!!)
                            changes = changes || follow[curName]!!.addAll(first[nextName]!!.filter { x -> x != "EPS" })
                        } else {
                            changes = changes || follow[curName]!!.addAll(first[nextName]!!)
                        }
                    }
                }
            }
        } while (changes)

        return follow.filterKeys { x -> nonTerminals.any { f -> f.name == x } }
    }
}