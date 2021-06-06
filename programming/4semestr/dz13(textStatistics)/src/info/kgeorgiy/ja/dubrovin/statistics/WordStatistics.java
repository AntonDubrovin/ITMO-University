package info.kgeorgiy.ja.dubrovin.statistics;

import java.text.BreakIterator;
import java.text.Collator;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class WordStatistics {
    private int wordsCount = 0;
    private String minWord = "";
    private String maxWord = "";
    private String minWordLength = "";
    private String maxWordLength = "";
    private double averageWordLength = 0;
    private int differentWordsCount;
    private final Locale localeText;
    private final NumberFormat outFormat;


    public WordStatistics(final Locale localeText, final Locale localeOut) {
        this.localeText = localeText;
        this.outFormat = NumberFormat.getNumberInstance(localeOut);
        outFormat.setMaximumFractionDigits(3);
    }

    public void wordStatisticsCount(final String text) {
        final Set<String> words = new HashSet<>();

        final BreakIterator breakIterator = BreakIterator.getWordInstance(localeText);
        breakIterator.setText(text);
        for (
                int begin = breakIterator.first(), end = breakIterator.next();
                end != BreakIterator.DONE;
                begin = end, end = breakIterator.next()
        ) {
            if (text.substring(begin, end).codePoints().noneMatch(Character::isLetter)) {
                continue;
            }

            final String currentWord = text.substring(begin, end);
            wordsCount++;
            words.add(currentWord);

            if (currentWord.length() > maxWordLength.length()) {
                maxWordLength = currentWord;
            }

            if (minWordLength.equals("")) {
                minWordLength = currentWord;
            } else {
                if (currentWord.length() < minWordLength.length()) {
                    minWordLength = currentWord;
                }
            }

            averageWordLength = (averageWordLength * (wordsCount - 1)
                    + currentWord.length()) / wordsCount;

            final Collator collator = Collator.getInstance(localeText);
            collator.setStrength(Collator.IDENTICAL);
            if (maxWord.equals("")) {
                maxWord = currentWord;
            } else {
                if (collator.compare(currentWord, maxWord) > 0) {
                    maxWord = currentWord;
                }
            }

            if (maxWord.equals("")) {
                maxWord = currentWord;
            } else {
                if (collator.compare(currentWord, maxWord) > 0) {
                    maxWord = currentWord;
                }
            }

            if (minWord.equals("")) {
                minWord = currentWord;
            } else {
                if (collator.compare(currentWord, minWord) < 0) {
                    minWord = currentWord;
                }
            }
        }

        differentWordsCount = words.size();
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public String getMinWord() {
        return minWord;
    }

    public String getMaxWord() {
        return maxWord;
    }

    public String getMinWordLength() {
        return minWordLength;
    }

    public String getMaxWordLength() {
        return maxWordLength;
    }

    public String getAverageWordsLength() {
        return outFormat.format(averageWordLength);
    }

    public int getDifferentWordsCount() {
        return differentWordsCount;
    }
}
