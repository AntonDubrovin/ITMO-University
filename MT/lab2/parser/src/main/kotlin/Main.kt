import kotlin.AssertionError

fun main() {
    test("Тест на or, and, xor", "a or b and c xor d")
    test("Тест на not без скобок", "not not a or not b or not c")
    test("Тест на not с скобками", "not (not (a or b) and c)")
    test("Тест на скобки", "(a and b) and (c or (d or e))")
    test("Лёгкий тест", "a or b and (c xor not(d or e and f))")
    test("Сложный тест", "a and not (b and (b or c) and c) and (e xor not d or not e)")
    test("Тест из условия", "a or (((a and b) or c) xor d)")
    failedTest("Не хватает второго выражения", "a or")
    failedTest("Не хватает выражения после not", "a or not")
    failedTest("Не хватает закрывающей скобки", "a or not (b")
    failedTest("Не хватает открывающей скобки после not", "not b)")
    failedTest("Не хватает операции между переменными", "a b")
    failedTest("Не хватает выражения внутри not", "a or not()")
    failedTest("Некорректное выражение внутри not", "a or not(or)")
    failedTest("Некорректное выражение", "xor")
    failedTest("Некорректное второе выражение", "a or ()")
    failedTest("Некорректное выражение внутри ()", "()")
    failedTest("Лишняя скобка в конце", "a or (a and b))")
    failedTest("Вместо второго выражения после or - операция", "a or or")
    failedTest("Синтаксическая ошибка: вместо or написано of", "a of b")
    println("----------")
    test("Тест модификации 1", "a or True")
    test("Тест модификации 2", "True and False or (Nan and (b or True) or False xor c)")
}

fun test(testDescription: String, test: String) {
    println(testDescription)
    val inputStream = test.byteInputStream()
    println(test)
    println(Parser(LexicalAnalyzer(inputStream).apply { nextToken() }).parse().show())
    println()
}

fun failedTest(testDescription: String, test: String) {
    try {
        test(testDescription, test)
        throw AssertionError("Прошёл тест с ошибкой ?????")
    } catch (e: AssertionError) {
        println(test)
        println(e.message)
        println()
    }
}