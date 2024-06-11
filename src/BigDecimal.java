import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class BigDecimal {
    public static final int MAX_BLOCK_SIZE = 100;

    private boolean negative = false;
    private int decimalPlace = 0;
    private final String name;
    private final LinkedList<Block> blocks = new LinkedList<>();

    private class Block {
        private ArrayList<Integer> digits;
        private String filePath = "";

        public Block(String filePath) {
            this(new ArrayList<>(), filePath);
        }
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
            
            Object[] result = { new Block(filePath), carry };
            return result;
        }
        public void open() throws IOException {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileInputStream stream = new FileInputStream(file);
            int content;
            while ((content = stream.read()) != -1) {
                digits.addLast(content);
            }
            stream.close();
        }
        public void close() {
            digits = new ArrayList<>();
        }
    }

    public BigDecimal(String name, Block block) {
        this.name = name;
        this.blocks.addFirst(block);
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
            } catch (IOException e) {
                System.out.println("Addition failed:" + e.toString());
                return null;
            }

            Object[] temp = blocks.get(i).add(oper.getBlock(j), prevCarry);
            blocksResult.addFirst((BigDecimal.Block)temp[0]);
            prevCarry = (Integer)temp[1];

            blocks.get(i).close();
            oper.getBlock(j).close();
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

        }

        return null;
    }
}
