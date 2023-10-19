package src.ast;

/**
 * This class creates the specific exception called ContinueException which is used by
 *      the continue AST node. This error is used to distinguish between other thrown errors
 *      and to continue in the execution of any loop node.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class ContinueException extends Exception
{
    /**
     * Default constructor for the ContinueException class with no parameters
     */
    public ContinueException()
    {
        super();
    }

    /**
     * Constructor for the ContinueException class with a reason parameter
     * @param reason the reason for which the ContinueException was thrown
     */
    public ContinueException(String reason)
    {
        super(reason);
    }
}