package src.ast;

import src.emitter.Emitter;
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

    /**
     * A method inherited from the abstract Statement class to compile the statement.
     * @param e type Emitter, the emitter that will emit the compiled code
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    public void compile(Emitter e, Object... args);
}