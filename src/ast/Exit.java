package src.ast;

import src.emitter.Emitter;
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

    /**
     * A method inherited from the Statement interface to compile the exit node of the AST.
     *      The method emits the MIPS assembly code to exit the program by jumping to the 
     *      exit program label or if the program context is in a suroutine, to locate the
     *      $ra register in the stack and jump to the address stored in $ra while cleaning
     *      up the procedure's stack trace.
     * @param e type Emitter the emitter that will emit the compiled code
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        if (e.context == null)
        {
            e.emit("j program_exit" + "\t# exit from the current routine");
            return;
        }
        // find $ra in the stack
        e.emitPop("$ra");
        for (VariableDeclaration v : e.context.getLocals())
        {
            e.emitPop("$v0"); // pop local variables
        }
        e.emitPop("$v0"); // pop method return value
        e.emit("jr $ra\t# return");
    }
}