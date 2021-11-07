package org.csc.kotlin2021.mastermind

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun GameWindow() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MasterMind",
        state = rememberWindowState(width = 700.dp, height = 600.dp)
    ) {
        var pressed by remember { mutableStateOf(false) }
        var differentLetters by remember { mutableStateOf(-1) }
        var secretLength by remember { mutableStateOf("") }
        var alphabetSize by remember { mutableStateOf("") }
        var countOfAttempts by remember { mutableStateOf("") }
        var graphicInputError: String? by remember { mutableStateOf(null) }
        val generatedGame: Generate
        if (pressed) {
            if (secretLength.toIntOrNull() != null && alphabetSize.toIntOrNull() != null &&
                countOfAttempts.toIntOrNull() != null
            ) {
                generatedGame = Game.generateGame(
                    differentLetters,
                    secretLength.toInt(),
                    alphabetSize.toInt(),
                    countOfAttempts.toInt()
                )
                if (generatedGame is InputError) {
                    pressed = false
                    graphicInputError = generatedGame.errorInput
                } else {
                    StartGame(generatedGame, secretLength.toInt(), alphabetSize.toInt())
                }
            } else {
                pressed = false
                graphicInputError = "All parameters must be integers"
            }
        } else {
            StartSettings(
                { pressed = true },
                { newDifferentLetters -> differentLetters = newDifferentLetters },
                { newSecretLength -> secretLength = newSecretLength },
                { newAlphabetSize -> alphabetSize = newAlphabetSize },
                { newCountOfAttempts -> countOfAttempts = newCountOfAttempts },
                graphicInputError
            )
        }
    }
}

@Composable
fun StartSettings(
    onPressed: () -> Unit,
    onDifferentLettersChange: (Int) -> Unit,
    onSecretLengthChange: (String) -> Unit,
    onAlphabetSizeChange: (String) -> Unit,
    onCountOfAttemptsChange: (String) -> Unit,
    graphicInputError: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.height(50.dp).fillMaxWidth()
    ) {
        Text(
            "Welcome to MasterMind game!",
            fontSize = 20.sp
        )
    }
    var secretLength by remember { mutableStateOf("") }
    var alphabetSize by remember { mutableStateOf("") }
    var countOfAttempts by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                label = {
                    Text("Secret length")
                },
                value = secretLength,
                onValueChange = { changeSecretLength ->
                    secretLength = changeSecretLength
                },
                modifier = Modifier.height(100.dp).width(300.dp).absolutePadding(bottom = 50.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                label = {
                    Text("Alphabet size")
                },
                value = alphabetSize,
                onValueChange = { changeAlphabetSize ->
                    alphabetSize = changeAlphabetSize
                },
                modifier = Modifier.height(100.dp).width(300.dp).absolutePadding(bottom = 50.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                label = {
                    Text("Count of attempts from 1 to 100")
                },
                value = countOfAttempts,
                onValueChange = { changeCountOfAttempts ->
                    countOfAttempts = changeCountOfAttempts
                },
                modifier = Modifier.height(100.dp).width(300.dp).absolutePadding(bottom = 50.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    onDifferentLettersChange(1)
                    onSecretLengthChange(secretLength)
                    onAlphabetSizeChange(alphabetSize)
                    onCountOfAttemptsChange(countOfAttempts)
                    onPressed()
                },
                modifier = Modifier.height(100.dp).width(250.dp).absolutePadding(right = 50.dp)
            ) {
                Text("Different")
            }
            Text("Game mode")
            Button(
                onClick = {
                    onDifferentLettersChange(2)
                    onSecretLengthChange(secretLength)
                    onAlphabetSizeChange(alphabetSize)
                    onCountOfAttemptsChange(countOfAttempts)
                    onPressed()
                },
                modifier = Modifier.height(100.dp).width(250.dp).absolutePadding(left = 50.dp)
            ) {
                Text("Repetitive")
            }
        }
        graphicInputError?.let {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().absolutePadding(top = 20.dp),
            ) {
                Text(it)
            }
        }
    }
}

@Composable
fun StartGame(
    generatedGame: Generate,
    secretLength: Int,
    alphabetSize: Int
) {
    var isAttempts by remember { mutableStateOf(true) }
    var isComplete by remember { mutableStateOf(false) }
    var guessValue by remember { mutableStateOf("") }
    var positions by remember { mutableStateOf(0) }
    var letters by remember { mutableStateOf(0) }
    val graphicGame = generatedGame as Game

    if (!isComplete && isAttempts) {
        Draw(
            graphicGame,
            guessValue,
            secretLength,
            alphabetSize,
            { newGuessValue -> guessValue = newGuessValue },
            { newPositions -> positions = newPositions },
            { newLetters -> letters = newLetters },
            { isAttempts = false },
            { isComplete = true }
        )
        DrawPositionsAndLetters(positions, letters)
    } else {
        if (!isAttempts) {
            DrawIsAttempts()
        }

        if (isComplete) {
            DrawIsComplete()
        }
    }
}

@Composable
fun DrawIsComplete() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "You win",
            fontSize = 30.sp,
            color = Color.Cyan
        )
    }
}

@Composable
fun DrawIsAttempts() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.height(50.dp).fillMaxSize()
    ) {
        Text(
            "Attempts ended, you lost :(",
            fontSize = 20.sp,
            modifier = Modifier.absolutePadding(top = 10.dp)
        )
    }
}

@Composable
fun DrawPositionsAndLetters(positions: Int, letters: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Positions: $positions; letters: $letters.",
            modifier = Modifier.absolutePadding(bottom = 150.dp),
            fontSize = 20.sp,
            color = Color.Blue
        )
    }
}

@Composable
fun Draw(
    graphicGame: Game,
    guessValue: String,
    secretLength: Int,
    alphabetSize: Int,
    onGuessValueChange: (String) -> Unit,
    onPositionsChange: (Int) -> Unit,
    onLettersChange: (Int) -> Unit,
    onAttemptsEnded: () -> Unit,
    onComplete: () -> Unit
) {
    var isInputCorrect by remember { mutableStateOf(false) }
    if (!graphicGame.checkAttempts()) {
        onAttemptsEnded()
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                label = {
                    Text("Your guess")
                },
                value = guessValue,
                onValueChange = { changeString ->
                    onGuessValueChange(changeString)
                },
                modifier = Modifier.absolutePadding(right = 50.dp)
            )
            Button(
                onClick = {
                    graphicGame.incrementAttempts()
                    if (!graphicGame.checkGuessString(guessValue)) {
                        isInputCorrect = true
                    } else {
                        isInputCorrect = false
                        val (positions, letters) = graphicGame.compare(guessValue)
                        if (positions == -1 && letters == -1) {
                            onComplete()
                        } else {
                            onPositionsChange(positions)
                            onLettersChange(letters)
                        }
                    }
                },
                modifier = Modifier.height(70.dp).width(200.dp)
            ) {
                Text("Check")
            }
        }
        if (isInputCorrect) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "Incorrect input\n" +
                            "You need a $secretLength letter word\n" +
                            "And letters must be from A to ${'A' + alphabetSize - 1}\n" +
                            "If mode is different that letters must be different",
                    modifier = Modifier.absolutePadding(top = 300.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
}
