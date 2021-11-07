package org.csc.kotlin2021.mastermind.generation

import org.csc.kotlin2021.mastermind.Game
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class BaseGeneratorTest {
    private var secretLength = 4
    private var alphabetSize = 8
    private var secret = ""

    companion object {
        @JvmStatic
        fun countRange() = listOf(
            Arguments.of(1..8)
        )
    }

    private fun test(curSecretLength: Int) {
        secretLength = curSecretLength
        val game = Game(true, alphabetSize, secretLength, 100)
        secret = game.secretWord
        assertTrue(secret.length == secretLength)
        assertTrue(secret.toSet().size == secretLength)
        assertTrue(secret.all { it in game.gameLetters })
    }

    @ParameterizedTest
    @MethodSource("countRange")
    fun testGenerator(secretLength: IntRange) {
        secretLength.forEach {
            test(it)
        }
    }
}
