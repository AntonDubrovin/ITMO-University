package org.csc.kotlin2021.mastermind.matching

import org.csc.kotlin2021.mastermind.Game
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class SequenceMatchingTest {
    private val alphabetSize = 26
    private val countOfAttempts = 100

    companion object {
        @JvmStatic
        fun sequencesDifferent() = listOf(
            Arguments.of("ACEB", "BCDF", 1, 1),
            Arguments.of("ABCD", "DCBA", 0, 4),
            Arguments.of("ABCD", "ABCE", 3, 0),
            Arguments.of("ABBAB", "BAABA", 0, 4),
            Arguments.of("HGFED", "ABCDE", 0, 2),
            Arguments.of("ABCDEF", "ABCDEF", -1, -1),
            Arguments.of("ABCDEFG", "EEEEEEE", 1, 0),
            Arguments.of("ABCDEFG", "ABAACCE", 2, 2),
            Arguments.of("ABCDEFGH", "AACCEEHH", 4, 0)
        )

        @JvmStatic
        fun sequenceRepetitive() = listOf(
            Arguments.of("AAAA", "ABCD", 1, 0),
            Arguments.of("AABB", "BBAA", 0, 4),
            Arguments.of("HHHH", "AAAA", 0, 0),
            Arguments.of("ABACH", "ABHCG", 3, 1),
            Arguments.of("BACHG", "AHGCB", 0, 5),
            Arguments.of("HHHGGGBA", "ABCHEFDH", 0, 4)
        )
    }

    @ParameterizedTest
    @MethodSource("sequencesDifferent")
    fun testSequenceMatchingDifferent(initial: String, actual: String, expectedFullMatch: Int, expectedPartMatch: Int) {
        val game = Game(true, alphabetSize, initial.length, countOfAttempts)
        test(game, initial, actual, expectedFullMatch, expectedPartMatch)
    }

    @ParameterizedTest
    @MethodSource("sequenceRepetitive")
    fun test(initial: String, actual: String, expectedFullMatch: Int, expectedPartMatch: Int) {
        val game = Game(false, alphabetSize, initial.length, countOfAttempts)
        test(game, initial, actual, expectedFullMatch, expectedPartMatch)
    }

    private fun test(game: Game, initial: String, actual: String, expectedFullMatch: Int, expectedPartMatch: Int) {
        val result = game.compare(actual, initial)
        val actualFullMatch = result.positions
        val actualPartMatch = result.letters
        Assertions.assertEquals(
            expectedFullMatch, actualFullMatch, "Full matches don't equal! " +
                    "Actual full match count = $actualFullMatch, expected full match count = $expectedFullMatch"
        )
        Assertions.assertEquals(
            expectedPartMatch, actualPartMatch, "Part matches don't equal! " +
                    "Part full part count = $actualPartMatch, expected part match count = $expectedPartMatch"
        )
    }
}
