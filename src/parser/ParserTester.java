package src.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import src.scanner.Scanner;
import src.environments.Environment;
import src.scanner.ScanErrorException;

/**
 * The ParserTester class tests the Parser class by feeding it an example input stream
 *      from a given file.
 * @author Daniel Gergov
 * @version 10/02/2023
 */
public class ParserTester
{
    /**
     * The main method to test the Scanner class. It opens a file stream and calls the Parser
     *      class on it by constantly calling parseStatement and printing it out until the
     *      Scanner class has no more tokens.
     * @param args command-line arguments parameter that Java requires to be specified here
     */
    public static void main(String[] args) 
    {
        try
        {
            FileInputStream inStream = new FileInputStream("test2.txt");
            Scanner lex = new Scanner(inStream);
            Parser yacc = new Parser(lex);
            Environment env = new Environment();
            yacc.parseProgram().exec(env);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("File not found.");
        }
        catch (ScanErrorException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
