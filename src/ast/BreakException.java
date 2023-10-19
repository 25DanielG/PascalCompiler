package src.ast;

/**
 * This class creates the specific exception called BreakException which is used by
 *      the break AST node. This error is used to distinguish between other thrown errors
 *      and to stop the execution of any loop node.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class BreakException extends Exception
{
    /**
     * Default constructor for the BreakException class with no parameters
     */
    public BreakException()
    {
        super();
    }

    /**
     * Constructor for the BreakException class with a reason parameter
     * @param reason the reason for which the BreakException was thrown
     */
    public BreakException(String reason)
    {
        super(reason);
    }
}