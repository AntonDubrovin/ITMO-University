package info.kgeorgiy.ja.dubrovin.statistics;

import java.text.BreakIterator;
import java.text.Collator;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MoneyStatistics {
    private int sumsCount = 0;
    private int differentSums = 0;
    private final Locale localeText;
    private final NumberFormat numberFormat;
    private final NumberFormat outFormat;
    private double minSums = 0;
    private double maxSums = 0;
    private double sum;
    private double averageSums;

    public MoneyStatistics(final Locale localeText, final Locale localeOut) {
        this.localeText = localeText;
        this.numberFormat = NumberFormat.getCurrencyInstance(localeText);
        this.outFormat = NumberFormat.getCurrencyInstance(localeOut);
        outFormat.setMaximumFractionDigits(3);
    }

    public void moneyStatisticsCount(final String text) {
        final Set<Double> moneys = new HashSet<>();

        final BreakIterator breakIterator = BreakIterator.getLineInstance(localeText);
        breakIterator.setText(text);
        for (
                int begin = breakIterator.first(), end = breakIterator.next();
                end != BreakIterator.DONE;
                begin = end, end = breakIterator.next()
        ) {
            final double currentMoney;
            try {
                currentMoney = numberFormat.parse(text.substring(begin, end)).doubleValue();
                sumsCount++;
                moneys.add(currentMoney);

                final Collator collator = Collator.getInstance(localeText);
                collator.setStrength(Collator.IDENTICAL);

                if (minSums == 0) {
                    minSums = currentMoney;
                } else {
                    if (currentMoney < minSums) {
                        minSums = currentMoney;
                    }
                }

                if (maxSums == 0) {
                    maxSums = currentMoney;
                } else {
                    if (currentMoney > maxSums) {
                        maxSums = currentMoney;
                    }
                }

                sum += currentMoney;
            } catch (final ParseException ignored) {
            }
        }

        averageSums = sum / sumsCount;
        differentSums = moneys.size();
    }

    public int getSumsCount() {
        return sumsCount;
    }

    public String getMinSums() {
        return outFormat.format(minSums);
    }

    public String getMaxSums() {
        return outFormat.format(maxSums);
    }

    public String getAverageSums() {
        return outFormat.format(averageSums);
    }

    public int getDifferentSums() {
        return differentSums;
    }
}
