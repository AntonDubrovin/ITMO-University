package com.h0tk3y.spbsu.kotlin.course.lesson1

import main
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
    fun testSolutionNoUsername() {
        testStdout("ещё никогда Штирлиц не был так близок к провалу$newline") { main() }
    }
}