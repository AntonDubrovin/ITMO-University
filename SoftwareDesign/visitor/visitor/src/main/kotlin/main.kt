import tokenizer.Tokenizer
import visitor.CalcVisitor
import visitor.ParseVisitor
import visitor.PrintVisitor

fun main() {
    val input = readln()
    println(input)
    val tokenizer = Tokenizer(input)
    var tokens = tokenizer.tokenize()
    val parser = ParseVisitor()

    for (token in tokens) {
        token.accept(parser)
    }
    tokens = parser.endParse()

    val printVisitor = PrintVisitor()
    for (token in tokens) {
        token.accept(printVisitor)
    }

    val calcVisitor = CalcVisitor()
    for (token in tokens) {
        token.accept(calcVisitor)
    }

    println("\nResult = ${calcVisitor.endCalc()}")
}