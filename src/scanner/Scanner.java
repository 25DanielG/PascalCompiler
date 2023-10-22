package src.scanner;

import java.io.*;

/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab exercise 1
 * @author Daniel Gergov
 * @version 8/30/2023
 * The Scanner class is a class that allows a user to scan an input file and return tokens
 *      of identifiers, numbers, and operators. It also allows the user to check for the
 *      end of file.
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
    public int lineno, colno;

    public static enum TOKEN_TYPE { OPERATOR, IDENTIFIER, NUMBER, EOF, UMKNOWN };

    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        lineno = 1;
        colno = 1;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * The getNextChar method reads the next character from the input stream
     *      and sets the end-of-file flag eof if the end of file is reached and
     *      the current character to the next character in the input stream
     * @precondition the input file stream is open
     * @postcondition updates the currentChar instance field with the next character
     *      and the eof instance field if the file ended
     */
    private void getNextChar()
    {
        try
        {
            int inp = in.read();
            if (inp == -1 || inp == '.')
            {
                eof = true;
            }
            else
            {
                currentChar = (char) inp;
            }
            if (inp == '\n')
            {
                ++lineno;
                colno = 1;
            }
            else
            {
                ++colno;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * The eat method checks if the current character is the expected character and calls
     *      the getNextChar method if it is. If the current character is not the expected
     *      character, it throws a ScanErrorException with a helpful debug message.
     * @param expected the expected character to eat
     * @precondition currentChar is equal to expected
     * @postcondition calls getNextChar to update the currentChar instance field
     * @throws ScanErrorException if the current character is not the expected character
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (currentChar == expected)
        {
            getNextChar();
        }
        else
        {
            throw new ScanErrorException("Illegal character, expected <" + expected + ">, found <" + currentChar + ">.");
        }
    }

    /**
     * Returns a boolean of whether the scanner has another token to read and the end
     *      of the file has not been reached
     * @postcondition returns the opposite of the eof instance field
     * @return type boolean whether the end of file has been reached
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * The isWhiteSpace method returns true if the input parameter is a whitespace character and
     *      false if it is not.
     * @param str the character to be checked if it is whitespace or not
     * @precondition the given str parameter is a valid character
     * @postcondtion returns whether the str parameter is a whitespace
     * @return a boolean whether the input parameter is whitespace or not
     */
    private boolean isWhiteSpace(char str)
    {
        String s = "" + str;
        return " \n\t\r".contains(s);
    }

    /**
     * The isLetter method returns true if the input parameter is a letter in the alphabet
     * @param str the string to be checked
     * @precondition the input parameter is a single character
     * @postcondition returns whether the parameter is a letter
     * @return a boolean whether the input paramter is a letter in the alphabet
     */
    private boolean isLetter(char str)
    {
        String s = "" + str;
        return "abcdefghijklmnopqrstuvwxyz".contains(s.toLowerCase());
    }

    /**
     * The isDigit method returns true if the input parameter is a digit
     * @param str the string to be checked
     * @precondition the input parameter is a single character
     * @postcondition returns whether the parameter is a digit
     * @return a boolean whether the input paramter is a digit
     */
    private boolean isDigit(char str)
    {
        String s = "" + str;
        return "1234567890".contains(s);
    }

    /**
     * The isOperator method returns true if the input parameter is an operator
     * @param str the string to be checked
     * @precondition the input parameter is a single character
     * @postcondition returns whether the parameter is an operator
     * @return a boolean whether the input paramter is an operator
     */
    public boolean isOperator(char str)
    {
        String s = "" + str;
        return ":<>=;,+-*/%()".contains(s);
    }

    /**
     * The scanNumber method scans the input stream for a number and returns the number
     *      as a string, and additionally, it scans numbers until it sees a non-digit. The
     *      method then returns that digit as a string.
     * @return the number as a string
     * @precondition the currentChar is a digit
     * @postcondition returns the complete number in the file stream
     * @throws ScanErrorException if the current character is not a digit
     */
    private Token scanNumber() throws ScanErrorException
    {
        char current = currentChar;
        String digit = "";
        while (isDigit(current)) {
            digit += current;
            eat(current);
            current = currentChar;
        }
        return new Token(TOKEN_TYPE.NUMBER, digit);
    }

    /**
     * The scanIdentifier method scans the input stream for an identifier and returns the
     *      identifier as a string, and additionally, it scans numbers until it sees a non-digit
     *      or non-letter. The method then returns that identifier as a string.
     * @return the identifier as a string
     * @precondition the currentChar is a letter
     * @postcondition returns the complete identifier in the file stream
     * @throws ScanErrorException if the current character is not a letter
     */
    private Token scanIdentifier() throws ScanErrorException
    {
        char current = currentChar;
        if (!isLetter(current))
        {
            throw new ScanErrorException("Illegal character, expected <letter>, found <" + current + ">.");
        }
        String identifier = "";
        while (isLetter(current) || isDigit(current))
        {
            identifier += current;
            eat(current);
            current = currentChar;
        }
        if (identifier.equals("mod"))
            return new Token(TOKEN_TYPE.OPERATOR, "mod");
        return new Token(TOKEN_TYPE.IDENTIFIER, identifier);
    }

    /**
     * The scanOperator method scans the input stream for an operator and returns the operator
     *      as a string, and additionally, it scans operators until it sees a non-operator. The
     *      method then returns that operator as a string. Additionally, when the method sees
     *      a "/" it calls the eatComment method to get rid of the comments.
     * @return the operator as a string
     * @precondition the currentChar is an operator
     * @postcondition returns the complete operator in the file stream and ignores comments
     * @throws ScanErrorException if the current character is not an operator
     */
    private Token scanOperator() throws ScanErrorException {
        if (!isOperator(currentChar))
        {
            throw new ScanErrorException("Unrecognized character: " + currentChar);
        }
    
        String token = "";
    
        if (currentChar == '/')
        {
            if (!eatComment())
            {
                token += "/";
            }
            else
            {
                return nextToken();
            }
        }
        else
        {
            token += currentChar;
        }
    
        eat(currentChar);
    
        if (isExtendableOperator(token.charAt(0)) && hasNext())
        {
            if (currentChar == '=' || (token.charAt(0) == '<' && currentChar == '>'))
            {
                token += currentChar;
                eat(currentChar);
            }
        }
    
        return new Token(TOKEN_TYPE.OPERATOR, token);
    }    
    
    /**
     * The eatComment method checks if the current character is a comment. If the first two
     *      characters are both "/" then it is a single line comment and the method eats until
     *      it encounters a "\n". If the first two characters are a "/*" the method calls the
     *      eatBlockComment method to eat the block comment.
     * @return a boolean whether the current character is a comment or not
     * @precondition none
     * @postcondition eats the current comment either single-line or block comment
     * @throws ScanErrorException if the parameter passed to the eat method doesn't match the
     *      currentChar instance field
     */
    private boolean eatComment() throws ScanErrorException
    {
        if (hasNext() && currentChar == '/')
        {
            eat(currentChar);
            if (currentChar == '/')
            {
                while (currentChar != '\n' && hasNext())
                {
                    eat(currentChar);
                }
                return true;
            }
            else if (currentChar == '*')
            {
                eatBlockComment();
                return true;
            }
            return false;
        }
        return false;
    }
    
    /**
     * The eatBlockComment method eats the block comment until it encounters a "*" and "/".
     *      If the method encounters a "/" and "*" it increments the nestedCount variable to
     *      handle nested comments. If the nested variable is greater than 0, the method throws
     *      a ScanErrorException to signify an unclosed block comment.
     * @precondition the currentChar is a "/"
     * @postcondition eats the current block comment
     * @throws ScanErrorException if the nestedCount variable is greater than 0 or the parameter
     *      passed to the eat method does not match the currentChar instance field
     */
    private void eatBlockComment() throws ScanErrorException
    {
        int nestedCount = 1;
        while (nestedCount > 0 && hasNext())
        {
            eat(currentChar);
            if (currentChar == '*')
            {
                eat(currentChar);
                if (currentChar == '/')
                {
                    nestedCount--;
                }
            }
            else if (currentChar == '/')
            {
                eat(currentChar);
                if (currentChar == '*')
                {
                    nestedCount++;
                }
            }
        }
        if (nestedCount > 0)
        {
            throw new ScanErrorException("Unclosed block comment");
        }
    }
    
    /**
     * The isExtendableOperator method returns true if the input parameter is an operator
     *      that can be extended such as +=, <=, etc.
     * @param ch the character/operator to be checked
     * @precondition none
     * @postcondition returns the given ch parameter is an extendable operator
     * @return a boolean whether the input parameter is an operator that can be extended
     */
    private boolean isExtendableOperator(char ch)
    {
        return ch == '=' || ch == '+' || ch == '-' || ch == '*' || ch == '%' || ch == '<' || ch == '>' || ch == ':' || ch == '!';
    }    

    /**
     * The nextToken method returns the next token in the input stream. It checks if the
     *      currentChar starts a number, identifier, operator, or whitespace. The method
     *      automatically eats whitespace, and returns the next token. If the method encounters
     *      an end of file, it returns "END".
     * @return type String, the nextToken in the file stream
     * @precondition none
     * @postcondition returns the next token in the file stream
     * @throws ScanErrorException
     */
    public Token nextToken() throws ScanErrorException
    {
        while (hasNext() && isWhiteSpace(currentChar))
        {
            eat(currentChar);
        }
        if(eof)
        {
            return new Token(TOKEN_TYPE.EOF, "");
        }
        else if(isLetter(currentChar))
        {
            return scanIdentifier();
        }
        else if(isDigit(currentChar))
        {
            return scanNumber();
        }
        else if(isOperator(currentChar))
        {
            return scanOperator();
        }
        else {
            throw new ScanErrorException("Unexpected character: " + currentChar);
        }
    }
}