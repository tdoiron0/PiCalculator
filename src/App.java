import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        int n = 100000;
        MathContext mc = new MathContext(n);

        BigDecimal base = new BigDecimal(16, mc);
        base = base.pow(n);
        BigDecimal term5 = new BigDecimal(1, mc).divide(base);

        System.out.println("base length: " + base.toString().length());
        System.out.println("term length: " + term5.toString().length());
    }
}
