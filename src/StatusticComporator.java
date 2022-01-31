import java.util.Comparator;
import java.util.Map;

public class StatusticComporator implements Comparator<Map.Entry<String, Integer>> {
    @Override
    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
        return a.getValue()-b.getValue();
    }
}
