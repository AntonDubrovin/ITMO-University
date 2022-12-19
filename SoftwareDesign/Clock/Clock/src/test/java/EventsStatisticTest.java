import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static org.junit.jupiter.api.Assertions.*;

class EventsStatisticTest {
    SettableClock clock;
    EventsStatistic eventsStatistic;

    @BeforeEach
    public void setUp() {
        clock = new SettableClock(Instant.now());
        eventsStatistic = new EventsStatisticImpl(clock);
    }


    @Test
    public void testStatisticByName() {
        eventsStatistic.incEvent("Event1");
        eventsStatistic.incEvent("Event1");
        eventsStatistic.incEvent("Event2");

        assertEquals(eventsStatistic.getEventStatisticByName("Event1"), 1.0 / 30);
    }

    @Test
    public void testAllStatistic() {
        eventsStatistic.incEvent("Event1");
        clock.plus(30, ChronoUnit.MINUTES);

        eventsStatistic.incEvent("Event2");
        eventsStatistic.incEvent("Event2");
        clock.plus(30, ChronoUnit.MINUTES);

        eventsStatistic.incEvent("Event1");
        eventsStatistic.incEvent("Event3");


        assertEquals(1.0 / 60.0, eventsStatistic.getEventStatisticByName("Event1"));
        assertEquals(1.0 / 30.0, eventsStatistic.getEventStatisticByName("Event2"));
        assertEquals(1.0 / 60.0, eventsStatistic.getEventStatisticByName("Event3"));
    }

    private static class SettableClock implements Clock {
        private Instant now;

        public SettableClock(Instant now) {
            this.now = now;
        }

        public void setNow(Instant now) {
            this.now = now;
        }

        @Override
        public Instant now() {
            return now;
        }

        public void plus(long value, TemporalUnit unit) {
            setNow(now.plus(value, unit));
        }
    }
}