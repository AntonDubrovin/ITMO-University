import antlr.GrammarLexer
import antlr.GrammarParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.junit.jupiter.api.Test

class Tests {
    @Test
    fun conditionTest() {
        println("Тест из условия")
        println("----------------")
        val testString = "a = int(input())\n" +
                "b = int(input())\n" +
                "print(a + b)\n"
        val requiredString =
            "int a, b;\n" +
                    "\n" + "int main() {\n" +
                    "\tscanf(\"%d\", &a);\n" +
                    "\tscanf(\"%d\", &b);\n" +
                    "\tprintf(\"%d\\n\", a + b);\n" +
                    "\treturn 0;\n" + "}"
        println(testString)
        println("----------------")
        println(requiredString)
        assert(
            create(testString) == requiredString
        )
    }

    @Test
    fun ifIfIfTest() {
        println("Тест на вложенные if")
        println("----------------")
        val testString = "a = int(input())\n" +
                "b = int(input())\n" +
                "if a > b:\n" +
                "    if a > 100:\n" +
                "        b = 0\n" +
                "    else:\n" +
                "        if a < 50:\n" +
                "            b = 100\n" +
                "        else:\n" +
                "            b = 10\n" +
                "else:\n" +
                "    print(a + b)\n" +
                "print()\n"
        val requiredString =
            "int a, b;\n" +
                    "\n" +
                    "int main() {\n" +
                    "\tscanf(\"%d\", &a);\n" +
                    "\tscanf(\"%d\", &b);\n" +
                    "\tif (a > b) {\n" +
                    "\t\tif (a > 100) {\n" +
                    "\t\t\tb = 0;\n" +
                    "\t\t} else {\n" +
                    "\t\t\tif (a < 50) {\n" +
                    "\t\t\t\tb = 100;\n" +
                    "\t\t\t} else {\n" +
                    "\t\t\t\tb = 10;\n" +
                    "\t\t\t}\n" +
                    "\t\t}\n" +
                    "\t} else {\n" +
                    "\t\tprintf(\"%d\\n\", a + b);\n" +
                    "\t}\n" +
                    "\tprintf(\"\\n\");\n" +
                    "\treturn 0;\n" +
                    "}"
        println(testString)
        println("----------------")
        println(requiredString)
        assert(create(testString) == requiredString)
    }

    @Test
    fun randomTest() {
        println("Случайный непростой тест")
        println("----------------")
        val testString = "a = float(input())\n" +
                "if (a > 3.5) and (a < 100.17):\n" +
                "    b = 10\n" +
                "else:\n" +
                "    b = 0\n" +
                "    c = a\n" +
                "if b == 10:\n" +
                "    print(a)\n" +
                "else:\n" +
                "    print(b)\n" +
                "print()\n" +
                "if b != 0:\n" +
                "    if a >= 50.50 ^ b:\n" +
                "        print(a + 50)\n" +
                "    else:\n" +
                "        f = int(input())\n" +
                "        g = float(input())\n" +
                "        print((f + g) / a * b)\n" +
                "else:\n" +
                "    print(10)\n"
        val requiredString = "int b, f;\n" +
                "float a, c, g;\n" +
                "\n" +
                "int main() {\n" +
                "\tscanf(\"%g\", &a);\n" +
                "\tif ((a > 3.5) && (a < 100.17)) {\n" +
                "\t\tb = 10;\n" +
                "\t} else {\n" +
                "\t\tb = 0;\n" +
                "\t\tc = a;\n" +
                "\t}\n" +
                "\tif (b == 10) {\n" +
                "\t\tprintf(\"%g\\n\", a);\n" +
                "\t} else {\n" +
                "\t\tprintf(\"%d\\n\", b);\n" +
                "\t}\n" +
                "\tprintf(\"\\n\");\n" +
                "\tif (b != 0) {\n" +
                "\t\tif (a >= 50.50 ^ b) {\n" +
                "\t\t\tprintf(\"%g\\n\", a + 50);\n" +
                "\t\t} else {\n" +
                "\t\t\tscanf(\"%d\", &f);\n" +
                "\t\t\tscanf(\"%g\", &g);\n" +
                "\t\t\tprintf(\"%g\\n\", (f + g) / a * b);\n" +
                "\t\t}\n" +
                "\t} else {\n" +
                "\t\tprintf(\"%d\\n\", 10);\n" +
                "\t}\n" +
                "\treturn 0;\n" +
                "}"
        println(testString)
        println("----------------")
        println(requiredString)
        assert(
            create(testString) == requiredString
        )
    }

    private fun create(test: String): String {
        val lexer = GrammarLexer(CharStreams.fromString(test))
        val tokens = CommonTokenStream(lexer)
        val parser = GrammarParser(tokens)
        val tree: ParseTree = parser.start()
        val walker = ParseTreeWalker()
        val grammarWalker = GrammarWalker()
        walker.walk(grammarWalker, tree)
        return grammarWalker.getResult()
    }
}