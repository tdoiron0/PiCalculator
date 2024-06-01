import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        Bailey(100);
    }

    public static void Bailey(int n) {
        MathContext mc = new MathContext(n);
        BigDecimal sum = new BigDecimal(0, mc);

        for (int k = 0; k < n; k++) {
            BigDecimal term1 = new BigDecimal(4, mc).divide(new BigDecimal(8 * k + 1, mc), mc);
            BigDecimal term2 = new BigDecimal(2, mc).divide(new BigDecimal(8 * k + 4, mc), mc);
            BigDecimal term3 = new BigDecimal(1, mc).divide(new BigDecimal(8 * k + 5, mc), mc);
            BigDecimal term4 = new BigDecimal(1, mc).divide(new BigDecimal(8 * k + 6, mc), mc);
            
            BigDecimal base = new BigDecimal(16, mc);
            base = base.pow(k);
            BigDecimal term5 = new BigDecimal(1, mc).divide(base);

            BigDecimal term = term5.multiply(term1.subtract(term2, mc).subtract(term3, mc).subtract(term4, mc));

            sum = sum.add(term, mc);
        }
        System.out.println(sum);
    }

    public static void Lebiniz() {
        MathContext mc = new MathContext(10);
        BigDecimal sum = new BigDecimal(0, mc);
        int n = 1000;

        for (int i = 1; i <= n; i++) {
            BigDecimal term = new BigDecimal(4, mc).divide(new BigDecimal(2 * i - 1, mc), mc);
            if (i % 2 == 0) {
                sum = sum.subtract(term, mc);
            } else {
                sum = sum.add(term, mc);
            }
        }
        System.out.println(sum);
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
