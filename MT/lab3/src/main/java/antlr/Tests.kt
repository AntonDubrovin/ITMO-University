package antlr

import GrammarWalker
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
        val testString =
            """
a = int(input())
b = int(input())
print(a + b)
"""
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
        val testString =
            """
a = int(input())
b = int(input())
if a > b:
    if a > 100:
        b = 0
    else:
        if a < 50:
            b = 100
        else:
            b = 10
else:
    print(a + b)
print()
"""
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
        val testString =
            """
a = float(input())
if (a > 3.5) and (a < 100.17):
    b = 10
else:
    b = 0
    c = a
if b == 10:
    print(a)
else:
    print(b)
print()
if b != 0:
    if a >= 50.50 ^ b:
        print(a + 50)
    else:
        f = int(input())
        g = float(input())
        print((f + g) / a * b)
else:
    print(10)
"""
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