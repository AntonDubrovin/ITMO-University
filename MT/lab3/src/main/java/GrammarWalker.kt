import antlr.GrammarBaseListener
import antlr.GrammarParser

class GrammarWalker : GrammarBaseListener() {
    private val wordsRegex = Regex("""[a-zA-Z]+""")
    private val numberRegex = Regex("[1-9][0-9]* (('.' [0-9]*)?) | 0 | 0. [1-9]*")
    private val compareOperators = listOf(">=", "<=", ">", "<", "!=", "==")
    private val brackets = listOf('(', ')')
    private val bitwise = listOf("and", "or", "^", "&", "|")

    private var result = StringBuilder()

    private val variables = HashMap<String, String?>()
    private val code = StringBuilder()
    private var countOfTabs = 1

    fun getResult(): String {
        return result.toString()
    }

    override fun enterIfCond(ctx: GrammarParser.IfCondContext) {
        val statement = ctx.statement().text
        addTabs()
        code.append("if (")
        codeIf(statement)
    }

    fun codeIf(statement: String) {
        statement.split(' ').forEach {
            when {
                brackets.contains(it[0]) -> {
                    code.append('(')
                    code.append(it.slice(1 until it.length))
                }
                brackets.contains(it[it.length - 1]) -> {
                    code.append(it.slice(0 until it.length - 1))
                    code.append(')')
                }
                bitwise.contains(it) -> {
                    when (it) {
                        "and" -> {
                            code.append(" && ")
                        }
                        "or" -> {
                            code.append(" || ")
                        }
                        else -> {
                            code.append(" $it ")
                        }
                    }
                }
                wordsRegex.matches(it) -> {
                    if (variables[it] == null) {
                        variables[it] = null
                    }
                    code.append(it)
                }
                compareOperators.contains(it) -> {
                    code.append(" $it ")
                }
                else -> { // number
                    code.append(it)
                }
            }
        }
        code.append(") {\n")
        countOfTabs++
    }

    override fun enterElseCond(ctx: GrammarParser.ElseCondContext) {
        countOfTabs = ctx.TAB()?.text?.length?.div(4) ?: 0
        countOfTabs++
        addTabs()
        countOfTabs++
        code.append("} else {\n")
    }

    override fun enterElifCond(ctx: GrammarParser.ElifCondContext) {
        countOfTabs = ctx.TAB()?.text?.length?.div(4) ?: 0
        countOfTabs++
        addTabs()
        code.append("} else if (")
        codeIf(ctx.statement().text)
    }

