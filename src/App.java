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
        try {
            open("testdata\\test.txt");
        } catch (IOException e) {
            System.out.println("ERROR:\n" + e.toString());
        }
    }

    public static void open(String filePath) throws IOException {
        ArrayList<Integer> digits = new ArrayList<>();

        File file = new File(filePath);
        System.out.println(file.getAbsolutePath() + ":");
        
        if (!file.exists()) {
            file.createNewFile();
        }

        FileInputStream stream = new FileInputStream(file);
        while (stream.available() > 0) {
            digits.addLast(stream.read() - 48);
        }
        System.out.println(digits);
    }
}
