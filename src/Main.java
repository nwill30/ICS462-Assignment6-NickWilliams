import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args){
        LinkedList<String> outputData = new LinkedList<>();
        outputData.add("Nick Williams, 6.22.2018, Assignment 6 \t");
        outputData.add("Initializing buffers");
        outputData = testBuffer1(outputData);
        outputData = testBuffer2(outputData);
        outputData = testBuffer3(outputData);
        outputData = testBuffer5(outputData);


        /**
         * The output data is sent to a file named BuddyBufferOutput.txt in the application dir.
         * */
        try {
            File userDirectory = new File(System.getProperty("user.dir"));
            File outputFile = Utilities.createFile("BuddyBufferOutput", userDirectory.getPath());
            Utilities.writeToFile(outputFile, outputData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The test creates a BufferManager of size 10 with a min of 7 and max of 511
     * An object Array requestResponse is created to receive multiple response locations if needed
     * The current buffer status is added to the output data
     * The request Parms and expected values are added to the output data
     * the test values are added to the in array
     * The test should generate an output of -2
     * */
    private static LinkedList<String> testBuffer1(LinkedList<String> outputData) {
        BufferManager testBuffer1 = new BufferManager();
        testBuffer1.BufferManager(10,511,7);
        ArrayList<Object> requestResponse1 = new ArrayList<>();
        outputData = (printStatus(testBuffer1.bufferStatus(), outputData));
        outputData.add("Requesting 700");
        outputData.add("Expected values:");
        int[] test1 = {700};
        return outputData = runTestDriver(test1, testBuffer1, outputData, requestResponse1);
    }
    /**
     * The test creates a BufferManager of size 10 with a min of 7 and max of 511
     * An object Array requestResponse is created to receive multiple response locations if needed
     * The current buffer status is added to the output data
     * The request Parms and expected values are added to the output data
     * the test values are added to the in array
     * The test should generate an output of bufferAddress
     * * */
    private static LinkedList<String> testBuffer2(LinkedList<String> outputData) {
        BufferManager testBuffer2 = new BufferManager();
        testBuffer2.BufferManager(10,511,7);
        ArrayList<Object> requestResponse2 = new ArrayList<>();
        outputData.add("Requesting 7");
        outputData.add("Expected values:  9 510 size buffers, 1 254 size buffer, 1 126 size buffer, 1 62 size buffer, 1 30 size buffer, 1 14 size buffer and 1 6 size buffer, Status OK");
        int[] test2 = {7};
        return outputData = runReturnTestDriver(test2, testBuffer2, outputData, requestResponse2);
    }
    /***
     * The test creates a BufferManager of size 10 with a min of 7 and max of 511
     * An object Array requestResponse is created to receive multiple response locations if needed
     * The current buffer status is added to the output data
     * The request Parms and expected values are added to the output data
     * the test values are added to the in array
     * The test should generate an output of the requested buffer addresses and status of tight
     * Test 2 in buffer 3 attempts to request and additional buffer after all buffers have been requested.
     * the test should generate an output of -1 as no buffers area available
     * * */
    private static LinkedList<String> testBuffer3(LinkedList<String> outputData) {
        BufferManager testBuffer3 = new BufferManager();
        testBuffer3.BufferManager(10,511,7);
        ArrayList<Object> requestResponse3 = new ArrayList<>();
        outputData.add("Requesting 10 510 buffers");
        outputData.add("Expected values:0 510 buffers, 0 for all buffers, Status Tight ");
        int[] test3 = {511,511,511,511,511,511,511,511,511,511};
        outputData = runTestDriver(test3, testBuffer3, outputData, requestResponse3);

        outputData.add("Requesting 62");
        outputData.add("Expected values: -1, Status Tight");
        int[] test4 = {62};
        return outputData = runTestDriver(test4,testBuffer3,outputData, requestResponse3);
    }
    /**
     * The test creates a BufferManager of size 10 with a min of 7 and max of 511
     * An object Array requestResponse is created to receive multiple response locations if needed
     * The current buffer status is added to the output data
     * The request Parms and expected values are added to the output data
     * the test values are added to the in array
     * The test should generate an output of the addressed locations
     * * */
    private static LinkedList<String> testBuffer5(LinkedList<String> outputData) {
        BufferManager testBuffer5 = new BufferManager();
        testBuffer5.BufferManager(10,511,7);
        ArrayList<Object> requestResponse5 = new ArrayList<>();
        outputData.add("Request 19 254 size buffers");
        outputData.add("Expected values: 0 510 buffers, 19 254 size buffers, 0 126 size buffers,  0 62 size buffers, 0 30 size buffers, 0 14 size buffers,  0 6 size buffers Status Tight ");
        int[] test5 = {253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253};
        return outputData = runTestDriver(test5,testBuffer5,outputData, requestResponse5);
    }
    /**
     * Test driver is the method used to support the majority of the tests
     * Each buffers current status is added to the output data for a pre-test record
     * Each value in the requestSize parm is requested by the supplied BufferManager and the response is added to the output data as the assigned address
     * The buffer status is added the output data as a post test check along with the buffer status
     * all of the output data is returned to the calling method
     * */
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
    /**
     * Test return driver is the method used to support the test that check values after a return is made
     * Each buffers current status is added to the output data for a pre-test record
     * Each value in the requestSize parm is requested by the supplied BufferManager and the response is added to the output data as the assigned address
     * The buffer status is added the output data as a post test check along with the buffer status
     * all of the output data is returned to the calling method
     * After the value has been requested and checked it is then returned to the BufferManager
     * The Status after the buffer is reclaimed is returned to the calling method
     * */
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
    /**
     * Prints the status of the buffers at the correlating locations
     * */
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
