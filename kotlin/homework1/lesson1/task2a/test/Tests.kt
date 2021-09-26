package com.h0tk3y.spbsu.kotlin.course.lesson1

import org.junit.Assert
import org.junit.Test

class Test {
    private fun doTest(expected: Int, n: Int) {
        try {
            val actual = factorial(n)
            Assert.assertEquals("factorial($n) should be $expected, but was $actual", expected, actual)
        } catch (e: StackOverflowError) {
            Assert.fail("Stack overflow in factorial($n).")
        }
    }

    @Test
    fun testFactorial() {
        Assert.assertEquals(-1, factorial(-123))
        Assert.assertEquals(-1, factorial(-1))
        Assert.assertEquals(1, factorial(0))
        var res = 1
        for (i in 1..10) {
            res *= i
            Assert.assertEquals(res, factorial(i))
        }
    }
}