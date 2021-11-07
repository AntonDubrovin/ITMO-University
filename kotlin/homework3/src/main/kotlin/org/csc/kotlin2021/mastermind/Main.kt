package org.csc.kotlin2021.mastermind

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        when (args[0]) {
            "console" -> startConsoleGame()
            "graphic" -> GameWindow()
            else -> incorrectInputMain()
        }
    } else {
        incorrectInputMain()
    }
}

fun incorrectInputMain() {
    println("Incorrect arguments. Enter \"graphic\" to window game, \"console\" to console game")
}
