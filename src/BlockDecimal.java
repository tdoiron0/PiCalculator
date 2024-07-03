import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class BlockDecimal {
    public static final int MAX_BLOCK_SIZE = 100;
    public static final String MATH_ENV_FILE_PATH = "testdata/";

    private File folder = null;
    private int numBlocks = 0;

    public BlockDecimal(String src) {
        this.folder = new File(MATH_ENV_FILE_PATH + MathEnvironment.getId());

        for (int i = src.length() - 1; i >= 0; --i) {
            addDigits(src.charAt(i) - 48);
        } 
    }

    public int getNumBlocks() { return numBlocks; } 

    public BlockDecimal add(BlockDecimal oper) {
        BlockDecimal result = new BlockDecimal("");
        int i = numBlocks - 1;
        int j = oper.getNumBlocks() - 1;
        int carry = 0;
        while (i >= 0 && j >= 0) {
            try {
                List<Integer> block1 = getBlock(i);
                List<Integer> block2 = oper.getBlock(i);

                Object[] opRes = addBlock(block1, block2, carry);
                List<Integer> newDigits = (List<Integer>)opRes[0];
                carry = (Integer)opRes[1];

                result.addDigits(newDigits);
            } catch (IOException e) {
                System.out.println("Addition failed:\n" + e.toString());
                return null;
            }
        }
        return result;
    }
    public void print() {
        
    }

    private Object[] addBlock(List<Integer> oper1, List<Integer> oper2, int prevCarry) {
        ArrayList<Integer> resultDigits = new ArrayList<>();

        int i = oper1.size() - 1;
        int j = oper2.size() - 1;
        int carry = prevCarry;
        while (i >= 0 && j >= 0) {
            int total = oper1.get(i) + oper2.get(j) + carry;
            int digit = total % 10;
            carry = total / 10;
            resultDigits.addFirst(digit);

            --i;
            --j;
        }

        while (i >= 0) {
            int total = oper1.get(i) + carry;
            int digit = total % 10; 
            carry = total / 10;
            resultDigits.addFirst(digit);
            --i;
        }
        while (j >= 0) {
            int total = oper2.get(j) + carry;
            int digit = total % 10; 
            carry = total / 10;
            resultDigits.addFirst(digit);
            --j;
        }

        if (carry == 0 && resultDigits.size() < MAX_BLOCK_SIZE) {
            resultDigits.addFirst(carry);
            Object[] result = { resultDigits, 0 };
            return result;
        } else {
            Object[] result = { resultDigits, carry };
            return result;
        }
    }
    public List<Integer> getBlock(int index) throws IOException{
        String data = Files.readString(Paths.get(blockPath(index)));
        String[] dataParsed = data.substring(1, data.length() - 1).split(", ");
        List<Integer> result = new ArrayList<>();
        for (String it : dataParsed) {
            result.add(Integer.parseInt(it));
        }
        return result;
    }
    private void writeBlock(List<Integer> data, int index) throws IOException {
        File file = new File(blockPath(index));
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }

        Files.writeString(file.toPath(), data.toString(), StandardOpenOption.CREATE);
    }
    private void addDigits(List<Integer> digits) {
        try {
            List<Integer> lastDigits = getBlock(numBlocks - 1);
            for (int i = digits.size() - 1; i >= 0; --i) {
                if (lastDigits.size() + 1 == MAX_BLOCK_SIZE) {
                    writeBlock(lastDigits, numBlocks);
                    lastDigits = new ArrayList<>();
                    ++numBlocks;
                }
                lastDigits.addFirst(digits.get(i));
            }
            if (lastDigits.size() != 0) {
                writeBlock(lastDigits, numBlocks);
                ++numBlocks;
            }
        } catch (IOException e) {
            System.out.println("ERROR::failed to add digits to decimal:\n" + e.toString());
            return;
        }
    }
    private void addDigits(int digit) {
        List<Integer> list = new ArrayList<>();
        list.add(digit);
        addDigits(list);
    }
    private String blockPath(int index) {
        return folder.getAbsolutePath() + "/" + index + ".txt";
    }
}
