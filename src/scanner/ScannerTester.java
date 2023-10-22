package src.scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The ScannerTester class tests the Scanner class by feeding it an example input stream
 *      from a given file.
 * @author Daniel Gergov
 * @version 8/30/2023
 */
public class ScannerTester {
    /**
     * The main method to test the Scanner class. It opens a file stream and calls the Scanner
     *      class on it by constantly calling nextToken and printing it out.
     * @param args command-line arguments parameter that Java requires to be specified here
     */
    public static void main(String[] args) 
    {
        try {
            FileInputStream inStream = new FileInputStream("test.txt");
            Scanner lex = new Scanner(inStream);
            while (lex.hasNext()) 
            {
                System.out.println(lex.nextToken());
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found.");
        }
        catch (ScanErrorException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
