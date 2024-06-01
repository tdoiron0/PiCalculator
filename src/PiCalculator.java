import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import Util.DebugTimer;

public class PiCalculator {
    public static String getPiToDigit(int n) {
        DebugTimer timer = new DebugTimer(String.format("Calculating to %d digit", n));

        MathContext mc = new MathContext(n);
        timer.split("Creating context");

        AtomicReference<BigDecimal> sum = new AtomicReference<>(new BigDecimal(0, mc));
        timer.split("Creating sum");

        timer.dumpToLog();

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            final int k = i;
            threads.add(new Thread(new Runnable(){
                @Override
                public void run() {
                    //DebugTimer timer = new DebugTimer(String.format("Iter%d", k));

                    BigDecimal term1 = new BigDecimal(4, mc).divide(new BigDecimal(8 * k + 1, mc), mc);
                    BigDecimal term2 = new BigDecimal(2, mc).divide(new BigDecimal(8 * k + 4, mc), mc);
                    BigDecimal term3 = new BigDecimal(1, mc).divide(new BigDecimal(8 * k + 5, mc), mc);
                    BigDecimal term4 = new BigDecimal(1, mc).divide(new BigDecimal(8 * k + 6, mc), mc);
                    //timer.split("Creating main terms");
            
                    BigDecimal base = new BigDecimal(16, mc);
                    base = base.pow(k);
                    BigDecimal term5 = new BigDecimal(1, mc).divide(base);
                    //timer.split("Creating exponent terms");

                    BigDecimal term = term5.multiply(term1.subtract(term2, mc).subtract(term3, mc).subtract(term4, mc));
                    //timer.split("Finding pi term");

                    AsyncAtomicAdder(sum, term, mc);
                    //timer.split("Adding pi term");

                    //timer.dumpToLog();
                }

            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        return sum.toString();
    }

    public static synchronized void AsyncAtomicAdder(AtomicReference<BigDecimal> num, BigDecimal term, MathContext mc) {
        num.set(num.get().add(term, mc));
    }
}
