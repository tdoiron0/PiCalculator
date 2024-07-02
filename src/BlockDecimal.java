import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BlockDecimal {
    public static final int MAX_BLOCK_SIZE = 100;
    public static final String MATH_ENV_FILE_PATH = "testdata/";

    private boolean negative = false;
    private int decimalPlace = 0;
    private File folder;
    private LinkedList<Block> blocks = new LinkedList<>();

    private class Block {
        private List<Integer> digits;

        public Block(List<Integer> digits) {
            this.digits = digits;
        }
        private Block(int startValue) {
            digits = new ArrayList<>();
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
                resultDigits.addFirst(digit);

                --i;
                --j;
            }

            while (i >= 0) {
                int total = digits.get(i) + carry;
                int digit = total % 10; 
                carry = total / 10;
                resultDigits.addFirst(digit);
                --i;
            }
            while (j >= 0) {
                int total = oper.getDigit(j) + carry;
                int digit = total % 10; 
                carry = total / 10;
                resultDigits.addFirst(digit);
                --j;
            }

            if (carry != 0 && resultDigits.size() < MAX_BLOCK_SIZE) {
                resultDigits.addFirst(carry);
                Object[] result = { new Block(resultDigits), 0 };
                return result;
            } else {
                Object[] result = { new Block(resultDigits), carry };
                return result;
            }
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
            } else {
                file.delete();
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
                System.out.println("ERROR::failed to open file to print:\n" + e);
            }
        }
    }

    public BlockDecimal(String src) {
        folder = new File(MATH_ENV_FILE_PATH + MathEnvironment.getId());
        
        List<List<Integer>> digits = new ArrayList<>();
        List<Integer> currList = new ArrayList<>();
        for (int i = src.length() - 1; i >= 0; --i) {
            currList.addFirst(src.charAt(i) - 48);
            if ((i + 1) % MAX_BLOCK_SIZE == 0) {
                digits.addFirst(currList);
                currList = new ArrayList<>();
            }
        }
        if (currList.size() > 0) {
            digits.addFirst(currList);
        }

        for (int i = digits.size() - 1; i >= 0; --i) {
            Block newBlock = new Block(digits.get(i));
            try {
                newBlock.write(folder.getAbsolutePath() + "/" + (i + 1) +".txt");
            } catch (IOException e) {
                System.out.println("ERROR::failed to create decimal:\n" + e);
            }
            newBlock.close();

            this.blocks.addFirst(newBlock);
        }
    }
    private BlockDecimal(LinkedList<Block> blocks, File folder) {
        this.blocks = blocks;
        this.folder = folder;
    }

    public boolean isNegative() { return negative; }
    public int getDecimalPlace() { return decimalPlace; } 
    public int getBlockSize() { return blocks.size(); }
    public Block getBlock(int index) { return blocks.get(index); }
    public File getFolder() { return folder; }

    public BlockDecimal add(BlockDecimal oper) {
        LinkedList<Block> blocksResult = new LinkedList<>();
        File resultFolder = new File(MATH_ENV_FILE_PATH + MathEnvironment.getId());

        int i = blocks.size() - 1;
        int j = oper.getBlockSize() - 1;
        int prevCarry = 0;
        while (i >= 0 && j >= 0) {
            try {
                blocks.get(i).open(folder.getAbsolutePath() + "/" + (i + 1) + ".txt");
                oper.getBlock(j).open(oper.getFolder().getAbsolutePath() + "/" + (j + 1) + ".txt");
                
                Object[] temp = blocks.get(i).add(oper.getBlock(j), prevCarry);
                blocksResult.addFirst((BlockDecimal.Block)temp[0]);
                prevCarry = (Integer)temp[1];

                blocksResult.getFirst().write(resultFolder.getAbsolutePath() + "/" + (Math.max(i, j) + 1) + ".txt");

                blocks.get(i).close();
                oper.getBlock(j).close();
                blocksResult.getFirst().close();
            } catch (IOException e) {
                System.out.println("Addition failed:\n" + e.toString());
                return null;
            }

            --i;
            --j;
        }

        Block carryBlock = new Block(prevCarry);
        while (i >= 0) {
            Object[] temp = blocks.get(i).add(carryBlock, 0);
            blocksResult.add((BlockDecimal.Block)temp[0]);
            carryBlock = new Block((Integer)temp[1]);
            --i;
        }
        while (j >= 0) {
            Object[] temp = oper.getBlock(j).add(carryBlock, 0);
            blocksResult.add((BlockDecimal.Block)temp[0]);
            carryBlock = new Block((Integer)temp[1]);
            --j;
        }
        
        if (carryBlock.digitLength() != 0) {
            blocks.addFirst(carryBlock);
        }

        return new BlockDecimal(blocksResult, resultFolder);
    }
    public void cleanUp() {

    }

    public void print() {
        System.out.println("folder path: " + folder.getAbsolutePath());
        System.out.println("num blocks: " + blocks.size());
        System.out.print("Digits: ");
        for (int i = 1; i <= blocks.size(); ++i) {
            blocks.get(i - 1).print(folder.getAbsolutePath() + "/" + i + ".txt");
        }
        System.out.println("");
    }
}
