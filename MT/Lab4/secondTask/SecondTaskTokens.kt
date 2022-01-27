package secondTask 
val patterns: Map<Token, Regex> = mapOf(
	0 to Regex("\\Q or \\E"),
	1 to Regex("\\Q and \\E"),
	2 to Regex("\\Q xor \\E"),
	3 to Regex("\\Qnot\\E"),
	4 to Regex("[a-z]"),
	5 to Regex("\\Q(\\E"),
	6 to Regex("\\Q)\\E"),
	7 to Regex("\\QEOF\\E")
)

object TOKENS {
	val OR = 0
	val AND = 1
	val XOR = 2
	val NOT = 3
	val LETTER = 4
	val LBRACKET = 5
	val RBRACKET = 6
	val EOF = 7
}
