import antlr.grammar.GrammarLexer
import antlr.grammar.GrammarParser
import generator.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File

fun main() {
    println("-----------SECOND TASK----------")
    createGrammar("GrammarSecond.g4", "secondTask", "SecondTask")
    println("-----------CALCULATOR-----------")
    createGrammar("GrammarCalculator.gAnton", "calculator", "Calculator")
}

fun createGrammar(taskName: String, path: String, name: String) {
    val bufferedReader = File("src/main/java/antlr/$taskName").bufferedReader()
    val lexer = GrammarLexer(CharStreams.fromReader(bufferedReader))
    val tokens = CommonTokenStream(lexer)
    val parser = GrammarParser(tokens)
    val tree: ParseTree = parser.start()
    val walker = ParseTreeWalker()
    val grammarWalker = GrammarWalker()
    walker.walk(grammarWalker, tree)
    val terminals = grammarWalker.terminals
    val nonTerminals = grammarWalker.nonTerminals
    println("Terminals:")
    terminals.forEach {
        println("${it.name} ${it.regex}")
    }
    println("--------------")
    println("NonTerminals:")
    nonTerminals.forEach {
        println("${it.name} ${it.nonTerminalRules} ${it.returnType} ${it.arguments}")
    }
    val firstFollow = FirstFollow()
    println("FIRST")
    val first = firstFollow.buildFirst(nonTerminals, terminals)
    println("-----")
    println("First")
    first.forEach {
        println("${it.key} ${it.value}")
    }
    println("-------")
    val follow = firstFollow.buildFollow(first, nonTerminals, terminals)
    println("Follow")
    follow.forEach {
        println("${it.key} ${it.value}")
    }
    val dir = File("$path/")
    if (!dir.isDirectory) {
        dir.mkdirs()
    }
    val parserGenerator = ParserGenerator()
    File("$path/${name}Parser.kt").writeText(parserGenerator.createFunction(nonTerminals, terminals, first, follow, path))
    val tokensGenerator = TokensGenerator()
    File("$path/${name}Tokens.kt").writeText(tokensGenerator.createTokens(terminals, path))
    val lexerGenerator = LexerGenerator()
    File("$path/${name}Lexer.kt").writeText(lexerGenerator.createLexer(path))
    val runGenerator = RunGenerator()
    File("$path/${name}Run.kt").writeText(runGenerator.createRun(path))
}