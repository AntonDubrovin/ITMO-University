package info.kgeorgiy.ja.dubrovin.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

public class TextStatistics {
    private final Locale localeText;
    private final Locale localeOut;
    private final String textFilePath;
    private final String outFilePath;

    TextStatistics(final Locale localeText, final Locale localeOut,
                   final String textFilePath, final String outFilePath) {
        this.localeText = localeText;
        this.localeOut = localeOut;
        this.textFilePath = textFilePath;
        this.outFilePath = outFilePath;
    }

    private void runClass() {
        final StringBuilder textFileStringBuilder = new StringBuilder();
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(textFilePath))) {
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                textFileStringBuilder.append(currentLine);
                currentLine = bufferedReader.readLine();
            }
            final String text = textFileStringBuilder.toString();

            final SentenceStatistics sentences = new SentenceStatistics(localeText, localeOut);
            sentences.sentenceStatisticsCount(text);
            final WordStatistics words = new WordStatistics(localeText, localeOut);
            words.wordStatisticsCount(text);
            final NumbersStatistics numbers = new NumbersStatistics(localeText, localeOut);
            numbers.numbersStatisticsCount(text);
            final MoneyStatistics moneys = new MoneyStatistics(localeText, localeOut);
            moneys.moneyStatisticsCount(text);
            final DateStatistics dates = new DateStatistics(localeText, localeOut);
            dates.dateStatisticsCount(text);

            try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(outFilePath))) {
                final ResourceBundle bundle =
                        ResourceBundle.getBundle("info.kgeorgiy.ja.dubrovin.statistics.UsageResourceBundle", localeOut);
                bufferedWriter.write(String.format(
                        "%s %s %s\n" +
                                "%s %s\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.\n" +
                                "%s %s\n" +
                                "\t%s %s: %s (%s %s).\n" +
                                "\t%s %s: \"%s\".\n" +
                                "\t%s %s: \"%s\".\n" +
                                "\t%s %s %s: %s (\"%s\").\n" +
                                "\t%s %s %s: %s (\"%s\").\n" +
                                "\t%s %s %s: %s.\n" +
                                "%s %s\n" +
                                "\t%s %s: %s (%s %s).\n" +
                                "\t%s %s: \"%s\".\n" +
                                "\t%s %s: \"%s\".\n" +
                                "\t%s %s %s: %s (\"%s\").\n" +
                                "\t%s %s %s: %s (\"%s\").\n" +
                                "\t%s %s %s: %s.\n" +
                                "%s %s\n" +
                                "\t%s %s: %s (%s %s).\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.\n" +
                                "%s %s\n" +
                                "\t%s %s: %s (%s %s).\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.\n" +
                                "%s %s\n" +
                                "\t%s %s: %s (%s %s).\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.\n" +
                                "\t%s %s: %s.",
                        bundle.getString("analysis"),
                        bundle.getString("file"),
                        textFilePath,
                        bundle.getString("summary"),
                        bundle.getString("stats"),
                        bundle.getString("count"),
                        bundle.getString("ofSentences"),
                        sentences.getSentencesCount(),
                        bundle.getString("count"),
                        bundle.getString("ofWords"),
                        words.getWordsCount(),
                        bundle.getString("count"),
                        bundle.getString("ofNumbers"),
                        numbers.getNumbersCount(),
                        bundle.getString("count"),
                        bundle.getString("ofSums"),
                        moneys.getSumsCount(),
                        bundle.getString("count"),
                        bundle.getString("ofDates"),
                        dates.getDateCount(),
                        bundle.getString("Stats"),
                        bundle.getString("sentences"),
                        bundle.getString("count"),
                        bundle.getString("ofSentences"),
                        sentences.getSentencesCount(),
                        sentences.getDifferentSentencesCount(),
                        bundle.getString("different"),
                        bundle.getString("min"),
                        bundle.getString("sentence"),
                        sentences.getMinSentence(),
                        bundle.getString("max"),
                        bundle.getString("sentence"),
                        sentences.getMaxSentence(),
                        bundle.getString("min9"),
                        bundle.getString("length"),
                        bundle.getString("ofSentence"),
                        sentences.getMinSentenceLength().length(),
                        sentences.getMinSentenceLength(),
                        bundle.getString("max9"),
                        bundle.getString("length"),
                        bundle.getString("ofSentence"),
                        sentences.getMaxSentenceLength().length(),
                        sentences.getMaxSentenceLength(),
                        bundle.getString("average"),
                        bundle.getString("length"),
                        bundle.getString("ofSentence"),
                        sentences.getAverageSentenceLength(),
                        bundle.getString("Stats"),
                        bundle.getString("words"),
                        bundle.getString("count"),
                        bundle.getString("ofWords"),
                        words.getWordsCount(),
                        words.getDifferentWordsCount(),
                        bundle.getString("different"),
                        bundle.getString("min"),
                        bundle.getString("word"),
                        words.getMinWord(),
                        bundle.getString("max"),
                        bundle.getString("word"),
                        words.getMaxWord(),
                        bundle.getString("min9"),
                        bundle.getString("length"),
                        bundle.getString("ofWord"),
                        words.getMinWordLength().length(),
                        words.getMinWordLength(),
                        bundle.getString("max9"),
                        bundle.getString("length"),
                        bundle.getString("ofWord"),
                        words.getMaxWordLength().length(),
                        words.getMaxWordLength(),
                        bundle.getString("average"),
                        bundle.getString("length"),
                        bundle.getString("ofWord"),
                        words.getAverageWordsLength(),
                        bundle.getString("Stats"),
                        bundle.getString("numbers"),
                        bundle.getString("count"),
                        bundle.getString("ofNumbers"),
                        numbers.getNumbersCount(),
                        numbers.getDifferentNumbersCount(),
                        bundle.getString("different"),
                        bundle.getString("min"),
                        bundle.getString("number"),
                        numbers.getMinNumber(),
                        bundle.getString("max"),
                        bundle.getString("number"),
                        numbers.getMaxNumber(),
                        bundle.getString("averageE"),
                        bundle.getString("number"),
                        numbers.getAverageNumber(),
                        bundle.getString("Stats"),
                        bundle.getString("sums"),
                        bundle.getString("count"),
                        bundle.getString("ofSums"),
                        moneys.getSumsCount(),
                        moneys.getDifferentSums(),
                        bundle.getString("different"),
                        bundle.getString("min9"),
                        bundle.getString("sum"),
                        moneys.getMinSums(),
                        bundle.getString("max9"),
                        bundle.getString("sum"),
                        moneys.getMaxSums(),
                        bundle.getString("average"),
                        bundle.getString("sum"),
                        moneys.getAverageSums(),
                        bundle.getString("Stats"),
                        bundle.getString("dates"),
                        bundle.getString("count"),
                        bundle.getString("ofDates"),
                        dates.getDateCount(),
                        dates.getDifferentDate(),
                        bundle.getString("different"),
                        bundle.getString("min9"),
                        bundle.getString("date"),
                        dates.getMinDate(),
                        bundle.getString("max9"),
                        bundle.getString("date"),
                        dates.getMaxDate(),
                        bundle.getString("average"),
                        bundle.getString("date"),
                        dates.getAverageDate()
                ));
            } catch (final IOException e) {
                System.err.println("Can't create buffered write");
                System.exit(0);
            }
        } catch (final IOException e) {
            System.err.println("Can't create buffered reader");
        }
    }


    private static boolean checkArguments(final String[] args) {
        if (args == null || args.length != 4) {
            return false;
        }

        for (final String arg : args) {
            if (arg == null) {
                return false;
            }
        }
        return true;
    }

    private static Locale getLocale(final String arg) {
        final Locale locale;
        final String[] localeArgs = arg.split("-");
        switch (localeArgs.length) {
            case 0:
                locale = Locale.getDefault();
                break;
            case 1:
                locale = new Locale(localeArgs[0]);
                break;
            case 2:
                locale = new Locale(localeArgs[0], localeArgs[1]);
                break;
            default:
                locale = new Locale(localeArgs[0], localeArgs[1], localeArgs[2]);
        }
        return locale;
    }

    public static void main(final String[] args) {
        if (!checkArguments(args)) {
            throw new IllegalArgumentException("Wrong arguments");
        }
        final Locale localeText = getLocale(args[0]);
        final Locale localeOut = getLocale(args[1]);

        final String textFile = args[2];
        final String outFile = args[3];
        new TextStatistics(localeText, localeOut, textFile, outFile).runClass();
    }
}