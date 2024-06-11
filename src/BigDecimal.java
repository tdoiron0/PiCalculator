import java.util.ArrayList;
import java.util.LinkedList;

public class BigDecimal {
    public static final int MAX_BLOCK_SIZE = 100;

    public class Block {
        private final ArrayList<Integer> digits;
        private String filePath = "";

        public Block(String filePath) {
            this(new ArrayList<>(), filePath);
        }
        public Block(ArrayList<Integer> digits, String filePath) {
            this.digits = digits;
            this.filePath = filePath;
        }

        public Object[] add(Block oper) {
            Object[] result = { new Block(filePath), 0};
            ArrayList<Integer> resultDigits = new ArrayList<>();
            
            
            return result;
        }
    }
    
    private final ArrayList<Block> blocks = new ArrayList<>();

    public BigDecimal(BigDecimal.Block block) {
        blocks.add(block);
    }
}
