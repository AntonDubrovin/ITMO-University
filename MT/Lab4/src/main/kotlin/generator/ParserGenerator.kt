package generator

import LexicalElement

class ParserGenerator {
    fun createFunction(
        nonTerminals: List<LexicalElement.NonTerminal>,
        terminals: List<LexicalElement.Terminal>,
        first: Map<String, Set<String>>,
        follow: Map<String, Set<String>>,
        path: String
    ): String {
        val file = StringBuilder()
        file.appendLine("package $path")
        file.appendLine("class Parser(private val lexer: Lexer) {")
        nonTerminals.forEach {
            file.append("\tprivate fun ${it.name}${it.arguments ?: "()"}")
            if (it.returnType != "null") file.append(": ${it.returnType}")
            file.appendLine(" {")
            file.appendLine("\t\twhen(lexer.token) {")
            it.nonTerminalRules.forEach { rule ->
                val firsts = first[rule.rulesAndArgument[0].first]
                if (rule.rulesAndArgument[0].first != "EPS") {
                    file.appendLine("\t\t\t${firsts!!.joinToString(", TOKENS.", prefix = "TOKENS.")} -> {")
                    rule.rulesAndArgument.forEach { element ->
                        if (nonTerminals.any { f -> f.name == element.first }) {
                            file.appendLine("\t\t\t\tval ${element.first}Res = ${element.first}${element.second ?: "()"}")
                        } else {
                            if (element.first != "EOF") {
                                file.appendLine("\t\t\t\tval ${element.first}Res = skip(TOKENS.${element.first})")
                            }
                        }
                    }
                } else {
                    file.appendLine("\t\t\t${follow[it.name]!!.joinToString(", TOKENS.", prefix = "TOKENS.")} -> {")
                }
                file.appendLine("\t\t\t\t${rule.returnCode ?: ""}")
                file.appendLine("\t\t\t}")
            }
            file.appendLine("\t\t\telse -> {")
            file.appendLine("\t\t\t\tthrow IllegalArgumentException(\"Wrong token\")")
            file.appendLine("\t\t\t}")
            file.appendLine("\t\t}")
            file.appendLine("\t}")
        }

        file.append(
            listOf(
                "fun parse() {",
                "\tlexer.next()",
                "\tprintln(start())",
                "\tprintln(\"All parses good :)\")",
                "}",
                "",
                "private fun skip(token: Token): String {",
                listOf(
                    "if (lexer.token != token) throw Exception(\"expectedNotFound(\$lexer, \$token)\")",
                    "val res = lexer.tokenValue ?: throw IllegalArgumentException(\"Cannot skip EOF token\")",
                    "lexer.next()",
                    "return res"
                ).joinToString(prefix = "\t", separator = "\n\t\t"),
                "}"
            ).joinToString(prefix = "\t", separator = "\n\t", postfix = "\n") + "}\n"
        )
        return file.toString()
    }
}