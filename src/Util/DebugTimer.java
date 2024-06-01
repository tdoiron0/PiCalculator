package Util;

import java.util.LinkedList;
import java.util.Queue;

public class DebugTimer {
    private class Split {
        private String name;
        private long timeLength;

        public Split(String name, long timeLength) {
            this.name = name;
            this.timeLength = timeLength;
        }

        public long getMilli() {
            return timeLength / 1000000;
        }
    }

    private Queue<Split> splits = new LinkedList<>();
    private long timerStart = 0;
    private String name;

    public DebugTimer(String name) {
        this.name = name;
        this.timerStart = System.nanoTime();
    }

    public void split(String splitName) {
        splits.add(new Split(splitName, System.nanoTime() - timerStart));
        timerStart = System.nanoTime();
    }
    public String dumpToLog() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: begin\n", name));
        while (splits.peek() != null) {
            Split temp = splits.poll();
            sb.append(String.format("\t%s: %d ms, %s\n", name, temp.getMilli(), temp.name));
        }
        sb.append(String.format("%s: end", name));
        String result = sb.toString();
        System.out.println(result);
        return result;
    }
}
