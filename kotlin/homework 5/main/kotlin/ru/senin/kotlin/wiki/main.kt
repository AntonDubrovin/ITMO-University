package ru.senin.kotlin.wiki

import com.apurebase.arkenv.Arkenv
import com.apurebase.arkenv.argument
import com.apurebase.arkenv.parse
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class Parameters : Arkenv() {
    val inputs: List<File> by argument("--inputs") {
        description = "Path(s) to bzip2 archived XML file(s) with WikiMedia dump. Comma separated."
        mapping = {
            it.split(",").map { name -> File(name) }
        }
        validate("File does not exist or cannot be read") {
            it.all { file -> file.exists() && file.isFile && file.canRead() }
        }
    }

    val output: String by argument("--output") {
        description = "Report output file"
        defaultValue = { "statistics.txt" }
    }

    val threads: Int by argument("--threads") {
        description = "Number of threads"
        defaultValue = { 4 }
        validate("Number of threads must be in 1..32") {
            it in 1..32
        }
    }
}

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    try {
        val parameters = Parameters().parse(args)

        if (parameters.help) {
            println(parameters.toString())
            return
        }

        val threadsCount = parameters.threads
        val inputs = parameters.inputs
        val outputs = parameters.output

        println("Files count: ${inputs.size}")
        inputs.forEach {
            if (it.toString().substring(it.toString().length - 4, it.toString().length) != ".bz2") {
                throw IllegalArgumentException("All inputs should be bzip2")
            }
        }
        val duration = measureTime {
            Solution(threadsCount).start(
                inputs, outputs
                //"src/test/resources/testData/simple.xml",
                //"src/test/resources/testData/second.xml"
                //"src/test/resources/testData/big.xml"
            )
        }
        println("Time: ${duration.inWholeMilliseconds} ms")

    } catch (e: Exception) {
        println("Error! ${e.message}")
        throw e
    }
}
