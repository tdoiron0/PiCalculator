import java.math.BigDecimal;

public class App {
    public static void main(String[] args) throws Exception {
        String numSrc1 = "90812980329000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String numSrc2 = "828";

        BlockDecimal num1 = new BlockDecimal(numSrc1);
        BlockDecimal num2 = new BlockDecimal(numSrc2);

        BlockDecimal result = num1.add(num2);

        result.print();

        BigDecimal actual1 = new BigDecimal(numSrc1);
        BigDecimal actual2 = new BigDecimal(numSrc2);

        BigDecimal actualResult = actual1.add(actual2);

        System.out.println("Actual result: " + actualResult);
    }
}
