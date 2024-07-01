import java.util.HashSet;
import java.util.Set;

public class MathEnvironment {
    private static int nextId = 1;

    public static String getId() {
        return "num" + ((nextId == Integer.MAX_VALUE) ? (nextId = 0) : nextId++);
    }
}
