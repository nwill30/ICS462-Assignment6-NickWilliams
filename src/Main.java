import java.util.ArrayList;

public class Main {

    public static void main(String[] args){

        runTestDriver(511,7, 128, 10);




    }

    private static void runTestDriver(int maxSize, int minSize, int requestSize, int listSize) {

        BufferManager testBufferManager = new BufferManager();
        testBufferManager.BufferManager(listSize,maxSize,minSize);
        Object requestResponse = testBufferManager.requestBuffer(requestSize);
        System.out.print("This buffer address is " + requestResponse + " Printed "+((Buffer) requestResponse).bufferToString());
        testBufferManager.returnBuffer((Buffer) requestResponse);


    }
}
