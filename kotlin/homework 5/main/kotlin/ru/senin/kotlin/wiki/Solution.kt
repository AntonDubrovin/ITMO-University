package ru.senin.kotlin.wiki

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Thread.sleep
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.pow

val wordsRegex = Regex("""[а-яА-Я]{3,}""")

class Solution(private val countOfThreads: Int) {
    private var executor: ExecutorService? = null

    init {
        executor = if (countOfThreads != 1) {
            Executors.newFixedThreadPool(countOfThreads - 1)
        } else {
            Executors.newFixedThreadPool(1)
        }
        popularTitleWords.clear()
        popularTextWords.clear()
        articleSizes.clear()
        articleTimes.clear()
    }

    companion object {
        val popularTitleWords = ConcurrentHashMap<String, Int>()
        val popularTextWords = ConcurrentHashMap<String, Int>()
        val articleSizes = ConcurrentHashMap<Int, Int>()
        val articleTimes = ConcurrentHashMap<Int, Int>()
    }

    fun start(inputFiles: List<File>, outputFile: String) {
        inputFiles.forEach {
            if (countOfThreads == 1) {
                parse(BZip2CompressorInputStream(FileInputStream(it)))
            } else {
                executor?.execute {
                    parse(BZip2CompressorInputStream(FileInputStream(it)))
                }
            }
        }
        executor?.shutdown()
        while (executor?.isTerminated == false) {
            sleep(1000)
        }
        printAnswer(outputFile)
    }

    private fun parse(bzipInputStream: BZip2CompressorInputStream) {
        val xmlParser = XmlParser()
        xmlParser.parse(bzipInputStream)
    }

    private fun printAnswer(outputFile: String) {
        FileOutputStream(outputFile).bufferedWriter().apply {
            write("Топ-300 слов в заголовках статей:\n")
            write(printWords(popularTitleWords))
            write("\n")
            write("Топ-300 слов в статьях:\n")
            write(printWords(popularTextWords))
            write("\n")
            write("Распределение статей по размеру:\n")
            write(printArticlesSize())
            write("\n")
            write("Распределение статей по времени:\n")
            write(printArticleTimes())
            close()
        }
    }

    private fun printArticlesSize(): String {
        val minSize = articleSizes.keys().toList().minOrNull()
        val maxSize = articleSizes.keys().toList().maxOrNull()
        var result = ""
        if (minSize != null && maxSize != null) {
            for (i in minSize.toString().length - 1 until maxSize.toString().length) {
                result += ("$i ${
                    articleSizes.filter { (key, _) -> key >= 10.0.pow(i) && key <= 10.0.pow(i + 1) - 1 }.values.toList()
                        .sum()
                }\n")
            }
        }
        return result
    }

    private fun printWords(map: ConcurrentHashMap<String, Int>): String {
        var result = ""
        map.toList()
            .sortedWith(compareByDescending<Pair<String, Int>> { (_, value) -> value }.thenComparing { (key, _) -> key })
            .take(300).forEach {
                result += "${it.second} ${it.first}\n"
            }
        return result
    }

    private fun printArticleTimes(): String {
        val minYear = articleTimes.keys().toList().minOrNull()
        val maxYear = articleTimes.keys().toList().maxOrNull()
        var result = ""
        if (minYear != null && maxYear != null) {
            for (i in minYear..maxYear) {
                result += "$i ${articleTimes[i] ?: 0}\n"
            }
        }
        return result
    }
}

fun solve(parameters: Parameters) {
    println(parameters)
}