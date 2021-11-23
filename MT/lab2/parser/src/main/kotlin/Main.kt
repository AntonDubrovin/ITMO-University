fun main() {
   println("Лёгкий тест:")
   val testEasy = "a or b and (c xor not(d))"
   val inputStreamEasy = testEasy.byteInputStream()
   println(Parser(LexicalAnalyzer(inputStreamEasy).apply { nextToken() }).E().toExpression())

   println()
   println("Сложный тест:")
   val testHard = "a and not (b and (b or c) and c) and (e xor not(d))"
   val inputStreamHard = testHard.byteInputStream()
   println(Parser(LexicalAnalyzer(inputStreamHard).apply { nextToken() }).E().toExpression())

   println()
   println("Тест из условия:")
   val testCondition = "a or (((a and b) or c) xor d)"
   val inputStreamCondition = testCondition.byteInputStream()
   println(Parser(LexicalAnalyzer(inputStreamCondition).apply { nextToken() }).E().toExpression())

    println()
    println("TEST NOT")
    val testNot = "not (not (a))"
    val inputStreamNot = testNot.byteInputStream()
    println(Parser(LexicalAnalyzer(inputStreamNot).apply { nextToken() }).E().toExpression())
}
