package org.csc.kotlin2021.mastermind.generation

import org.csc.kotlin2021.mastermind.Game
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SmartGeneratorTest {
    @Test
    fun checkSize() {
        for (i in 1 until 26) {
            val game = Game(false, 26, i, 100)
            assertTrue(game.secretWord.length == i)
        }
    }
}
