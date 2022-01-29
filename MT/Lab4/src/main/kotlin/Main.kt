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
    println("-----------NOT LL-----------")
    createGrammar("NotLL.gAnton", "notLL", "NotLL")
    println("-----------NOT LL SECOND-----------")
    createGrammar("NotLLSecond.gAnton", "notLLSecond", "NotLLSecond")
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
    println("-----------------------------------TERMINALS $name-----------------------------------")
    terminals.forEach {
        println("${it.name} ${it.regex}")
    }
    println("-----------------------------------NON TERMINALS $name-----------------------------------")
    nonTerminals.forEach {
        println("${it.name} ${it.nonTerminalRules} ${it.returnType} ${it.arguments}")
    }
    val first = buildFirst(nonTerminals, terminals)
    println("-----------------------------------FIRST $name-----------------------------------")
    first.forEach {
        println("${it.key} ${it.value}")
    }
    val follow = buildFollow(first, nonTerminals, terminals)
    println("-----------------------------------FOLLOW $name-----------------------------------")
    follow.forEach {
        println("${it.key} ${it.value}")
    }
    println("------------------------------------LL GRAMMAR $name-----------------------------------")
    val checkLL = checkLL(nonTerminals, first, follow)
    println(checkLL)
    if (checkLL) {
        val parserGenerator = ParserGenerator()
        val dir = File("$path/")
        if (!dir.isDirectory) {
            dir.mkdirs()
        }
        val parserDir = File("$path/${name}Parser.kt")
        if (!parserDir.isFile) {
            parserDir.createNewFile()
        }
        parserDir.writeText(
            parserGenerator.createFunction(
                nonTerminals, terminals, first, follow, path
            )
        )
        val tokensGenerator = TokensGenerator()
        val tokensDir = File("$path/${name}Tokens.kt")
        if (!tokensDir.isFile) {
            tokensDir.createNewFile()
        }
        tokensDir.writeText(tokensGenerator.createTokens(terminals, path))
        val lexerGenerator = LexerGenerator()
        val lexerDir = File("$path/${name}Lexer.kt")
        if (!lexerDir.isFile) {
            lexerDir.createNewFile()
        }
        lexerDir.writeText(lexerGenerator.createLexer(path))
        val runGenerator = RunGenerator()
        val runDir = File("$path/${name}Run.kt")
        if (!runDir.isFile) {
            runDir.createNewFile()
        }
        runDir.writeText(runGenerator.createRun(path))
    }
}