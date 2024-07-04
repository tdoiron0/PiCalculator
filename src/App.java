import java.io.File;
import java.math.BigDecimal;

public class App {
    public static void main(String[] args) throws Exception {
        //System.out.println(new File("testdata").getAbsolutePath());

        String numSrc1 = "5";
        String numSrc2 = "5";

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
    }
}
