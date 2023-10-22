package src.ast;

/**
 * The ExitException class represents an exit exception node in the AST for which the exit
 *      exception node is thrown by the exit node in the AST. The exit exception is caught
 *      by the ProcedureCall node or the program node and the subroutine is exited.
 * @author Daniel Gergov
 * @version 10/20/23
 */
public class ExitException extends ParseErrorException
{
    /**
     * Default constructor for the ExitException class with no parameters
     */
    public ExitException()
    {
        super();
    }

    /**
     * Constructor for the ExitException class with a reason parameter
     * @param reason the reason for which the ExitException was thrown
     */
    public ExitException(String reason)
    {
        super(reason);
    }
}