package generator

import java.io.File

class LexerGenerator {
    fun createLexer(path: String): String {
        val lexer = StringBuilder()
        lexer.appendLine("package $path")
        lexer.appendLine("import java.io.IOException")
        lexer.appendLine("import java.io.Reader")
        lexer.appendLine("typealias Token = Int")
        lexer.appendLine("const val UNKNOWN_CHAR = -2")
        lexer.appendLine("open class Lexer(_reader: Reader) {")
        lexer.appendLine(File("src/main/kotlin/generator/LexerTemplate").readText())
        lexer.appendLine("}\n")
        return lexer.toString()
    }
}