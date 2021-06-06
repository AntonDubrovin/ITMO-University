package info.kgeorgiy.ja.dubrovin.statistics;

import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class DateStatistics {
    private int dateCount = 0;
    private Date minDate = null;
    private Date maxDate = null;
    private Date averageDate;
    private int differentDate = 0;
    private long sum = 0;
    private final Locale localeText;
    private final DateFormat outFormat;

    public DateStatistics(final Locale
                                  localeText, final Locale localeOut) {
        this.localeText = localeText;
        outFormat = DateFormat.getDateInstance(DateFormat.SHORT, localeOut);
    }

    public void dateStatisticsCount(final String text) {
        final Set<Date> dates = new HashSet<>();

        final BreakIterator breakIterator = BreakIterator.getLineInstance(localeText);
        breakIterator.setText(text);
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, localeText);
        for (
                int begin = breakIterator.first(), end = breakIterator.next();
                end != BreakIterator.DONE;
                begin = end, end = breakIterator.next()
        ) {
            try {
                final Date currentDate = dateFormat.parse(text.substring(begin, end));

                dateCount++;
                dates.add(currentDate);

                if (minDate == null) {
                    minDate = currentDate;
                } else {
                    if (currentDate.before(minDate)) {
                        minDate = currentDate;
                    }
                }

                if (maxDate == null) {
                    maxDate = currentDate;
                } else {
                    if (currentDate.after(maxDate)) {
                        maxDate = currentDate;
                    }
                }

                sum += currentDate.getTime();
            } catch (final ParseException ignored) {
            }
        }

        averageDate = new Date(sum / dateCount);
        differentDate = dates.size();
    }

    public int getDateCount() {
        return dateCount;
    }

    public int getDifferentDate() {
        return differentDate;
    }

    public String getMinDate() {
        return outFormat.format(minDate);
    }

    public String getMaxDate() {
        return outFormat.format(maxDate);
    }

    public String getAverageDate() {
        return outFormat.format(averageDate);
    }
}
