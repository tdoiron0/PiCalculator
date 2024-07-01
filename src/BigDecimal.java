import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;

public class BigDecimal {
    public static final int MAX_BLOCK_SIZE = 100;
    public static final String MATH_ENV_FILE_PATH = "testdata/";

    private boolean negative = false;
    private int decimalPlace = 0;
    private File folder;
    private final LinkedList<Block> blocks = new LinkedList<>();

    private class Block {
        private ArrayList<Integer> digits;

        public Block(ArrayList<Integer> digits) {
            this.digits = digits;
        }
        private Block(int startValue) {
            digits.addFirst(startValue);
        }

        public int digitLength() { return digits.size(); }
        public Integer getDigit(int index) { return digits.get(index); }

        public Object[] add(Block oper, int prevCarry) {
            ArrayList<Integer> resultDigits = new ArrayList<>();

            int i = digits.size() - 1;
            int j = oper.digitLength() - 1;
            int carry = prevCarry;
            while (i >= 0 && j >= 0) {
                int total = digits.get(i) + oper.getDigit(j) + carry;
                int digit = total % 10;
                carry = total / 10;
                resultDigits.add(digit);

                --i;
                --j;
            }

            while (i >= 0) {
                int total = digits.get(i) + carry;
                int digit = total % 10; 
                carry = total / 10;
                resultDigits.add(digit);
                --i;
            }
            while (j >= 0) {
                int total = oper.getDigit(j) + carry;
                int digit = total % 10; 
                carry = total / 10;
                resultDigits.add(digit);
                --j;
            }
            
            Object[] result = { new Block(resultDigits), carry };
            return result;
        }
        public void open(String filePath) throws IOException {
            if (filePath.length() == 0) {
                throw new IllegalStateException("Must set filepath before opening file");
            }

            File file = new File(filePath);
            String data = Files.readString(file.toPath());
            String[] dataParsed = data.substring(1, data.length() - 1).split(", ");
            for (String it : dataParsed) {
                digits.add(Integer.parseInt(it));
            }
        }
        public void close() {
            digits = new ArrayList<>();
        }
        public void write(String filePath) throws IOException {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            Files.writeString(new File(filePath).toPath(), digits.toString(), StandardOpenOption.CREATE);
        }

        public void print(String filePath) {
            try {
                open(filePath);
                StringBuilder sb = new StringBuilder();
                for (Integer it : digits) {
                    sb.append(it);
                }
                System.out.print(sb.toString());
            } catch (IOException e) {
                System.out.println("ERROR::failed to open file to convert block into string:\n" + e);
            }
        }
    }

    public BigDecimal(String src) {
        folder = new File(MATH_ENV_FILE_PATH + MathEnvironment.getId());
        
        ArrayList<Integer> digits = new ArrayList<>();
        for (int i = 0; i < src.length(); ++i) {
            digits.add(src.charAt(i) - 48);
        }
        Block startBlock = new Block(digits);
        
        this.blocks.addFirst(startBlock);
        try {
            startBlock.write(folder.getAbsolutePath() + "\\1.txt");
        } catch (IOException e) {
            System.out.println("ERROR::failed to create decimal:\n" + e);
        }
    }

    public boolean isNegative() { return negative; }
    public int getDecimalPlace() { return decimalPlace; } 
    public int getBlockSize() { return blocks.size(); }
    public Block getBlock(int index) { return blocks.get(index); }
    public File getFolder() { return folder; }

    public BigDecimal add(BigDecimal oper) {
        LinkedList<Block> blocksResult = new LinkedList<>();
        File resultFolder = new File(MATH_ENV_FILE_PATH + MathEnvironment.getId());

        int i = blocks.size() - 1;
        int j = oper.getBlockSize() - 1;
        int prevCarry = 0;
        while (i >= 0 && j >= 0) {
            try {
                blocks.get(i).open(folder.getAbsolutePath() + "/" + (i + 1) + ".txt");
                oper.getBlock(j).open(oper.getFolder().getAbsolutePath() + "/" + (i + 1) + ".txt");
                
                Object[] temp = blocks.get(i).add(oper.getBlock(j), prevCarry);
                blocksResult.addFirst((BigDecimal.Block)temp[0]);
                prevCarry = (Integer)temp[1];

                blocksResult.getFirst().write(resultFolder.getAbsolutePath() + "/" + (i + 1) + ".txt");

                blocks.get(i).close();
                oper.getBlock(j).close();
                blocksResult.getFirst().close();
            } catch (IOException e) {
                System.out.println("Addition failed:\n" + e.toString());
                return null;
            }
        }

        Block carryBlock = new Block(prevCarry);
        while (i >= 0) {
            Object[] temp = blocks.get(i).add(carryBlock, 0);
            blocksResult.add((BigDecimal.Block)temp[0]);
            carryBlock = new Block((Integer)temp[1]);
        }
        while (j >= 0) {
            Object[] temp = oper.getBlock(j).add(carryBlock, 0);
            blocksResult.add((BigDecimal.Block)temp[0]);
            carryBlock = new Block((Integer)temp[1]);
        }
        
        if (carryBlock.getDigit(0) != 0) {
            blocks.addFirst(carryBlock);
        }

        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Block it : blocks) {
            sb.append(it.toString());
        }
        return sb.toString();
    }
}
