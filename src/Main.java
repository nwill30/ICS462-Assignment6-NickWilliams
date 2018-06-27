import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args){
        LinkedList<String> outputData = new LinkedList<>();
        outputData.add("Nick Williams, 6.22.2018, Assignment 6 \t");
        outputData.add("Initializing buffers");
        BufferManager testBuffer1 = new BufferManager();
        testBuffer1.BufferManager(10,511,7);
        ArrayList<Object> requestResponse1 = new ArrayList<>();
        outputData = (printStatus(testBuffer1.bufferStatus(), outputData));
        outputData.add("Requesting 700");
        outputData.add("Expected values:");
        int[] test1 = {700};
        outputData = runTestDriver(test1, testBuffer1, outputData, requestResponse1);
        /**
         *
         * */
        BufferManager testBuffer2 = new BufferManager();
        testBuffer2.BufferManager(10,511,7);
        ArrayList<Object> requestResponse2 = new ArrayList<>();
        outputData.add("Requesting 7");
        outputData.add("Expected values:  9 510 size buffers, 1 254 size buffer, 1 126 size buffer, 1 62 size buffer, 1 30 size buffer, 1 14 size buffer and 1 6 size buffer, Status OK");
        int[] test2 = {7};
        outputData = runReturnTestDriver(test2, testBuffer2, outputData, requestResponse2);
        /***
         *
         * */
        BufferManager testBuffer3 = new BufferManager();
        testBuffer3.BufferManager(10,511,7);
        ArrayList<Object> requestResponse3 = new ArrayList<>();
        outputData.add("Requesting 10 510 buffers");
        outputData.add("Expected values:0 510 buffers, 0 for all buffers, Status Tight ");
        int[] test3 = {511,511,511,511,511,511,511,511,511,511};
        outputData = runTestDriver(test3, testBuffer3, outputData, requestResponse3);
        /**
         *
         * */
        BufferManager testBuffer4 = new BufferManager();
        testBuffer4.BufferManager(10,511,7);
        ArrayList<Object> requestResponse4 = new ArrayList<>();
        outputData.add("Requesting 62");
        outputData.add("Expected values: -1, Status Tight");
        int[] test4 = {62};
        outputData = runTestDriver(test4,testBuffer4,outputData, requestResponse4);
        /**
         *
         * */
        BufferManager testBuffer5 = new BufferManager();
        testBuffer5.BufferManager(10,511,7);
        ArrayList<Object> requestResponse5 = new ArrayList<>();
        outputData.add("Request 19 254 size buffers");
        outputData.add("Expected values: 0 510 buffers, 19 254 size buffers, 0 126 size buffers,  0 62 size buffers, 0 30 size buffers, 0 14 size buffers,  0 6 size buffers Status Tight ");
        int[] test5 = {253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253};
        outputData = runTestDriver(test5,testBuffer5,outputData, requestResponse5);

        /**
         * The output data is sent to a file named diskScheduleOutput_out.txt in the application dir.
         * */
        try {
            File userDirectory = new File(System.getProperty("user.dir"));
            File outputFile = Utilities.createFile("BuddyBufferOutput", userDirectory.getPath());
            Utilities.writeToFile(outputFile, outputData);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static LinkedList<String> runTestDriver(int[] requestSize, BufferManager bufferManager, LinkedList outputData, ArrayList<Object> requestResponse) {
        outputData = printStatus(bufferManager.bufferStatus(), outputData);
        for(int i = 0;i < requestSize.length;i++){
            requestResponse.add(bufferManager.requestBuffer(requestSize[i]));
            outputData.add("Actual = Assigned address: " + requestResponse.get(i).toString());
        }
        outputData = printStatus(bufferManager.bufferStatus(), outputData);
        outputData.add("Status: "+bufferManager.getStatus());
        return outputData;
    }

    private static LinkedList<String> runReturnTestDriver(int[] requestSize, BufferManager bufferManager, LinkedList outputData, ArrayList<Object> requestResponse) {
        outputData = printStatus(bufferManager.bufferStatus(), outputData);
        for(int i = 0;i < requestSize.length;i++){
            requestResponse.add(bufferManager.requestBuffer(requestSize[i]));
            outputData.add("Actual = Assigned address: " + requestResponse.get(i).toString());
        }
        outputData = printStatus(bufferManager.bufferStatus(), outputData);
        outputData.add("Return buffer size: " + requestSize[0]);
        outputData.add("Expected values: 10 510 size buffers, Status OK");
        for(int i = 0;i<requestResponse.size();i++){
            bufferManager.returnBuffer((Buffer) requestResponse.get(i));
        }
        outputData = printStatus(bufferManager.bufferStatus(), outputData);
        outputData.add("Status: "+bufferManager.getStatus());
        return outputData;
    }

    private static LinkedList printStatus(int[] bufferCounts, LinkedList outputData){
        outputData.add("Free Buffer Count:");
        outputData.add(bufferCounts[0] + " 510 size buffers");
        outputData.add(bufferCounts[1] + " 254 size buffers");
        outputData.add(bufferCounts[2] + " 126 size buffers");
        outputData.add(bufferCounts[3] + " 62 size buffers");
        outputData.add(bufferCounts[4] + " 30 size buffers");
        outputData.add(bufferCounts[5] + " 14 size buffers");
        outputData.add(bufferCounts[6] + " 6 size buffers");
        return outputData;
    }
}
