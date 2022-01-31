import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

public class StatisticGroupComporator implements Comparator<Map.Entry<Double, String>> {
    @Override
    public int compare(Map.Entry<Double, String> a, Map.Entry<Double, String> b) {
        return new BigDecimal(a.getKey()).compareTo(new BigDecimal(b.getKey()));
    }
}
