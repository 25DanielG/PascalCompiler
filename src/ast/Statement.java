package src.ast;

import src.environments.Environment;

/**
 * The Statement class represents a Statement node in the AST for which the statement
 *      is an inferface that is implemented by the other statement nodes in the AST.
 *      The statement interface provides a exec method that defines how the statement is 
 *      executed.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public interface Statement
{
    /**
     * A method inherited from the abstract Statement class to execute the statement.
     * @param env type Environment, the environment of where the exec method will run
     * @precondition the env parameter is not null
     * @postcondition the statement is executed
     * @throws ParseErrorException to handle the break, continue, or exit statements
     */
    public void exec(Environment env) throws ParseErrorException;
}