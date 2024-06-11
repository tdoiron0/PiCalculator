import java.util.ArrayList;
import java.util.LinkedList;

public class BigDecimal {
    public static final int MAX_BLOCK_SIZE = 100;

    private boolean negative = false;
    private int decimalPlace = 0;
    private final LinkedList<Block> blocks = new LinkedList<>();

    private class Block {
        private final ArrayList<Integer> digits;
        private String filePath = "";

        public Block(String filePath) {
            this(new ArrayList<>(), filePath);
        }
        public Block(ArrayList<Integer> digits, String filePath) {
            this.digits = digits;
            this.filePath = filePath;
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
    }

    public BigDecimal(BigDecimal.Block block) {
        blocks.addFirst(block);
    }

    public boolean isNegative() { return negative; }
    public int getDecimalPlace() { return decimalPlace; } 
    public int getBlockSize() { return blocks.size(); }
    public Block getBlock(int index) { return blocks.get(index); }

    public BigDecimal add(BigDecimal oper) {
        LinkedList<Block> resultBlocks = new LinkedList<>();

        int i = blocks.size() - 1;
        int j = oper.getBlockSize() - 1;
        int prevCarry = 0;
        while (i >= 0 && j >= 0) {
            Object[] block = blocks.get(i).add(oper.getBlock(j), prevCarry);
            resultBlocks.addFirst((BigDecimal.Block)block[0]);
            prevCarry = (Integer)block[1];

            --i;
            --j;
        }

        

        return null;
    }
}
