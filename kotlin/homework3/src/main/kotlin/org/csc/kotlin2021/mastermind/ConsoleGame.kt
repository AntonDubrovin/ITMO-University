package org.csc.kotlin2021.mastermind

fun startConsoleGame() {
    var mode: Int? = null
    var secretLength: Int? = null
    var alphabetSize: Int? = null
    var countOfAttempts: Int? = null
    var generatedGame: Generate = InputError("")
    do {
        println((generatedGame as InputError).errorInput)
        println("Enter type of game: \n 1) All letters is different \n 2) Letters can be repeatable")
        readLine()?.let {
            mode = it.toIntOrNull()
        }
        println("Enter secret length")
        readLine()?.let {
            secretLength = it.toIntOrNull()
        }
        println("Enter alphabet size from $minAlphabet to $maxAlphabet")
        readLine()?.let {
            alphabetSize = it.toIntOrNull()
        }
        println("Enter count of attempts from $minAttempts to $maxAttempts")
        readLine()?.let {
            countOfAttempts = it.toIntOrNull()
        }
        if (mode == null || secretLength == null || alphabetSize == null || countOfAttempts == null) {
            continue
        }
        generatedGame = Game.generateGame(mode!!, secretLength!!, alphabetSize!!, countOfAttempts!!)
    } while (generatedGame is InputError)
    val consoleGame = generatedGame as Game
    var isComplete = false
    var isAttempts = true
    var guessString: String?
    while (!isComplete && isAttempts) {
        isAttempts = consoleGame.checkAttempts()
        if (!isAttempts) {
            break
        }
        consoleGame.incrementAttempts()
        println("Enter your guess")
        guessString = readLine()
        if (!consoleGame.checkGuessString(guessString)) {
            println("Your input is incorrect")
            continue
        }
        val (positions, letters) = consoleGame.compare(guessString!!)
        if (positions == -1 && letters == -1) {
            isComplete = true
            break
        }
        println("Positions: $positions; letters: $letters.")
    }

    if (!isAttempts) {
        println("Attempts ended, you lost :(")
    }

    if (isComplete) {
        println("You win")
    }
}
