import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println(PiCalculator.getPiToDigit(10000));

        /*
        for (int i = 0; i < 10000; ++i) {
            System.out.printf("Iteration %d: %s\n", i, PiCalculator.getPiToDigit(i));
        }
        */
    }
}
