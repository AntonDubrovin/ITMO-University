package info.kgeorgiy.ja.dubrovin.statistics;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class TextStatisticsTest {
    private static Locale localeTextRU;
    private static Locale localeTextEN;
    private static String textRU;
    private static String textEN;

    @BeforeClass
    public static void set() {
        localeTextRU = new Locale("ru", "RU");
        localeTextEN = new Locale("en", "GB");

        final StringBuilder textFileStringBuilder = new StringBuilder();
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("input.txt"))) {
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                textFileStringBuilder.append(currentLine);
                currentLine = bufferedReader.readLine();
            }
            textRU = textFileStringBuilder.toString();
        } catch (final IOException e) {
            System.err.println("Can't create buffered reader");
        }

        final StringBuilder textFileStringBuilderEN = new StringBuilder();
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("inputEN.txt"))) {
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                textFileStringBuilderEN.append(currentLine);
                currentLine = bufferedReader.readLine();
            }
            textEN = textFileStringBuilderEN.toString();
        } catch (final IOException e) {
            System.err.println("Can't create buffered reader");
        }
    }

    @Test
    public void sentencesStatisticTestRULocale() {
        final SentenceStatistics sentenceStatistics = new SentenceStatistics(localeTextRU, localeTextRU);
        sentenceStatistics.sentenceStatisticsCount(textRU);
        Assert.assertEquals(11, sentenceStatistics.getSentencesCount());
        Assert.assertEquals(9, sentenceStatistics.getDifferentSentencesCount());
        Assert.assertEquals("Абвгдеёжзийклмнопрстуфхцчшщъыьэюя.", sentenceStatistics.getMinSentence());
        Assert.assertEquals("Фазан 4,12.", sentenceStatistics.getMaxSentence());
        Assert.assertEquals(6, sentenceStatistics.getMinSentenceLength().length());
        Assert.assertEquals("Знать.", sentenceStatistics.getMinSentenceLength());
        Assert.assertEquals(34, sentenceStatistics.getMaxSentenceLength().length());
        Assert.assertEquals("Абвгдеёжзийклмнопрстуфхцчшщъыьэюя.", sentenceStatistics.getMaxSentenceLength());
        Assert.assertEquals("16,364", sentenceStatistics.getAverageSentenceLength());
    }

    @Test
    public void sentencesStatisticTestENLocale() {
        final SentenceStatistics sentenceStatisticsEN = new SentenceStatistics(localeTextEN, localeTextEN);
        sentenceStatisticsEN.sentenceStatisticsCount(textEN);
        Assert.assertEquals(7, sentenceStatisticsEN.getSentencesCount());
        Assert.assertEquals(7, sentenceStatisticsEN.getDifferentSentencesCount());
        Assert.assertEquals("Every 7.", sentenceStatisticsEN.getMinSentence());
        Assert.assertEquals("Wants 17.", sentenceStatisticsEN.getMaxSentence());
        Assert.assertEquals(3, sentenceStatisticsEN.getMinSentenceLength().length());
        Assert.assertEquals("To.", sentenceStatisticsEN.getMinSentenceLength());
        Assert.assertEquals(39, sentenceStatisticsEN.getMaxSentenceLength().length());
        Assert.assertEquals("fifty 01/01/2000 05/05/2021 05/05/2021.", sentenceStatisticsEN.getMaxSentenceLength());
        Assert.assertEquals("17", sentenceStatisticsEN.getAverageSentenceLength());
    }

    @Test
    public void wordsStatisticsTestRULocale() {
        final WordStatistics wordStatistics = new WordStatistics(localeTextRU, localeTextRU);
        wordStatistics.wordStatisticsCount(textRU);
        Assert.assertEquals(16, wordStatistics.getWordsCount());
        Assert.assertEquals(9, wordStatistics.getDifferentWordsCount());
        Assert.assertEquals("Абвгдеёжзийклмнопрстуфхцчшщъыьэюя", wordStatistics.getMinWord());
        Assert.assertEquals("Фазан", wordStatistics.getMaxWord());
        Assert.assertEquals(3, wordStatistics.getMinWordLength().length());
        Assert.assertEquals("кек", wordStatistics.getMinWordLength());
        Assert.assertEquals(33, wordStatistics.getMaxWordLength().length());
        Assert.assertEquals("Абвгдеёжзийклмнопрстуфхцчшщъыьэюя", wordStatistics.getMaxWordLength());
        Assert.assertEquals("6,875", wordStatistics.getAverageWordsLength());
    }

    @Test
    public void wordsStatisticsTestENLocale() {
        final WordStatistics wordStatistics = new WordStatistics(localeTextEN, localeTextEN);
        wordStatistics.wordStatisticsCount(textEN);
        Assert.assertEquals(11, wordStatistics.getWordsCount());
        Assert.assertEquals(11, wordStatistics.getDifferentWordsCount());
        Assert.assertEquals("Every", wordStatistics.getMinWord());
        Assert.assertEquals("where", wordStatistics.getMaxWord());
        Assert.assertEquals(2, wordStatistics.getMinWordLength().length());
        Assert.assertEquals("To", wordStatistics.getMinWordLength());
        Assert.assertEquals(8, wordStatistics.getMaxWordLength().length());
        Assert.assertEquals("pheasant", wordStatistics.getMaxWordLength());
        Assert.assertEquals("4.727", wordStatistics.getAverageWordsLength());
    }

    @Test
    public void numbersStatisticsTestRULocale() {
        final NumbersStatistics numbersStatistics = new NumbersStatistics(localeTextRU, localeTextRU);
        numbersStatistics.numbersStatisticsCount(textRU);
        Assert.assertEquals(10, numbersStatistics.getNumbersCount());
        Assert.assertEquals(7, numbersStatistics.getDifferentNumbersCount());
        Assert.assertEquals("-11", numbersStatistics.getMinNumber());
        Assert.assertEquals("500", numbersStatistics.getMaxNumber());
        Assert.assertEquals("75,012", numbersStatistics.getAverageNumber());
    }

    @Test
    public void numbersStatisticsTestENLocale() {
        final NumbersStatistics numbersStatistics = new NumbersStatistics(localeTextEN, localeTextEN);
        numbersStatistics.numbersStatisticsCount(textEN);
        Assert.assertEquals(6, numbersStatistics.getNumbersCount());
        Assert.assertEquals(5, numbersStatistics.getDifferentNumbersCount());
        Assert.assertEquals("-17.18", numbersStatistics.getMinNumber());
        Assert.assertEquals("17", numbersStatistics.getMaxNumber());
        Assert.assertEquals("2.97", numbersStatistics.getAverageNumber());
    }

    @Test
    public void dateStatisticsTestRULocale() {
        final DateStatistics dateStatistics = new DateStatistics(localeTextRU, localeTextRU);
        dateStatistics.dateStatisticsCount(textRU);
        Assert.assertEquals(2, dateStatistics.getDateCount());
        Assert.assertEquals(2, dateStatistics.getDifferentDate());
        Assert.assertEquals("07.02.2021", dateStatistics.getMaxDate());
        Assert.assertEquals("07.02.2019", dateStatistics.getMinDate());
        Assert.assertEquals("07.02.2020", dateStatistics.getAverageDate());
    }

    @Test
    public void dateStatisticsTestENLocale() {
        final DateStatistics dateStatistics = new DateStatistics(localeTextEN, localeTextEN);
        dateStatistics.dateStatisticsCount(textEN);
        Assert.assertEquals(3, dateStatistics.getDateCount());
        Assert.assertEquals(2, dateStatistics.getDifferentDate());
        Assert.assertEquals("05/05/2021", dateStatistics.getMaxDate());
        Assert.assertEquals("01/01/2000", dateStatistics.getMinDate());
        Assert.assertEquals("24/03/2014", dateStatistics.getAverageDate());
    }
}