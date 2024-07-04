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
            appendDigit(src.charAt(i) - 48);
        } 
    }

    public int getNumBlocks() { return numBlocks; } 

    public BlockDecimal add(BlockDecimal oper) {
        try {
            BlockDecimal result = new BlockDecimal("");
            int i = numBlocks - 1;
            int j = oper.getNumBlocks() - 1;
            int carry = 0;
            while (i >= 0 && j >= 0) {
                List<Integer> block1 = getBlock(i);
                List<Integer> block2 = oper.getBlock(i);

                Object[] opRes = addBlock(block1, block2, carry);
                List<Integer> newDigits = (List<Integer>)opRes[0];
                carry = (Integer)opRes[1];

                result.appendDigits(newDigits);

                --i;
                --j;
            }

            while (i >= 0) {
                List<Integer> temp = getBlock(i);
                int total = temp.getFirst() + carry;
                int digit = total % 10;
                carry = total / 10;
                
                --i;
            }
            while (j >= 0) {

                --j;
            }

            return result;
        } catch (IOException e) {
            System.out.println("Addition failed:\n" + e.toString());
            return null;
        }
    }
    public void print() {
        System.out.println("folder path: " + folder.getAbsolutePath());
        System.out.println("num blocks: " + numBlocks);
        System.out.print("Digits: ");

        for (int i = numBlocks - 1; i >= 0; --i) {
            try {
                List<Integer> currBlock = getBlock(i);
                String sb = "";
                for (Integer it : currBlock) {
                    sb = it + sb;
                }
                System.out.print(sb.toString());
            } catch (IOException e) {
                System.out.println("ERROR::failed to print to console:\n" + e.toString());
                return;
            }
        }
        System.out.println("");
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

        if (carry == 0 && resultDigits.size() == MAX_BLOCK_SIZE) {
            resultDigits.addFirst(carry);
            Object[] result = { resultDigits, 0 };
            return result;
        } else {
            Object[] result = { resultDigits, carry };
            return result;
        }
    }
    public List<Integer> getBlock(int index) throws IOException {
        String data = Files.readString(Paths.get(blockPath(index)));
        String[] dataParsed = data.substring(1, data.length() - 1).split(", ");
        List<Integer> result = new ArrayList<>();
        for (String it : dataParsed) {
            result.add(Integer.parseInt(it));
        }
        return result;
    }
    public List<Integer> getBlockFront() throws IOException {
        return (numBlocks == 0) ? new ArrayList<>() : getBlock(numBlocks - 1);
    }
    public List<Integer> getBlockLast() throws IOException, IllegalAccessException {
        if (numBlocks == 0) {
            throw new IllegalAccessException("ERROR::Cannot access last block if no blocks exist");
        }

        return getBlock(0);
    }
    private int blockFrontIndex() {
        return (numBlocks == 0) ? 0 : numBlocks - 1;
    }
    private int blockBackIndex() {
        return 0;
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
    private void replaceFrontBlock(List<Integer> digits) {
        try {
            writeBlock(digits, blockFrontIndex());
        } catch (IOException e) {
            System.out.println("ERROR::failed to replaace front block\n" + e);
            return;
        }
    }
    private void appendDigits(List<Integer> digits) {
        try {
            List<Integer> lastDigits = getBlockFront();
            for (int i = digits.size() - 1; i >= 0; --i) {
                if (lastDigits.size() + 1 == MAX_BLOCK_SIZE) {
                    replaceFrontBlock(lastDigits);
                    lastDigits = new ArrayList<>();
                    ++numBlocks;
                }
                lastDigits.addFirst(digits.get(i));
                digits.removeFirst();
            }
            if (numBlocks == 0) {
                writeBlock(lastDigits, 0);
                numBlocks++;
            } else {
                writeBlock(lastDigits, numBlocks - 1);
            }
        } catch (IOException e) {
            System.out.println("ERROR::failed to add digits to decimal:\n" + e.toString());
            return;
        }
    }
    private void appendDigit(int digit) {
        List<Integer> list = new ArrayList<>();
        list.add(digit);
        appendDigits(list);
    }
    private String blockPath(int index) {
        return folder.getAbsolutePath() + "\\" + index + ".txt";
    }
}
