import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class BlockInteger {
    public static final int MAX_BLOCK_SIZE = 2;
    public static final long LONG_MASK = 0xffffffffL;

    private File folder = null;
    private int numDigits = 0;
    private int numBlocks = 0;
    private int numBytesOnDisk = 0;
    private int numBytesInMem = 0;
    private int signum = 0;
    private int activeBlockIdx = -1;
    private List<Integer> activeBlock = null;

    public BlockInteger(String src) {
        this.folder = new File(MathEnvironment.MATH_ENV_FILE_PATH + MathEnvironment.getId());
    }

    public BlockInteger(int signum, File folder) {
        this.signum = signum;
        this.folder = new File(MathEnvironment.MATH_ENV_FILE_PATH + MathEnvironment.getId());

        File[] files = folder.listFiles();
        for (File it : files) {
            if (it.isFile()) {
                List<Integer> data = readBlock(it);
                File newFile = new File(folder.getAbsolutePath() + "\\" + it.getName());
                writeBlock(newFile, data);
                numDigits += data.size();
                numBlocks++;
                numBytesOnDisk += (data.size() & LONG_MASK) * 4;
            }
        }
    }

    public int getNumDigits() { return numDigits; }
    public int getNumBlocks() { return numBlocks; } 
    public int getSignum() { return signum; }

    /**
     * Returns a BigInteger whose value is (this + val). 
     * Version of code in BigInteger.
     * 
     * @param val value to be added to this block integer
     * @return new BlockInteger with the value resulting from the opertaion 
     */
    public BlockInteger add(BlockInteger val) {
        if (val.signum == 0) 
            return this;
        if (signum == 0) 
            return val;
        if (signum == val.signum) {
            File resultFolder = new File(MathEnvironment.MATH_ENV_FILE_PATH + MathEnvironment.getId());
            
            int i = numBlocks - 1;
            int j = val.numBlocks - 1;
            while (i >= 0 && j >= 0) {
                
            }
        }
        
        int cmp = compareMagnitude(val);

        return null;
    }

    private Object[] add(List<Integer> x, List<Integer> y) {
        if (x.size() < y.size()) {
            List<Integer> temp = x;
            x = y;
            y = temp;
        }

        int i = x.size();
        int j = y.size();

        return null;
    }

    private int compareMagnitude(BlockInteger val) {
        if (numDigits > val.getNumDigits()) 
            return 1;
        if (numDigits < val.getNumDigits()) 
            return -1;
        
        for (int i = numDigits - 1; i >= 0; --i) {
            int a = get(i);
            int b = val.get(i);
            if (a != b) {
                return ((a & LONG_MASK) > (b & LONG_MASK)) ? 1 : -1;
            }
        }

        return 0;
    }

    private void writeBlock(File file, List<Integer> data) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }

            Files.writeString(file.toPath(), data.toString(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("ERROR::failed to write block to file" + e.toString());
        }
    }

    private List<Integer> readBlock(File file) {
        try {
            String data = Files.readString(file.toPath());
            String[] dataParsed = data.substring(1, data.length() - 1).split(", ");
            List<Integer> result = new ArrayList<>();
            for (String it : dataParsed) {
                result.add(Integer.parseInt(it));
            }
            return result;
        } catch (IOException e) {
            System.out.println("ERROR::failed to read block" + e.toString());
            return null;
        }
    }

    public int get(int index) {
        if (index < 0 || index >= numDigits) {
            throw new IndexOutOfBoundsException(String.format("Index %d is out of bounds for size %d.", index, numDigits));
        }

        int blockIdx = index / MAX_BLOCK_SIZE;
        int subIdx = index % MAX_BLOCK_SIZE;

        if (blockIdx != activeBlockIdx) {
            activeBlock = getBlock(blockIdx);
        }

        return activeBlock.get(subIdx);
    }

    private List<Integer> getBlock(int blockIndex) {
        if (blockIndex < 0 || blockIndex >= numBlocks) {
            throw new IndexOutOfBoundsException(String.format("Block index %d is out of bounds for number of blocks %d.", blockIndex, numBlocks));
        }

        return readBlock(new File(blockPath(blockIndex)));
    }

    private String blockPath(int index) {
        return folder.getAbsolutePath() + "\\" + index + ".txt";
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
            System.out.println("ERROR::failed to delete file at\n" + file.toString());
        }
    }
}
