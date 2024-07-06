import java.io.File;
import java.math.BigDecimal;

public class App {
    public static final int NUM1_MAX_VALUE = 1000;
    public static final int NUM2_MAX_VALUE = 1000;
    public static void main(String[] args) throws Exception {
        //System.out.println(new File("testdata").getAbsolutePath());
        
        //specificTest();
        fullTest();
    }

    public static void specificTest() {
        String numSrc1 = "0";
        String numSrc2 = "100";

        BlockDecimal num1 = new BlockDecimal(numSrc1);
        BlockDecimal num2 = new BlockDecimal(numSrc2);

        num1.print();
        num2.print();

        BlockDecimal result = num1.add(num2);

        result.print();

        BigDecimal actual1 = new BigDecimal(numSrc1);
        BigDecimal actual2 = new BigDecimal(numSrc2);

        BigDecimal actualResult = actual1.add(actual2);

        System.out.println("Actual result: " + actualResult);

        num1.cleanUp();
        num2.cleanUp();
        result.cleanUp();
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
