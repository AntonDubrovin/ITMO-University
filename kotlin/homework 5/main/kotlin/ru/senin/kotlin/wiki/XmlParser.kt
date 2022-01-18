package ru.senin.kotlin.wiki

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import ru.senin.kotlin.wiki.Solution.Companion.articleSizes
import ru.senin.kotlin.wiki.Solution.Companion.articleTimes
import ru.senin.kotlin.wiki.Solution.Companion.popularTextWords
import ru.senin.kotlin.wiki.Solution.Companion.popularTitleWords
import javax.xml.parsers.SAXParserFactory

class XmlParser {
    private var tags = ArrayDeque<String>()
    private var titleText = ""
    private var revisionText = ""
    private var bytes = ""
    private var date = ""

    fun parse(bzipInputStream: BZip2CompressorInputStream) {
        val saxParser = SAXParserFactory.newInstance().newSAXParser()
        val saxHandler = object : DefaultHandler() {
            override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
                tags.add(qName)
                if (qName == "text") {
                    bytes = attributes.getValue("bytes") ?: ""
                }
            }

            override fun characters(ch: CharArray, start: Int, length: Int) {
                when (tags.last()) {
                    "title" -> {
                        if (checkTags("mediawiki", "page", "title")) {
                            titleText = String(ch, start, length)
                        }
                    }
                    "text" -> {
                        if (checkTags("mediawiki", "page", "revision", "text")) {
                            revisionText += String(ch, start, length)
                        }
                    }
                    "timestamp" -> {
                        if (checkTags("mediawiki", "page", "revision", "timestamp")) {
                            date = String(ch, start, length)
                        }
                    }
                }
            }

            override fun endElement(uri: String, localName: String, qName: String) {
                when (tags.last()) {
                    "page" -> {
                        if (titleText != "" && revisionText != "" && date != "" && bytes != "") {
                            wordsRegex.findAll(titleText).forEach {
                                popularTitleWords[it.value.lowercase()] =
                                    popularTitleWords[it.value.lowercase()]?.plus(1) ?: 1
                            }

                            wordsRegex.findAll(revisionText).forEach {
                                popularTextWords[it.value.lowercase()] =
                                    popularTextWords[it.value.lowercase()]?.plus(1) ?: 1
                            }

                            articleSizes[bytes.toInt()] = articleSizes[bytes.toInt()]?.plus(1) ?: 1

                            val year = date.substring(0, 4)
                            articleTimes[year.toInt()] = articleTimes[year.toInt()]?.plus(1) ?: 1
                        }
                        titleText = ""
                        revisionText = ""
                        date = ""
                        bytes = ""
                    }
                }
                tags.removeLast()
            }
        }
        saxParser.parse(bzipInputStream, saxHandler)
    }

    private fun checkTags(vararg checkedTags: String): Boolean {
        var flag = true
        checkedTags.reversed().forEach {
            if (tags.removeLast() != it) {
                flag = false
            }
        }
        checkedTags.forEach {
            tags.add(it)
        }
        return flag
    }
}