package com.h0tk3y.spbsu.kotlin.course.lesson1

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class Test {
    companion object {
        val newline = System.getProperty("line.separator")
    }

    private fun testStdout(expected: String, fn: () -> Unit) {
        val oldStdOut = System.out
        try {
            val stream = ByteArrayOutputStream()
            stream.use {
                PrintStream(stream, true, "UTF-8").use { System.setOut(it); fn(); }
            }
            val actual = stream.toByteArray().inputStream().bufferedReader().readText()
            Assert.assertEquals(expected, actual)
        } finally {
            System.setOut(oldStdOut)
        }
    }

    @Test
    fun testFizzBuzz() {
        listOf(1..30, 10..200, -5..300).forEach { range ->
            testStdout(range.joinToString(newline, "", newline, transform = {
                when {
                    it % 15 == 0 -> "fizzbuzz"
                    it % 5 == 0 -> "buzz"
                    it % 3 == 0 -> "fizz"
                    else -> it.toString()
                }
            })) {
                fizzbuzz(range)
            }
        }
    }
}