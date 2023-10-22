package src.ast;

import src.environments.Environment;

/**
 * The exit class represents an exit node in the AST for which the exit node
 *      throws an exit exception to exit the program. The class inherits the
 *      Statement class and provides an exec method to execute the exit node.
 *      The exit node specifically exits out of the current subroutine, so the
 *      current procedure or the main program depending on where the statement
 *      was called.
 * @author Daniel Gergov
 * @version 10/20/23
 */
public class Exit implements Statement
{
    /**
     * The exec method executes the exit node by throwing an exit exception
     *      to exit the program. The exit exception is caught by the ProcedureCall
     *      node of the program node and the subroutine is exited.
     * @param env the Environment of where the exec method will run
     * @precondition env is not null
     * @postcondition the exit exception is thrown
     * @throws ParseErrorException the parent of ExitException
     */
    @Override
    public void exec(Environment env) throws ParseErrorException
    {
        if (env == null)
        {
            throw new RuntimeException("Environment is null, cannot exit.");
        }
        throw new ExitException();
    }
}