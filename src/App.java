import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        
    }

    public static String genRandNumber() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        int resultlength = Math.abs(rand.nextInt() % 100);
        for (int i = 0; i < resultlength; ++i) {
            sb.append(Math.abs(rand.nextInt() % 9) + 1);
        }
        return sb.toString();
    }
}
