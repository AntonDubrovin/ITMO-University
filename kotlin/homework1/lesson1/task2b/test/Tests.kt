package com.h0tk3y.spbsu.kotlin.course.lesson1

import org.junit.Assert
import org.junit.Test

class Test {
    private fun doTest(expected: Int, n: Int, first: Int? = null, second: Int? = null) {
        val actual = when {
            first == null -> fibonacci(n)
            second == null -> fibonacci(n, first)
            else -> fibonacci(n, first, second)
        }
        Assert.assertEquals(
                "fibonacci(${listOfNotNull(n, first, second).joinToString()}) should be $expected, but was $actual",
                expected,
                actual
        )
    }

    @Test
    fun testFibonacci() {
        doTest(-1, 0)
        doTest(-1, 0, 1)
        doTest(-1, 0, 1, 1)

        doTest(-1, -1)
        doTest(-1, -1, 1)
        doTest(-1, -1, 1, 1)

        doTest(1, 1)
        doTest(5, 1, 5)
        doTest(10, 1, 10, 20)

        doTest(13, 7)
        doTest(65, 7, 5)
        doTest(210, 7, 10, 20)

        doTest(46368, 24)
        doTest(231840, 24, 5)
        doTest(750250, 24, 10, 20)
    }
}