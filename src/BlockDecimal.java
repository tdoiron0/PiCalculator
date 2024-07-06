import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class BlockDecimal {
    public static final int MAX_BLOCK_SIZE = 2;
    public static final String MATH_ENV_FILE_PATH = "testdata/";

    private File folder = null;
    private int numBlocks = 0;
    private boolean isNegative = false;

    public BlockDecimal(String src) {
        this.folder = new File(MATH_ENV_FILE_PATH + MathEnvironment.getId());

        if (src.charAt(0) == '-') {
            isNegative = true;
        }
        for (int i = src.length() - 1; i >= 0; --i) {
            appendDigit(src.charAt(i) - 48);
        } 
    }

    public int getNumBlocks() { return numBlocks; } 

    public BlockDecimal add(BlockDecimal oper) {
        try {
            BlockDecimal result = new BlockDecimal("");
            int i = 0;
            int j = 0;
            int carry = 0;
            while (i < numBlocks && j < oper.getNumBlocks()) {
                List<Integer> block1 = getBlock(i);
                List<Integer> block2 = oper.getBlock(i);

                Object[] opRes = addBlock(block1, block2, carry);
                List<Integer> newDigits = (List<Integer>)opRes[0];
                carry = (Integer)opRes[1];

                result.appendDigits(newDigits);

                ++i;
                ++j;
            }

            while (i < numBlocks) {
                List<Integer> tempBlock = getBlock(i);
                for (int k = tempBlock.size() - 1; k >= 0; --k) {
                    int total = tempBlock.get(k) + carry;
                    int digit = total % 10;
                    carry = total / 10;

                    result.appendDigit(digit);
                }
                
                ++i;
            }
            while (j < oper.getNumBlocks()) {
                List<Integer> tempBlock = oper.getBlock(j);
                for (int k = tempBlock.size() - 1; k >= 0; --k) {
                    int total = tempBlock.get(k) + carry;
                    int digit = total % 10;
                    carry = total / 10;

                    result.appendDigit(digit);
                }

                ++j;
            }

            if (carry != 0) {
                result.appendDigit(carry);
            }

            return result;
        } catch (IOException e) {
            System.out.println("Addition failed:\n" + e.toString());
            return null;
        }
    }
    public BlockDecimal subtract(BlockDecimal oper) {
        int i = 0;
        int j = 0; 
        while (i < numBlocks && j < oper.getNumBlocks()) {


            ++i;
            ++j;
        }

        return null;
    }

    private Object[] addBlock(List<Integer> oper1, List<Integer> oper2, int prevCarry) {
        int i = 0; 
        int j = 0;
        while (i < oper1.size() && j < oper2.size()) {
            

            ++i;
            ++j;
        }

        return null;
    }
    private Object[] subtractBlock(List<Integer> oper1, List<Integer> oper2, int prevBorrow) {
        int i = oper1.size();
        int j = oper2.size();
        int borrow = prevBorrow;
        while (i >= 0 && j >= 0) {
            int total = oper1.get(i) - oper2.get(j) - borrow;
            if (total < 0) {
                
            }
        }
        
        return null;
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
    public void cleanUp() {
        deleteDir(folder);
    }
    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File it : contents) {
                deleteDir(it);
            }
        }
        if (file.delete() == false) {
            System.out.println("ERROR::failed to delete file\n" + file.toString());
        }
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
                if (lastDigits.size() == MAX_BLOCK_SIZE) {
                    replaceFrontBlock(lastDigits);
                    lastDigits = new ArrayList<>();
                    ++numBlocks;
                }
                lastDigits.addFirst(digits.get(i));
                digits.removeLast();
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

    public void print() {
        System.out.println("folder path: " + folder.getAbsolutePath());
        System.out.println("num blocks: " + numBlocks);
        System.out.print("Digits: ");

        for (int i = numBlocks - 1; i >= 0; --i) {
            try {
                List<Integer> currBlock = getBlock(i);
                StringBuilder sb = new StringBuilder();
                for (Integer it : currBlock) {
                    sb.append(it);
                }
                System.out.print(sb.toString());
            } catch (IOException e) {
                System.out.println("ERROR::failed to print to console:\n" + e.toString());
                return;
            }
        }
        System.out.println("");
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = numBlocks - 1; i >= 0; --i) {
            try {
                List<Integer> currBlock = getBlock(i);
                for (Integer it : currBlock) {
                    sb.append(it);
                }
            } catch (IOException e) {
                System.out.println("ERROR::failed to print to console:\n" + e.toString());
                return null;
            }
        }
        return sb.toString();
    }
}
