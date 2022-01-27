package generator

class TokensGenerator {
    fun createTokens(terminals: List<LexicalElement.Terminal>, path: String): String {
        val patterns = StringBuilder()
        val tokens = StringBuilder()

        patterns.appendLine("val patterns: Map<Token, Regex> = mapOf(")
        tokens.appendLine("object TOKENS {")

        patterns.appendLine(terminals.filter { x -> x.name != "EPS" }.mapIndexed { index, terminal ->
            "$index to Regex(\"" + terminal.regex.toString().replace("\\", "\\\\") + "\")"
        }.joinToString(separator = ",\n\t", prefix = "\t"))

        tokens.appendLine(terminals.filter { x -> x.name != "EPS" }
            .mapIndexed { index, terminal -> "val " + terminal.name + " = $index" }
            .joinToString(separator = "\n\t", prefix = "\t"))

        patterns.appendLine(")")
        tokens.appendLine("}")

        return "package $path \n$patterns\n$tokens"
    }
}