public class App {
    public static void main(String[] args) throws Exception {
        BigDecimal num1 = new BigDecimal("num1", "1");
        BigDecimal num2 = new BigDecimal("num2", "3");

        BigDecimal result = num1.add(num2);

        System.err.println(result);
    }
}
