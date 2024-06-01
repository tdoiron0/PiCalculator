import java.math.BigDecimal;
import java.math.MathContext;

import Util.DebugTimer;

public class PiCalculator {
    public static String getPiToDigit(int n) {
        DebugTimer timer = new DebugTimer(String.format("Calculating to %d digit", n));

        MathContext mc = new MathContext(n);
        timer.split("Creating context");

        BigDecimal sum = new BigDecimal(0, mc);
        timer.split("Creating sum");

        timer.dumpToLog();

        for (int k = 0; k < n; k++) {
            timer = new DebugTimer(String.format("Iter%d", k));

            BigDecimal term1 = new BigDecimal(4, mc).divide(new BigDecimal(8 * k + 1, mc), mc);
            BigDecimal term2 = new BigDecimal(2, mc).divide(new BigDecimal(8 * k + 4, mc), mc);
            BigDecimal term3 = new BigDecimal(1, mc).divide(new BigDecimal(8 * k + 5, mc), mc);
            BigDecimal term4 = new BigDecimal(1, mc).divide(new BigDecimal(8 * k + 6, mc), mc);
            timer.split("Creating main terms");
            
            BigDecimal base = new BigDecimal(16, mc);
            base = base.pow(k);
            BigDecimal term5 = new BigDecimal(1, mc).divide(base);
            timer.split("Creating exponent terms");

            BigDecimal term = term5.multiply(term1.subtract(term2, mc).subtract(term3, mc).subtract(term4, mc));
            timer.split(String.format("Iter%d: Finding pi term", k));

            sum = sum.add(term, mc);
            timer.split(String.format("Iter%d: Adding pi term", k));

            timer.dumpToLog();
        }

        return sum.toString();
    }
}
