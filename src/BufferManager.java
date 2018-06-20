import java.util.LinkedList;

public class BufferManager {

    private LinkedList<Buffer> bufferList = null;
    private int maxBufferSize = 0;
    private int minBufferSize = 0;
    private boolean tightPool = false;

    public void BufferManager(int listSize, int bufferMax, int bufferMin){

        this.maxBufferSize = bufferMax;
        this.minBufferSize = bufferMin;
        this.bufferList = buildBufferList(listSize, this.maxBufferSize, this.minBufferSize);
    }


    private LinkedList<Buffer> buildBufferList(int listSize,int maxBufferSize, int minBufferSize) {
        LinkedList<Buffer> bufferList = new LinkedList<>();
        for(int i = 0;i<listSize;i++){
            Buffer newBuffer = new Buffer();
            newBuffer.Buffer(maxBufferSize,minBufferSize);
            bufferList.add(newBuffer);
        }
        return bufferList;
    }

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

    public Object requestBuffer(int requestSize){
        String placeholder = "0";

        for(int i = 0;i<bufferList.size();i++){
            Object requestResponse = bufferList.get(i).requestBuffer(requestSize);
            if(requestResponse == "-2"){
                return "-2";
            }else if (requestResponse == "-1"){
                return "-1";
            }else {return requestResponse;}

        }

        return placeholder;
    }

    public void returnBuffer(int addess){

    }

    public String getStatus(){

        return "null";
    }


    public Buffer getBuffer(Buffer requestResponse) {
        Buffer returnBuffer = bufferList.get(bufferList.indexOf(requestResponse));
        return returnBuffer;
    }
}
