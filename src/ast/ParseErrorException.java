package src.ast;

/**
 * The parent class of the ContinueException, BreakException, and ExitException in order
 *      to group together the exceptions thrown by the AST classes in the parser package.
 * @author Daniel Gergov
 * @version 10/20/23
 */
public class ParseErrorException extends Exception
{
    public ParseErrorException()
    {
        super();
    }

    public ParseErrorException(String reason)
    {
        super(reason);
    }
}