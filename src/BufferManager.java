import java.util.LinkedList;

public class BufferManager {

    private LinkedList<Buffer> bufferList = null;
    private int maxBufferSize = 0;
    private boolean tightPool = false;

    public LinkedList<Buffer> getBufferList() {
        return bufferList;
    }

    public void setBufferList(LinkedList<Buffer> bufferList) {
        this.bufferList = bufferList;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }

    public void setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    public boolean isTightPool() {
        return tightPool;
    }

    public void setTightPool(boolean tightPool) {
        this.tightPool = tightPool;
    }
}