    override fun enterLine(ctx: GrammarParser.LineContext) {
        when {
            ctx.input() != null -> {
                val input = ctx.input()
                val variable = input.text.split(" ")[0]
                if (variables[variable] == null) {
                    variables[variable] = null
                }
                when {
                    input.EQUAL() != null -> {
                        when {
                            input.STRING() != null -> {
                                addTabs()
                                variables[variable] = "string"
                                code.append("$variable = ${input.STRING().text.replace("\'", "\"")};\n")
                            }
                            input.INPUT() != null -> {
                                if (input.INPUT().text == "int(input())") {
                                    variables[variable] = "int"
                                } else {
                                    if (input.INPUT().text == "float(input())") {
                                        variables[variable] = "float";
                                    } else {
                                        variables[variable] = "string"
                                    }
                                }
                                addTabs()
                                if (variables[variable] == "int") {
                                    code.append("scanf(\"%d\", &$variable);\n")
                                } else {
                                    if (variables[variable] == "float") {
                                        code.append("scanf(\"%g\", &$variable);\n")
                                    } else {
                                        code.append("scanf(\"%s\", &$variable);\n")
                                    }
                                }
                            }
                            input.NUMBER() != null -> {
                                val number = input.NUMBER()
                                if (number.text.contains('.')) {
                                    variables[variable] = "float"
                                } else {
                                    variables[variable] = "int"
                                }
                                addTabs()
                                code.append("$variable = ${input.NUMBER()};\n")
                            }
                            input.expression() != null -> {
                                addTabs()
                                code.append("${ctx.input().text};\n")
                            }
                            else -> {
                                variables[variable] = variables[input.VARIABLE()[1].toString()]
                                addTabs()
                                code.append("${ctx.input().text};\n")
                            }
                        }
                    }
                    input.EQUALS() != null -> {
                        addTabs()
                        code.append("${ctx.input().text};\n")
                        val equalsVariables = HashSet<String>()
                        wordsRegex.findAll(input.expression().text).forEach { equalsVariables.add(it.value) }
                        if (equalsVariables.isNotEmpty()) {
                            variables[variable] = variables[equalsVariables.elementAt(0)]
                        } else {
                            var flag = false
                            input.expression().text.split(' ').forEach {
                                if (numberRegex.matches(it) && it.contains('.')) {
                                    flag = true
                                }
                            }
                            if (flag) {
                                variables[variable] = "float"
                            } else {
                                variables[variable] = "int"
                            }
                        }
                    }
                }
            }
            ctx.print() != null -> {
                addTabs()
                when {
                    ctx.print().VARIABLE() != null -> {
                        val printVariable = ctx.print().VARIABLE().text
                        if (variables[printVariable] == "int") {
                            code.append("printf(\"%d\\n\", ${printVariable});\n")
                        } else {
                            if (variables[printVariable] == "float") {
                                code.append("printf(\"%g\\n\", ${printVariable});\n")
                            } else {
                                code.append("printf(\"%s\\n\", ${printVariable});\n")
                            }
                        }
                    }
                    ctx.print().NUMBER() != null -> {
                        val number = ctx.print().NUMBER()
                        if (number.text.contains('.')) {
                            code.append("printf(\"%g\\n\", ${number.text});\n")
                        } else {
                            code.append("printf(\"%d\\n\", ${number.text});\n")
                        }
                    }
                    ctx.print().expression() != null -> {
                        var flag = false
                        ctx.print().expression().text.split(" ").forEach {
                            if (wordsRegex.matches(it)) {
                                if (variables[it] == "float") {
                                    flag = true
                                }
                            }
                        }
                        if (flag) {
                            code.append("printf(\"%g\\n\", ${ctx.print().expression().text});\n")
                        } else {
                            code.append("printf(\"%d\\n\", ${ctx.print().expression().text});\n")
                        }
                    }
                    else -> {
                        code.append("printf(\"\\n\");\n")
                    }
                }
            }
        }
    }

    override fun exitCondition(ctx: GrammarParser.ConditionContext) {
        countOfTabs--
        addTabs()
        code.append("}\n")
    }

    private fun addTabs() {
        repeat(countOfTabs) {
            code.append("\t")
        }
    }

    override fun exitStart(ctx: GrammarParser.StartContext) {
        val intVariables = HashSet<String>()
        variables.forEach {
            if (it.value == "int") {
                intVariables.add(it.key)
            }
        }
        if (intVariables.isNotEmpty()) {
            result.append("int ")
            intVariables.forEach {
                result.append("$it, ")
            }
            result.deleteRange(result.length - 2, result.length)
            result.append(";\n")
        }
        val floatVariables = HashSet<String>()
        variables.forEach {
            if (it.value == "float") {
                floatVariables.add(it.key)
            }
        }
        if (floatVariables.isNotEmpty()) {
            result.append("float ")
            floatVariables.forEach {
                result.append("$it, ")
            }
            result.deleteRange(result.length - 2, result.length)
            result.append(";\n")
        }
        val stringVariables = HashSet<String>()
        variables.forEach {
            if (it.value == "string") {
                stringVariables.add(it.key)
            }
        }
        if (stringVariables.isNotEmpty()) {
            result.append("string ")
            stringVariables.forEach {
                result.append("$it, ")
            }
            result.deleteRange(result.length - 2, result.length)
            result.append(";\n")
        }
        result.append("\n")
        result.append("int main() {\n")
        addTabs()
        code.append("return 0;\n")
        result.append(code)
        result.append("}")
    }
}