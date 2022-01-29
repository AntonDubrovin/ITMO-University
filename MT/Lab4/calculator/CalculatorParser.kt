package calculator
class Parser(private val lexer: Lexer) {
	private fun start(): Double {
		when(lexer.token) {
			TOKENS.NUM, TOKENS.LBRACKET, TOKENS.SUB -> {
				val addSubRes = addSub()
				return addSubRes
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun addSub(): Double {
		when(lexer.token) {
			TOKENS.NUM, TOKENS.LBRACKET, TOKENS.SUB -> {
				val mulDivRes = mulDiv()
				val addSubEvalRes = addSubEval(mulDivRes)
				return addSubEvalRes
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun addSubEval(curResult: Double): Double {
		when(lexer.token) {
			TOKENS.ADD -> {
				val addEvalRes = addEval(curResult)
				return addEvalRes
			}
			TOKENS.SUB -> {
				val subEvalRes = subEval(curResult)
				return subEvalRes
			}
			TOKENS.EOF, TOKENS.RBRACKET, TOKENS.ADD, TOKENS.SUB, TOKENS.MUL, TOKENS.DIV, TOKENS.POW -> {
				return curResult
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun addEval(curResult: Double): Double {
		when(lexer.token) {
			TOKENS.ADD -> {
				val ADDRes = skip(TOKENS.ADD)
				val mulDivRes = mulDiv()
				val addSubEvalRes = addSubEval(curResult + mulDivRes)
				return addSubEvalRes
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun subEval(curResult: Double): Double {
		when(lexer.token) {
			TOKENS.SUB -> {
				val SUBRes = skip(TOKENS.SUB)
				val mulDivRes = mulDiv()
				val addSubEvalRes = addSubEval(curResult - mulDivRes)
				return addSubEvalRes
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun mulDiv(): Double {
		when(lexer.token) {
			TOKENS.NUM, TOKENS.LBRACKET, TOKENS.SUB -> {
				val powRes = pow()
				val mulDivEvalRes = mulDivEval(powRes)
				return mulDivEvalRes
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun mulDivEval(curResult: Double): Double {
		when(lexer.token) {
			TOKENS.MUL -> {
				val mulEvalRes = mulEval(curResult)
				return mulEvalRes
			}
			TOKENS.DIV -> {
				val divEvalRes = divEval(curResult)
				return divEvalRes
			}
			TOKENS.EOF, TOKENS.ADD, TOKENS.SUB, TOKENS.RBRACKET, TOKENS.MUL, TOKENS.DIV, TOKENS.POW -> {
				return curResult
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun mulEval(curResult: Double): Double {
		when(lexer.token) {
			TOKENS.MUL -> {
				val MULRes = skip(TOKENS.MUL)
				val powRes = pow()
				val mulDivEvalRes = mulDivEval(curResult * powRes)
				return mulDivEvalRes
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun divEval(curResult: Double): Double {
		when(lexer.token) {
			TOKENS.DIV -> {
				val DIVRes = skip(TOKENS.DIV)
				val powRes = pow()
				val mulDivEvalRes = mulDivEval(curResult / powRes)
				return mulDivEvalRes
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun pow(): Double {
		when(lexer.token) {
			TOKENS.NUM, TOKENS.LBRACKET, TOKENS.SUB -> {
				val numsRes = nums()
				val powEvalRes = powEval()
				return powEvalRes(numsRes)
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun powEval(): (Double) -> Double {
		when(lexer.token) {
			TOKENS.POW -> {
				val POWRes = skip(TOKENS.POW)
				val numsRes = nums()
				val powEvalRes = powEval()
				return {x->Math.pow(x,powEvalRes(numsRes))}
			}
			TOKENS.EOF, TOKENS.ADD, TOKENS.SUB, TOKENS.MUL, TOKENS.DIV, TOKENS.RBRACKET, TOKENS.POW -> {
				return {x->x}
			}
			else -> {
				throw IllegalArgumentException("Wrong token")
			}
		}
	}
	private fun nums(): Double {
		when(lexer.token) {
			TOKENS.NUM -> {
				val NUMRes = skip(TOKENS.NUM)
				return NUMRes.toDouble()
			}
			TOKENS.LBRACKET -> {
				val LBRACKETRes = skip(TOKENS.LBRACKET)
				val addSubRes = addSub()
				val RBRACKETRes = skip(TOKENS.RBRACKET)
				return addSubRes
			}
			TOKENS.SUB -> {
				val SUBRes = skip(TOKENS.SUB)
				val addSubRes = addSub()
				return -1*addSubRes
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
