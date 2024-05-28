import java.util.Random;

import Symbolics.Number;

public class App {
    public static void main(String[] args) throws Exception {
        /* 
        for (int i = 0; i < 100; ++i) {
            System.out.println(genRandNumber());
        } 
        */

        Number num1 = new Number(genRandNumber());
        Number num2 = new Number(genRandNumber());

        System.out.println(num1 + " + " + num2 + " = " + num1.add(num2));
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
