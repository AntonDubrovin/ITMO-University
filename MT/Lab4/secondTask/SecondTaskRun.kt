package secondTask
fun main() {
    while (true) {
        println(Parser(Lexer(readLine()!!.reader())).parse())
    }
}