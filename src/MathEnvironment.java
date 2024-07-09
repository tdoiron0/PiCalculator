import java.util.HashSet;
import java.util.Set;

public class MathEnvironment {
    public static final String MATH_ENV_FILE_PATH = "testdata/";
    private static int nextId = 1;

    public static String getId() {
        return "num" + ((nextId == Integer.MAX_VALUE) ? (nextId = 0) : nextId++);
    }
}
