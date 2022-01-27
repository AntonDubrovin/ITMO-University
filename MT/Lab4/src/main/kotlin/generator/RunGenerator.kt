package generator

class RunGenerator {
    fun createRun(path: String): String {
        val run = StringBuilder()

        run.append("package $path\n")
        run.append("fun main() {\n" +
                "    while (true) {\n" +
                "        println(Parser(Lexer(readLine()!!.reader())).parse())\n" +
                "    }\n" +
                "}")
        return run.toString()
    }
}