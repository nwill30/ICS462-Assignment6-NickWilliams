import java.lang.reflect.Array;
import java.util.ArrayList;

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
    private Object requestResponse;
    private Buffer thisBuffer = this;

    /**
     * Constructor method to create buffer
     * all attributes are set based on provided parms
     * current size is set to max size
     * The parent of the buffer is provided if it is a split from a previous buffer else the parent is null
     * */
    public void Buffer(int maxSize, int minSize, Buffer parent) {
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.currentSize = maxSize;
        this.parent = parent;
    }

    /**
     * A buffer size request is provided to the buffer
     * The checkRequest method is used to validate the request
     * The bufferaddress will be returned if the request is satisfied
     * */
    public Object requestBuffer(int requestSize) {
        return this.requestResponse = checkRequest(requestSize);
    }

    /**
     * if a buffer is not free but has children the requestChild method will determine if they may fill the request
     * the request is passed to the checkChildRequest method
     * */
    public Object requestChild(int requestSize) {
        return this.requestResponse = checkChildRequest(requestSize);
    }

    /**
     * The checkRequest method receives a requested buffer size
     * if the request is too large a "-2" is returned to the calling method
     * if the buffer is not large enough to fulfill the request a "-1" is returned
     * otherwise the if the current buffer is less than the minSize the request is fulfilled and the current address is returned
     * if the request is larger than the next split size the request is fulfilled and the current address is returned
     * otherwise the buffer is split in 2 and the request is further processed
     * */
    private Object checkRequest(int requestSize) {
        if (requestSize > this.maxSize) {
            return "-2";
        } else if (requestSize > currentSize) {
            return "-1";
        } else {
            if (currentSize <= minSize+1) {
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

    /**
     * if the request exceeds the max alloted buffer size "-2" is returned
     * if the request exceeds the current buffer size "-1" is returned
     * else the buffer that is free processes the check request as normal
     * */
    private Object checkChildRequest(int requestSize) {
        if (requestSize > this.maxSize) {
            return "-2";
        } else if (requestSize > currentSize) {
            return "-1";
        } else {
            Buffer child = freeChild();
            if(child == null){
                return "-1";
            }else{
                if(child.isBufferFree() && child.currentSize <= minSize+1){
                    child.setControlWord(requestSize);
                    return child.thisBuffer;
                }else if(child.isBufferFree() && requestSize > (child.currentSize / 2)) {
                    child.setControlWord(requestSize);
                    return child.thisBuffer;
                }else {
                    return child.checkRequest(requestSize);
                }
            }
        }
    }

    /**
     * returns the name of the any free child else null
     * */
    private Buffer freeChild() {
        if(this.childA.isBufferFree()){
            return childA;
        }else if(this.childB.isBufferFree()){
            return childB;
        }
        return null;
    }

    /**
     * The splitBuffer creates two additional buffers 1/2 the size of the current buffer
     * the new buffers are assigned to childA and childB
     * the current Buffer is set to split and no longer free
     * */
    private Object splitBuffer(int requestSize) {
        Buffer childA = new Buffer();
        childA.Buffer(this.maxSize / 2, 7, this.thisBuffer);
        this.childA = childA;
        Buffer childB = new Buffer();
        childB.Buffer(this.maxSize / 2, 7, this.thisBuffer);
        this.childB = childB;
        this.bufferFree = false;
        this.bufferSplit = true;


        return childA.checkRequest(requestSize);

    }

    /**
     * A buffer address is provided to the recalimeBuffer method
     * if the address matches the current buffer the control word is emptied, the buffers is set to free and no longer split
     * The address is then returned to the calling method (or buffer)
     * */
    public Buffer reclaimBuffer(Buffer returnedAddress) {
        if (returnedAddress == this.thisBuffer) {
            this.controlWord = null;
            this.bufferFree = true;
            this.bufferSplit = false;
            return this.thisBuffer;
        } else if (this.childA == null && this.childB == null) {
            return new Buffer();
        } else if (this.bufferSplit = true) {
            if (childA.reclaimBuffer(returnedAddress) == returnedAddress || childB.reclaimBuffer(returnedAddress) == returnedAddress) {
                this.childA = null;
                this.childB = null;
                this.bufferFree = true;
                this.bufferSplit = false;
                return returnedAddress;
            } else {
                return new Buffer();
            }
        }
        return new Buffer();
    }

    /**
     * The control word contains the buffer size and a pointer to the next buffer in the pool
     * */
    private void setControlWord(int request) {
        if(this.parent !=null){
            this.controlWord = this.currentSize + " " + getSibling();
        }else {
            this.controlWord = this.thisBuffer.toString()+"";
        }
        this.bufferFree = false;
    }

    /**
     * The sibling is initially set to null
     * An array is created for the return values of the getChildren method requested from the current Buffers parent
     * Which ever buffer in the string does not match the current Buffer is returned as the current Buffer's sibling
     * */
    private String getSibling() {
        String sibling = "null";
        ArrayList<Buffer> siblings = this.parent.getChildren();
        if (siblings.get(0) == this) {
            return siblings.get(1).toString();
        } else {
            return siblings.get(0).toString();
        }
    }

    /**
     * Both of the current buffer's children are set to a Buffer array and returned to the calling method
     * */
    private ArrayList<Buffer> getChildren() {
        ArrayList<Buffer> children = new ArrayList<>();
        children.add(this.childA);
        children.add(this.childB);
        return children;
    }

    /**
     * Each child in the Buffer is returned to the calling method in the childrenStatus Array
     * If the requested child is split then it along with it's children are added to the childrenStatus Array
     * */
    public ArrayList getChildrenStatus() {
        ArrayList childrenStatus = new ArrayList();
        if(this.childA!= null && !this.childA.isBufferSplit()){
            childrenStatus.add(this.childA.getStatus());
        }else if(this.childA != null){
            childrenStatus.addAll(this.childA.getChildrenStatus());
        }
        if(this.childB != null && !this.childB.isBufferSplit()){
            childrenStatus.add(this.childB.getStatus());
        }else if(this.childB != null){
            childrenStatus.addAll(this.childB.getChildrenStatus());
        }
        return childrenStatus;
    }

    /**
     * return the buffer and it's current size
     * */
    public String getStatus() {
        return String.format("%s size buffer", this.currentSize);
    }

    /**
     * Below are Buffer getter and setter methods
     * */
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

    public String bufferToString() {
        return this.thisBuffer.toString();
    }


}