public class App {
    public static void main(String[] args) throws Exception {
        BigDecimal num1 = new BigDecimal("1");
        BigDecimal num2 = new BigDecimal("3");

        BigDecimal result = num1.add(num2);

        System.err.println(result);
    }
}
