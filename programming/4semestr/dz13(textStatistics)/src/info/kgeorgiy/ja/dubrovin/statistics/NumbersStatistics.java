package info.kgeorgiy.ja.dubrovin.statistics;

import java.text.BreakIterator;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class NumbersStatistics {
    private int numbersCount = 0;
    private double minNumber = Double.MAX_VALUE;
    private double maxNumber = Double.MIN_VALUE;
    private double averageNumber = 0;
    private int differentNumbersCount = 0;
    private final Locale localeText;
    private double sum = 0;
    private final NumberFormat numberFormat;
    private final NumberFormat outFormat;

    public NumbersStatistics(final Locale localeText, final Locale localeOut) {
        this.localeText = localeText;
        this.numberFormat = NumberFormat.getNumberInstance(localeText);
        this.outFormat = NumberFormat.getNumberInstance(localeOut);
        outFormat.setMaximumFractionDigits(3);
    }

    public void numbersStatisticsCount(final String text) {
        final Set<Double> numbers = new HashSet<>();

        final BreakIterator breakIterator = BreakIterator.getLineInstance(localeText);
        breakIterator.setText(text);
        for (
                int begin = breakIterator.first(), end = breakIterator.next();
                end != BreakIterator.DONE;
                begin = end, end = breakIterator.next()
        ) {
            if (text.substring(begin, end).codePoints().noneMatch(Character::isDigit)) {
                continue;
            }

            try {
                final double currentNumber = numberFormat.parse(text.substring(begin, end)).doubleValue();
                numbersCount++;
                sum += currentNumber;
                numbers.add(currentNumber);

                if (currentNumber < minNumber) {
                    minNumber = currentNumber;
                }

                if (currentNumber > maxNumber) {
                    maxNumber = currentNumber;
                }
            } catch (final ParseException ignored) {
            }
        }

        averageNumber = sum / numbersCount;
        differentNumbersCount = numbers.size();
    }

    public int getNumbersCount() {
        return numbersCount;
    }

    public String getMinNumber() {
        return outFormat.format(minNumber);
    }

    public String getMaxNumber() {
        return outFormat.format(maxNumber);
    }

    public String getAverageNumber() {
        return outFormat.format(averageNumber);
    }

    public int getDifferentNumbersCount() {
        return differentNumbersCount;
    }
}
