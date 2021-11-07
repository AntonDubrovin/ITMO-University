package org.csc.kotlin2021.mastermind

const val minLetters = 2
const val maxLetters = 8
const val minAttempts = 1
const val maxAttempts = 100
const val minAlphabet = 1
const val maxAlphabet = 26

class Game(
    private val differentLetters: Boolean,
    private val alphabetSize: Int,
    private val secretLength: Int,
    private val countOfAttempts: Int
) : Generate() {
    val gameLetters = List(alphabetSize) { index -> 'A' + index }
    private var currentAttempt = 0
    private val secret = generateSecret(differentLetters)
    val secretWord = secret

    private fun generateSecret(
        differentLetters: Boolean
    ) = buildString {
        val mark = MutableList(alphabetSize) { false }
        for (i in 0 until secretLength) {
            var index = (0 until alphabetSize).random()
            if (differentLetters) {
                if (!mark[index]) {
                    append(gameLetters[index])
                    mark[index] = true
                } else {
                    while (mark[index]) {
                        index = ++index % alphabetSize
                    }
                    append(gameLetters[index])
                    mark[index] = true
                }
            } else {
                append(gameLetters[index])
            }
        }
    }

    fun checkAttempts(): Boolean {
        return currentAttempt < countOfAttempts
    }

    fun incrementAttempts() {
        currentAttempt++
    }

    fun checkGuessString(guessString: String?): Boolean {
        return guessString != null && guessString.length == secretLength &&
                differentLetters && guessString.toSet().size == guessString.length &&
                guessString.all { gameLetters.contains(it) }
    }

    fun compare(guessString: String, compareSecret: String = secret): ResultPair {
        return if (compareSecret == guessString) {
            ResultPair(-1, -1)
        } else {
            var positions = 0
            var letters = 0
            guessString.indices.forEach {
                if (guessString[it] == compareSecret[it]) {
                    positions++
                }
            }
            gameLetters.forEach { letter ->
                letters += Integer.min(compareSecret.count { it == letter }, guessString.count { it == letter })
            }
            ResultPair(positions, letters - positions)
        }
    }

    companion object {
        fun generateGame(
            differentLetters: Int,
            secretLength: Int,
            alphabetSize: Int,
            countOfAttempts: Int
        ): Generate {
            return when {
                differentLetters !in 1..2 ->
                    InputError("Incorrect game mode")
                countOfAttempts !in minAttempts..maxAttempts ->
                    InputError("Incorrect count of attempts")
                secretLength !in minLetters..maxLetters ->
                    InputError("Incorrect count of letters in secret word")
                alphabetSize !in 1..26 ->
                    InputError("Incorrect alphabet size")
                differentLetters == 1 && alphabetSize < secretLength ->
                    InputError("Alphabet size must be greater than secret length when letters are different")
                else ->
                    Game(differentLetters == 1, alphabetSize, secretLength, countOfAttempts)
            }
        }
    }
}

data class ResultPair(val positions: Int, val letters: Int)

sealed class Generate

class InputError(val errorInput: String) : Generate()
