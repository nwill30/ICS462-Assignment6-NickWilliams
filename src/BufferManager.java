import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class BufferManager {

    private LinkedList<Buffer> bufferList = null;
    private int maxBufferSize = 0;
    private int minBufferSize = 0;
    private boolean tightPool = false;
    private String status = null;

    /**
     * Constructor receives the size of the buffer list, the max size of each buffer and the min isze of each buffers
     * The values are set to the BufferManager attributes and the buildBufferList method is used to create the bufferLIst
     * The status is set by the buffer debug method
     * */
    public void BufferManager(int listSize, int bufferMax, int bufferMin){

        this.maxBufferSize = bufferMax;
        this.minBufferSize = bufferMin;
        this.bufferList = buildBufferList(listSize, this.maxBufferSize, this.minBufferSize);
        setStatus(bufferDebug());
    }

    /**
     * The size of the list min/max buffer sizes are provided and a linked list (the buffer list) is returned
     * Each buffer is initialized and added to the buffer linkedList
     * */
    private LinkedList<Buffer> buildBufferList(int listSize,int maxBufferSize, int minBufferSize) {
        LinkedList<Buffer> bufferList = new LinkedList<>();
        for(int i = 0;i<listSize;i++){
            Buffer newBuffer = new Buffer();
            newBuffer.Buffer(maxBufferSize,minBufferSize,null);
            bufferList.add(newBuffer);
        }
        return bufferList;
    }

    /**
     * The status is set to the value provided
     * */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * bufferDebug returns the current status of the buffers in the BufferManager
     * The buffer sizes are then processed but the getBufferList status and returned as a count of each buffer size
     * (ex. x y size buffers)
     * The buffer size and count are then returned to the calling method with current status
     * */
    public String bufferDebug(){
        String bufferStatus = null;
        ArrayList<String> bufferStatusList = buildAvailableBufferList();
        String bufferSizeCounts = getBufferListStatus(bufferStatusList);
        return String.format("Expected Values: %s Status: %s",bufferSizeCounts, getStatus());
    }

    /**
     * BuildAvailableBuffer list iterates through each buffer in the BufferManager List
     * If the buffer is free it's status is added to the list
     * If not the status of it's children is added to the list
     * The list is returned to the calling method
     * */
    private ArrayList<String> buildAvailableBufferList() {
        ArrayList returnBufferList = new ArrayList();
        for(int i = 0;i < this.bufferList.size();i++){
            if(this.bufferList.get(i).isBufferFree()){
                returnBufferList.add(this.bufferList.get(i).getStatus());
            }else{
                returnBufferList.addAll(this.bufferList.get(i).getChildrenStatus());
            }
        }
        return returnBufferList;
    }

    /**
     * The method receives a list of buffer statuses
     * If the list is not null it processes each buffer status in the list
     * Each status is compared against all subesquent statuses in list to find a match
     * Once a match is found that status is removed from the list
     * once the full list has been processed the status and number of occurences is added to the buffer count lis
     * (ex. 2 15 size buffers where 2 is the number off occurances of "15 size buffers")
     * The collection is reversed so the largest buffers are first in the list
     * All values are concatenated with a , and returned to the calling method
     * */
    private String getBufferListStatus(ArrayList<String> bufferStatusList) {
        ArrayList<String> bufferCountList = new ArrayList();
        if(bufferStatusList !=null){
            while(bufferStatusList.size()>0){
                String checkValue = bufferStatusList.get(0);
                int checkCount = 1;
                for(int i = 1;i < bufferStatusList.size();i++){
                    if(bufferStatusList.get(i).equals(checkValue)){
                        checkCount = checkCount+1;
                        bufferStatusList.remove(i);
                        i = 0;
                    }
                }
                bufferCountList.add(String.format("%d %s",checkCount,checkValue));
                bufferStatusList.remove(0);
            }
        }
        Collections.reverse(bufferCountList);
        StringBuilder debugStatus = new StringBuilder();
        for(String i : bufferCountList){
            debugStatus.append(i);
            debugStatus.append(", ");
        }
        return debugStatus.toString();
    }

    /**
     * RequestBuffer receives a requestedSize and returned an object of:
     *          "-2" for an invalid request
     *          "-1" for lack of space
     *          "memory loc" of the buffer assigned to the request
     * Buffers in the bufferManager list are processed individually
     * They are checked to see if they are free, if not the next buffer is processed
     *  if they are free the buffer is requested with the requestSize provided and the above responsed are returned from the buffer
     *  if a buffer is assigned the tight constraint is checked for the BufferManager and the buffer loc is returned
     * */
    public Object requestBuffer(int requestSize){
        Object returnValue = "-1";
        Object requestResponse = null;
        for(int i = 0;i<bufferList.size();i++){
            if(bufferList.get(i).isBufferFree()){
                requestResponse = bufferList.get(i).requestBuffer(requestSize);
                if(requestResponse == "-2"){
                    returnValue = "-2";
                    break;
                }else if (requestResponse == "-1"){
                    returnValue =  "-1";
                }else {
                    this.tightPool = checkTightConstraint();
                    returnValue =  requestResponse;
                    break;
                }
            }else if(bufferList.get(i).isBufferSplit()){
                requestResponse = bufferList.get(i).requestChild(requestSize);
                if(requestResponse == "-2"){
                    returnValue =  "-2";
                    break;
                }else if (requestResponse == "-1"){
                    returnValue =  "-1";
                }else {
                    this.tightPool = checkTightConstraint();
                    returnValue =  requestResponse;
                    break;
                }
            }
        }
        setStatus(bufferDebug());
        return returnValue ;
    }

    /**
     * Each buffer in the buffer list is check to see if it is free
     * if there are less that 2 available buffers the constraint is set to true (for constraint is tight)
     * */
    private boolean checkTightConstraint() {
        int free = 0;
        for(int i = 0;i < bufferList.size();i++){
            if(bufferList.get(i).isBufferFree()){
                free = free+1;
            }
        }

        return free < 2;
    }

    /**
     * The address of the buffer to reclaim is provided
     * each buffer in the BufferManager list is checked to determine if it contains the reclaim buffer
     * If it is located the buffer is reclaimed and the tightPool constraint is run to update it's status
     * */
    public void returnBuffer(Buffer addess){
        for(int i = 0;i < bufferList.size();i++){
            if(bufferList.get(i).reclaimBuffer(addess)==addess){
                this.tightPool = checkTightConstraint();
                break;
            }
        }
    }

    /**
     * A list of available buffers is retrieved using the buildAvailableBufferList method
     * Each value in that list is checked, if the value matches one of the pre-determined buffer sizes
     * it is recorded in the int array at it's corresponding location
     * */
    public int[] bufferStatus(){
        ArrayList<String> bufferCountList = buildAvailableBufferList();
        int[] bufferStatusList = {0,0,0,0,0,0,0};
        for(int i = 0;i<bufferCountList.size();i++){
            if(bufferCountList.get(i).equals("511 size buffer")){
                bufferStatusList[0] = bufferStatusList[0] +1;
            }else if(bufferCountList.get(i).equals("255 size buffer")){
                bufferStatusList[1] = bufferStatusList[1] +1;
            }else if(bufferCountList.get(i).equals("127 size buffer")){
                bufferStatusList[2] = bufferStatusList[2] +1;
            }else if(bufferCountList.get(i).equals("63 size buffer")){
                bufferStatusList[3] = bufferStatusList[3] +1;
            }else if(bufferCountList.get(i).equals("31 size buffer")){
                bufferStatusList[4] = bufferStatusList[4] +1;
            }else if(bufferCountList.get(i).equals("15 size buffer")){
                bufferStatusList[5] = bufferStatusList[5] +1;
            }else if(bufferCountList.get(i).equals("7 size buffer")){
                bufferStatusList[6] = bufferStatusList[6] +1;
            }
        }

        return bufferStatusList;
    }

    /**
     * The tightPool is checked, if tight "Tight" is returned else "Ok"
     * */
    public String getStatus(){
        if(isTightPool()){
            return "tight";
        }else{
            return "OK";
        }
    }

    /**
     * Below are getter and setter methods for the BufferManager class
     * */
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
