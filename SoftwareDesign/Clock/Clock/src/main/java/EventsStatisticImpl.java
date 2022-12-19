import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventsStatisticImpl implements EventsStatistic {
    private final Clock clock;
    private final Map<String, List<Instant>> events = new HashMap<>();

    public EventsStatisticImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void incEvent(String name) {
        if (!events.containsKey(name)) {
            events.put(name, new ArrayList<>());
        }

        events.get(name).add(clock.now());
    }

    @Override
    public double getEventStatisticByName(String name) {
        Instant hourAgo = getHourAgo(clock.now());
        return getEventStatisticByNameAfterInstant(name, hourAgo);
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        Instant hourAgo = getHourAgo(clock.now());
        return events.keySet().stream().collect(Collectors.toMap(Function.identity(), name -> getEventStatisticByNameAfterInstant(name, hourAgo)));
    }

    @Override
    public void printStatistic() {
        for (var stat : getAllEventStatistic().entrySet()) {
            System.out.println("RPM for " + stat.getValue() + " - " + stat.getKey());
        }
    }

    private double getEventStatisticByNameAfterInstant(String name, Instant start) {
        return events.getOrDefault(name, List.of()).stream().filter(instant -> instant.isAfter(start)).count() / 60.0;
    }

    private Instant getHourAgo(Instant from) {
        return from.minus(Duration.ofHours(1));
    }
}