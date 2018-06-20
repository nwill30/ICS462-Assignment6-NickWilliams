public class Buffer {

    private int maxSize;
    private int minSize;
    private int currentSize;
    private Buffer parent = null;
    private Buffer childA = null;
    private Buffer childB = null;
    private boolean bufferFree = true;
    private boolean bufferSplit = false;
    private String controlWord = null;
    private Object address;
    private Buffer thisBuffer = this;

    public void Buffer(int maxSize, int minSize){
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.currentSize = maxSize;
    }

    public void Buffer(int maxSize, int minSize, int requestSize){
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.currentSize = maxSize;
    }

    public Object Buffer(int maxSize, int minSize, int requestSize, Buffer parent){
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.currentSize = maxSize;
        return this.address = checkRequest(requestSize);


    }

    public void Buffer(int maxSize, int minSize, Buffer parent){
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.currentSize = maxSize;

    }

    public Object requestBuffer(int requestSize){
        return this.address = checkRequest(requestSize);
    }

    private Object checkRequest(int requestSize) {
        if(requestSize>this.maxSize){
            return "-2";
        }else if(requestSize > currentSize){
            return "-1";
        }else {
            if (currentSize <= 8) {
                setControlWord(requestSize);
                return this.thisBuffer;
            } else if (requestSize > (currentSize / 2)) {
                setControlWord(requestSize);
                return this.thisBuffer;
            } else {
               return splitBuffer(requestSize);
            }
        }
    }

    private Object splitBuffer(int requestSize) {

        Buffer childA = new Buffer();
        childA.Buffer(this.maxSize/2,7,this.thisBuffer);
        this.childA = childA;
        Buffer childB = new Buffer();
        childB.Buffer(this.maxSize/2,7,this.thisBuffer);
        this.childB = childB;
        this.bufferFree = false;
        this.bufferSplit = true;

        return childA.checkRequest(requestSize);

    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getMinSize() {
        return minSize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public Buffer getParent() {
        return parent;
    }

    public Buffer getChildA() {
        return childA;
    }

    public Buffer getChildB() {
        return childB;
    }

    public boolean isBufferFree() {
        return bufferFree;
    }

    public boolean isBufferSplit() {
        return bufferSplit;
    }

    public String getControlWord() {
        return controlWord;
    }

    public String bufferToString(){
        return  this.thisBuffer.toString();
    }

    private void setControlWord(int request){
        this.controlWord = request+"";
    }


}
