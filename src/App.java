import java.io.BufferedInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import Util.DebugTimer;

public class App {
    public static final int NUM1_MAX_VALUE = 1000;
    public static final int NUM2_MAX_VALUE = 1000;

    public static final long LONG_MASK = 0xffffffffL;
    public static void main(String[] args) throws Exception {
        //System.out.println(new File("testdata").getAbsolutePath());
        
        //specificTest();
        //fullTest();

        /*
        int big = 10;
        int little = 8;
        long difference = 0;

        difference = (big & LONG_MASK) - (little & LONG_MASK) + (difference >> 32);
        
        System.out.println((int)difference);
        */

        System.out.println("Integer.MAX_VALUE: " + Integer.MAX_VALUE);
        System.out.println("Long.MAX_VALUE: " + Long.MAX_VALUE);

        byte[] src1 = { 0b00000001, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000 };
        byte[] src2 = { (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1 };

        BigInteger x = new BigInteger(1, src1); 
        BigInteger y = new BigInteger(1, src2);

        BigInteger z = x.subtract(y);

        System.out.println(z);
    }

    public static void specificTest() {
        String numSrc1 = "2";
        String numSrc2 = "3";
        
        DebugTimer timer = new DebugTimer("Specific test");

        BlockDecimal num1 = new BlockDecimal(numSrc1);
        BlockDecimal num2 = new BlockDecimal(numSrc2);
        timer.split("Initializing block decimals");

        num1.print();
        num2.print();
        timer.split("Printing block decimals");

        BlockDecimal result = num1.add(num2);
        timer.split("Adding block decimals");

        result.print();
        timer.split("Printing result");

        BigDecimal actual1 = new BigDecimal(numSrc1);
        BigDecimal actual2 = new BigDecimal(numSrc2);

        BigDecimal actualResult = actual1.add(actual2);

        System.out.println("Actual result: " + actualResult);
        timer.split("Verifying result");

        num1.cleanUp();
        num2.cleanUp();
        result.cleanUp();
        timer.split("Cleaning up block decimals");
    }

    public static void fullTest() {
        for (int i = 0; i < NUM1_MAX_VALUE; ++i) {
            for (int j = 0; j < NUM2_MAX_VALUE; ++j) {
                BlockDecimal num1 = new BlockDecimal(Integer.toString(i));
                BlockDecimal num2 = new BlockDecimal(Integer.toString(j));

                BlockDecimal result = num1.add(num2);

                BigDecimal actual1 = new BigDecimal(Integer.toString(i));
                BigDecimal actual2 = new BigDecimal(Integer.toString(j));

                BigDecimal actualResult = actual1.add(actual2);

                if (!result.toString().equals(actualResult.toString())) {
                    System.out.println("Test failed\n" + result.toString() + "\nDoes not equal\n" + actualResult);
                    System.out.println("i = " + i);
                    System.out.println("j = " + j);
                }

                num1.cleanUp();
                num2.cleanUp();
                result.cleanUp();
            }
        }
        System.out.println("Test end");
    }
}
