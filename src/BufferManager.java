import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class BufferManager {

    private LinkedList<Buffer> bufferList = null;
    private int maxBufferSize = 0;
    private int minBufferSize = 0;
    private boolean tightPool = false;
    private String status = null;

    public void BufferManager(int listSize, int bufferMax, int bufferMin){

        this.maxBufferSize = bufferMax;
        this.minBufferSize = bufferMin;
        this.bufferList = buildBufferList(listSize, this.maxBufferSize, this.minBufferSize);
        setStatus(bufferDebug());
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
        String placeholder = "-1";
        Object requestResponse = null;
        for(int i = 0;i<bufferList.size();i++){
            if(bufferList.get(i).isBufferFree()){
                requestResponse = bufferList.get(i).requestBuffer(requestSize);
                if(requestResponse == "-2"){
                    return "-2";
                }else if (requestResponse == "-1"){
                    return "-1";
                }else {
                    this.tightPool = checkTightConstraint();
                    return requestResponse;
                }
            }
        }
        setStatus(bufferDebug());
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
                this.tightPool = checkTightConstraint();
                break;
            }
        }

    }

    public String getStatus(){
        if(isTightPool()){
            return "tight";
        }else{
            return "OK";
        }
    }

    public String bufferDebug(){
        String bufferStatus = null;
        ArrayList<String> bufferStatusCount = buildAvailableBufferList();
        String test = getBufferListStatus(bufferStatusCount);
        return String.format("Expected Values: %s Status: %s",test, getStatus());
    }

    private String getBufferListStatus(ArrayList<String> bufferStatusCount) {
        ArrayList<String> bufferCountList = new ArrayList();
        if(bufferStatusCount !=null){
            while(bufferStatusCount.size()>0){
                String checkValue = bufferStatusCount.get(0);
                int checkCount = 1;
                for(int i = 1;i < bufferStatusCount.size();i++){
                    if(bufferStatusCount.get(i).equals(checkValue)){
                        checkCount = checkCount+1;
                        bufferStatusCount.remove(i);
                        i = 0;
                    }
                }
                bufferCountList.add(String.format("%d %s",checkCount,checkValue));
                bufferStatusCount.remove(0);
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

    private ArrayList<String> buildAvailableBufferList() {
        ArrayList bufferList = new ArrayList();
        for(int i = 0;i < this.bufferList.size();i++){
            if(this.bufferList.get(i).isBufferFree()){
                bufferList.add(this.bufferList.get(i).getStatus());
            }else{
                bufferList.addAll(this.bufferList.get(i).getChildrenStatus());
            }
        }
        return bufferList;
    }

    public Buffer getBuffer(Buffer requestResponse) {
        Buffer returnBuffer = bufferList.get(bufferList.indexOf(requestResponse));
        return returnBuffer;
    }

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

    public void setStatus(String status) {
        this.status = status;
    }


}
