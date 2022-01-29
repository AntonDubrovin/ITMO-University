package calculator 
val patterns: Map<Token, Regex> = mapOf(
	0 to Regex("[0-9]+"),
	1 to Regex("\\Q(\\E"),
	2 to Regex("\\Q)\\E"),
	3 to Regex("\\Q**\\E"),
	4 to Regex("\\Q*\\E"),
	5 to Regex("\\Q/\\E"),
	6 to Regex("\\Q+\\E"),
	7 to Regex("\\Q-\\E"),
	8 to Regex("\\QEOF\\E")
)

object TOKENS {
	val NUM = 0
	val LBRACKET = 1
	val RBRACKET = 2
	val POW = 3
	val MUL = 4
	val DIV = 5
	val ADD = 6
	val SUB = 7
	val EOF = 8
}
