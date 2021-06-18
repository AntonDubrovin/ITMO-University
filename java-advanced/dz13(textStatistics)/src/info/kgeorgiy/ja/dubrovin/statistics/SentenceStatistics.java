package info.kgeorgiy.ja.dubrovin.statistics;

import java.text.BreakIterator;
import java.text.Collator;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class SentenceStatistics {
    private int sentencesCount = 0;
    private String minSentence = "";
    private String maxSentence = "";
    private String minSentenceLength = "";
    private String maxSentenceLength = "";
    private double averageSentenceLength = 0;
    private int differentSentencesCount;
    private final Locale localeText;
    private final NumberFormat outFormat;

    public SentenceStatistics(final Locale localeText, final Locale localeOut) {
        this.localeText = localeText;
        this.outFormat = NumberFormat.getNumberInstance(localeOut);
        outFormat.setMaximumFractionDigits(3);
    }

    public void sentenceStatisticsCount(final String text) {
        final Set<String> sentences = new HashSet<>();

        final BreakIterator breakIterator = BreakIterator.getSentenceInstance(localeText);
        breakIterator.setText(text);
        for (
                int begin = breakIterator.first(), end = breakIterator.next();
                end != BreakIterator.DONE;
                begin = end, end = breakIterator.next()
        ) {
            final String currentSentence;
            if (text.charAt(end - 1) == ' ') {
                currentSentence = text.substring(begin, end - 1);
            } else {
                currentSentence = text.substring(begin, end);
            }

            sentencesCount++;
            sentences.add(currentSentence);

            if (currentSentence.length() > maxSentenceLength.length()) {
                maxSentenceLength = currentSentence;
            }

            if (minSentenceLength.equals("")) {
                minSentenceLength = currentSentence;
            } else {
                if (currentSentence.length() < minSentenceLength.length()) {
                    minSentenceLength = currentSentence;
                }
            }

            averageSentenceLength = (averageSentenceLength * (sentencesCount - 1)
                    + currentSentence.length()) / sentencesCount;

            final Collator collator = Collator.getInstance(localeText);
            collator.setStrength(Collator.IDENTICAL);
            if (maxSentence.equals("")) {
                maxSentence = currentSentence;
            } else {
                if (collator.compare(currentSentence, maxSentence) > 0) {
                    maxSentence = currentSentence;
                }
            }

            if (minSentence.equals("")) {
                minSentence = currentSentence;
            } else {
                if (collator.compare(currentSentence, minSentence) < 0) {
                    minSentence = currentSentence;
                }
            }
        }

        differentSentencesCount = sentences.size();
    }

    public int getSentencesCount() {
        return sentencesCount;
    }

    public String getMinSentence() {
        return minSentence;
    }

    public String getMaxSentence() {
        return maxSentence;
    }

    public String getMinSentenceLength() {
        return minSentenceLength;
    }

    public String getMaxSentenceLength() {
        return maxSentenceLength;
    }

    public String getAverageSentenceLength() {
        return outFormat.format(averageSentenceLength);
    }

    public int getDifferentSentencesCount() {
        return differentSentencesCount;
    }
}
