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
            }else {
                this.tightPool = checkTightConstraint();
                return requestResponse;
            }

        }

        return placeholder;
    }

    private boolean checkTightConstraint() {
        int free = 0;
        for(int i = 0;i < bufferList.size();i++){
            if(bufferList.get(i).isBufferFree()){
                free = free+1;
            }
        }

        return free < 2;
    }

    public void returnBuffer(Buffer addess){

        for(int i = 0;i < bufferList.size();i++){
            if(bufferList.get(i).reclaimBuffer(addess)==addess){

                System.out.print(String.format("Buffer %s reclaimed from parent %s, %s in the buffer list. ",addess,bufferList.get(i),i));
                System.out.println("The number of remaining buffers is "+bufferList.size());
                break;
            }
        }

    }

    public String getStatus(){

        return "null";
    }


    public Buffer getBuffer(Buffer requestResponse) {
        Buffer returnBuffer = bufferList.get(bufferList.indexOf(requestResponse));
        return returnBuffer;
    }
}
