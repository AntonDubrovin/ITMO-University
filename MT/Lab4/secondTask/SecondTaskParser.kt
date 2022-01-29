package secondTask
class Parser(private val lexer: Lexer) {
	private fun start() {
		when(lexer.token) {
			TOKENS.LETTER, TOKENS.LBRACKET, TOKENS.NOT -> {
				val eRes = e()
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun e() {
		when(lexer.token) {
			TOKENS.LETTER, TOKENS.LBRACKET, TOKENS.NOT -> {
				val tRes = t()
				val eHatchRes = eHatch()
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun eHatch() {
		when(lexer.token) {
			TOKENS.OR -> {
				val ORRes = skip(TOKENS.OR)
				val tRes = t()
				val eHatchRes = eHatch()
			}
			TOKENS.EOF, TOKENS.RBRACKET -> {
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun t() {
		when(lexer.token) {
			TOKENS.LETTER, TOKENS.LBRACKET, TOKENS.NOT -> {
				val fRes = f()
				val tHatchRes = tHatch()
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun tHatch() {
		when(lexer.token) {
			TOKENS.AND -> {
				val ANDRes = skip(TOKENS.AND)
				val fRes = f()
				val tHatchRes = tHatch()
			}
			TOKENS.EOF, TOKENS.OR, TOKENS.RBRACKET -> {
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun f() {
		when(lexer.token) {
			TOKENS.LETTER, TOKENS.LBRACKET, TOKENS.NOT -> {
				val gRes = g()
				val fHatchRes = fHatch()
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun fHatch() {
		when(lexer.token) {
			TOKENS.XOR -> {
				val XORRes = skip(TOKENS.XOR)
				val gRes = g()
				val fHatchRes = fHatch()
			}
			TOKENS.EOF, TOKENS.OR, TOKENS.AND, TOKENS.RBRACKET -> {
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun g() {
		when(lexer.token) {
			TOKENS.LETTER -> {
				val LETTERRes = skip(TOKENS.LETTER)
			}
			TOKENS.LBRACKET -> {
				val LBRACKETRes = skip(TOKENS.LBRACKET)
				val eRes = e()
				val RBRACKETRes = skip(TOKENS.RBRACKET)
			}
			TOKENS.NOT -> {
				val NOTRes = skip(TOKENS.NOT)
				val LBRACKETRes = skip(TOKENS.LBRACKET)
				val eRes = e()
				val RBRACKETRes = skip(TOKENS.RBRACKET)
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	fun parse() {
		lexer.next()
		println(start())
		println("All parses good :)")
	}
	
	private fun skip(token: Token): String {
		if (lexer.token != token) throw Exception("expectedNotFound($lexer, $token)")
		val res = lexer.tokenValue ?: throw IllegalArgumentException("Cannot skip EOF token")
		lexer.next()
		return res
	}
}
