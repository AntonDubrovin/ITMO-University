Некоторые домашние задания университета ИТМО

<h1>Программмирование
  <h2>Парадигмы программирования(2 семестр)
    <h3>Домашнее задание 1. Обработка ошибок.
      Модификации
       * *Базовая*
    * Класс `ExpressionParser` должен реализовывать интерфейс
        [Parser](java/expression/exceptions/Parser.java)
    * Классы `CheckedAdd`, `CheckedSubtract`, `CheckedMultiply`,
        `CheckedDivide` и `CheckedNegate` должны реализовывать интерфейс
        [TripleExpression](java/expression/TripleExpression.java)
    * Нельзя использовать типы `long` и `double`
    * Нельзя использовать методы классов `Math` и `StrictMath`
    * [Исходный код тестов](java/expression/exceptions/ExceptionsTest.java)
 * *PowLog2*
    * Дополнительно реализуйте унарные операции:
        * `log2` – логарифм по уснованию 2, `log2 10` равно 3;
        * `pow2` – два в степени, `pow2 4` равно 16.
    * [Исходный код тестов](java/expression/exceptions/ExceptionsPowLog2Test.java)
 * *PowLog*
    * Дополнительно реализуйте бинарные операции (максимальный приоритет):
        * `**` – возведение в степень, `2 ** 3` равно 8;
        * `//` – логарифм, `10 // 2` равно 3.
    * [Исходный код тестов](java/expression/exceptions/ExceptionsPowLogTest.java)
  <h2>java-advanced(4 семестр)
