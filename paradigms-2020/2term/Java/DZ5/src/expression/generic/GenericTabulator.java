package expression.generic;

import expression.type.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GenericTabulator<T extends Number> implements Tabulator {
    private Map<String, Function<String, Type>> tabulator = new HashMap<>();
    {
        tabulator.put("i",  SafeIntType::parse);
        tabulator.put("bi",  BigIntType::parse);
        tabulator.put("l",  LongType::parse);
        tabulator.put("d",  DoubleType::parse);
        tabulator.put("s",  ShortType::parse);
        tabulator.put("u",  UncheckedIntType::parse);
    }

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        return getTabulator(mode).execute(expression, x1, x2, y1, y2, z1, z2);
    }

    private TabulateExecutor<T> getTabulator(String mode) {
        Function<String, Type> function = tabulator.get(mode);
        if (function != null) {
            return new TabulateExecutor<T>(function);
        }
        throw new IllegalArgumentException("Illegal argument: " + mode);
    }
}
