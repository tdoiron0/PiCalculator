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
    private final String name;
    private final LinkedList<Block> blocks = new LinkedList<>();

    private class Block {
        private ArrayList<Integer> digits;
        private String filePath = "";

        public Block(ArrayList<Integer> digits, String filePath) {
            this.digits = digits;
            this.filePath = filePath;
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
            
            Object[] result = { new Block(resultDigits, filePath), carry };
            return result;
        }
        public void open() throws IOException {
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
        public void write() throws IOException {
            Files.writeString(new File(filePath).toPath(), digits.toString(), StandardOpenOption.CREATE);
        }

        public String toString() {
            try {
                open();

                StringBuilder sb = new StringBuilder();
                for (Integer it : digits) {
                    sb.append(it);
                }
                close();
                return sb.toString();
            } catch (IOException e) {
                System.out.println("ERROR::failed to open file to convert block into string:\n" + e);
            }
            return "";
        }
    }

    public BigDecimal(String name, String src) {
        ArrayList<Integer> digits = new ArrayList<>();
        for (int i = 0; i < src.length(); ++i) {
            digits.add(src.charAt(i) - 30);
        }
        Block startBlock = new Block(digits, src);
        
        this.name = name;
        this.blocks.addFirst(startBlock);
        try {
            startBlock.write();
        } catch (IOException e) {
            System.out.println("ERROR::failed to create decimal:\n" + e);
        }
    }
    public BigDecimal(String name, Block block) {
        this.name = name;
        this.blocks.addFirst(block);
        try {
            block.write();
        } catch (IOException e) {
            System.out.println("ERROR::failed to create decimal:\n" + e);
        }
    }

    public boolean isNegative() { return negative; }
    public int getDecimalPlace() { return decimalPlace; } 
    public int getBlockSize() { return blocks.size(); }
    public Block getBlock(int index) { return blocks.get(index); }

    public BigDecimal add(BigDecimal oper) {
        LinkedList<Block> blocksResult = new LinkedList<>();

        int i = blocks.size() - 1;
        int j = oper.getBlockSize() - 1;
        int prevCarry = 0;
        while (i >= 0 && j >= 0) {
            try {
                blocks.get(i).open();
                oper.getBlock(j).open();
                
                Object[] temp = blocks.get(i).add(oper.getBlock(j), prevCarry);
                blocksResult.addFirst((BigDecimal.Block)temp[0]);
                prevCarry = (Integer)temp[1];

                blocksResult.getFirst().write();

                blocks.get(i).close();
                oper.getBlock(j).close();
                blocksResult.getFirst().close();
            } catch (IOException e) {
                System.out.println("Addition failed:" + e.toString());
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
