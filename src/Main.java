import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        System.out.println("Nick Williams, 6.22.2018, Assignment 6 \t");
        System.out.println("Initializing buffers");
        BufferManager bufferManager = new BufferManager();
        bufferManager.BufferManager(10,511,7);
        System.out.println(bufferManager.bufferDebug());
        int[] test1 = {700};
        runTestDriver(test1, bufferManager);
        int[] test2 = {7};
        runTestDriver(test2, bufferManager);
        int[] test3 = {511,511,511,511,511,511,511,511,511,511};
        runTestDriver(test3, bufferManager);
        int[] test4 = {62};



    }

    private static Buffer runTestDriver(int[] requestSize, BufferManager bufferManager) {
        printStatus(bufferManager.bufferStatus());
        Object requestResponse = null;
        for(int i = 0;i < requestSize.length;i++){
            requestResponse = bufferManager.requestBuffer(requestSize[i]);
        }
        return (Buffer) requestResponse;

    }

    private static void printStatus(int[] bufferCounts){
        System.out.println(bufferCounts[0] + " 510 size buffers");
        System.out.println(bufferCounts[1] + " 254 size buffers");
        System.out.println(bufferCounts[2] + " 126 size buffers");
        System.out.println(bufferCounts[3] + " 62 size buffers");
        System.out.println(bufferCounts[4] + " 30 size buffers");
        System.out.println(bufferCounts[5] + " 14 size buffers");
        System.out.println(bufferCounts[6] + " 6 size buffers");
    }
}
