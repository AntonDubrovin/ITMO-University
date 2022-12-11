import org.testng.AssertJUnit.assertTrue
import org.testng.annotations.Test
import tokenizer.Tokenizer
import visitor.CalcVisitor
import visitor.ParseVisitor

class Tests {
    @Test
    fun simple_add() {
        val expression = "1 + 2"
        val result = 3.0
        assertTrue(calculate(expression, result))
    }

    @Test
    fun simple_minus() {
        val expression = "5 - 4"
        val result = 1.0
        assertTrue(calculate(expression, result))
    }

    @Test
    fun simple_multiply() {
        val expression = "3 * 7"
        val result = 21.0
        assertTrue(calculate(expression, result))
    }

    @Test
    fun simple_divide() {
        val expression = "22 / 4"
        val result = 5.5
        assertTrue(calculate(expression, result))
    }

    @Test
    fun test_from_task_1() {
        val expression = "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2"
        val result = 1279.0
        assertTrue(calculate(expression, result))
    }

    @Test
    fun test_from_task_2() {
        val expression = "(30 + 2) / 8"
        val result = 4.0
        assertTrue(calculate(expression, result))
    }

    private fun calculate(expression: String, result: Double): Boolean {
        val tokenizer = Tokenizer(expression)
        var tokens = tokenizer.tokenize()
        val parser = ParseVisitor()

        for (token in tokens) {
            token.accept(parser)
        }
        tokens = parser.endParse()

        val calcVisitor = CalcVisitor()
        for (token in tokens) {
            token.accept(calcVisitor)
        }

        return calcVisitor.endCalc() == result
    }
}