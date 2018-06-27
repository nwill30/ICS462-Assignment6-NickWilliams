import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Utilities {

    public Utilities() {
    }

    /**
     * Receives an existing input file and reads through and assigns each line to an array
     *
     * @param inputFile
     * @return fileList
     */
    public static ArrayList<String> readFileInput(File inputFile) {
        ArrayList<String> fileList = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile.getPath()));
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                fileList.add(readLine);
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: No file at specified file path" + inputFile.getPath());
        } catch (IOException e) {
            System.out.println("IOException: No data in selected file");
        }
        return fileList;
    }

    /**
     * Creates a new text file in a specified accessible location
     *
     * @return newFile is the file object
     * @parm fileName the name of the text file to be created (_out.txt will be appended to name)
     * @parm the location the file is to be created in
     */
    public static File createFile(String fileName, String fileDirectory) throws IOException {
        File newFile = new File(String.format("%s\\%s.txt", fileDirectory, fileName));
        newFile.createNewFile();
        return newFile;
    }

    /**
     * Writes a Linked list to an existing file
     *
     * @param fileName the name of the existing file
     *
     */
    public static void writeToFile(File fileName, LinkedList<String> outputList) {

        try {
            PrintStream writer = new PrintStream(new FileOutputStream(fileName));
            for(int i =0;i<outputList.size();i++){
                writer.println(outputList.get(i));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
