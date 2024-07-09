import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BlockInteger {
    public static final int MAX_BLOCK_SIZE = 2;

    private File folder = null;
    private int size = 0;
    private int numBlocks = 0;
    private int activeBlockIdx = -1;
    private List<Integer> activeBlock = null;

    public BlockInteger() {
        this.folder = new File(MathEnvironment.MATH_ENV_FILE_PATH + MathEnvironment.getId());
    }

    public BlockInteger add(BlockInteger val) {

        return null;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format("Index %d is out of bounds for size %d.", index, size));
        }

        int blockIdx = index / MAX_BLOCK_SIZE;
        int subIdx = index % MAX_BLOCK_SIZE;

        if (blockIdx != activeBlockIdx) {
            activeBlock = getBlock(blockIdx);
        }

        return activeBlock.get(subIdx);
    }

    private List<Integer> add(List<Integer> x, List<Integer> y) {
        if (x.size() < y.size()) {
            List<Integer> temp = x;
            x = y;
            y = temp;
        }

        int i = x.size();
        int j = y.size();

        return null;
    }
    private int cmpBlocks(List<Integer> x, List<Integer> y) {
        if (x.size() > y.size()) {
            return 1;
        } 
        if (x.size() < y.size()) {
            return -1;
        }

        
        return 0;
    }
    private List<Integer> getBlock(int blockIndex) {
        if (blockIndex < 0 || blockIndex >= numBlocks) {
            throw new IndexOutOfBoundsException(String.format("Block index %d is out of bounds for number of blocks %d.", blockIndex, numBlocks));
        }

        try {
            String data = Files.readString(Paths.get(blockPath(blockIndex)));
            String[] dataParsed = data.substring(1, data.length() - 1).split(", ");
            List<Integer> result = new ArrayList<>();
            for (String it : dataParsed) {
                result.add(Integer.parseInt(it));
            }
            return result;
        } catch (IOException e) {
            System.out.println("ERROR::failed to retrieve block #" + blockIndex + "\n" + e.toString());
            return null;
        }
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
            System.out.println("ERROR::failed to delete file\n" + file.toString());
        }
    }
}
