package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The VariableDeclaration class represents a VariableDeclaration node in the AST for which
 *      the variable declaration node declares a variable in the current environment.
 * @author Daniel Gergov
 * @version 12/3/23
 */
public class VariableDeclaration implements Statement
{
    private String[] names;

    /**
     * Constructor for the VariableDeclaration class creating a VariableDeclaration node for the
     *      AST. The constructor takes in a String array of the names of the variables to declare.
     * @param names the names of the variables to declare
     */
    public VariableDeclaration(String[] names)
    {
        this.names = names;
    }

    /**
     * A method inherited from the Statement interface to execute the variable declaration node.
     *      The method declares the variables in the current environment.
     * @param env type Environment the environment of where the exec method will run
     * @precondition env is not null
     * @postcondition the variables are declared in the current environment
     */
    @Override
    public void exec(Environment env)
    {
        for (String name : names)
        {
            env.declareVariable(name, null);
        }
    }

    /**
     * A method inherited from the Statement interface to compile the variable declaration node.
     *      The method compiles the variable declaration node by emitting the assembly code to
     *      allocate space in the .data section of an assembly program for the variable
     * @param e the emitter for which the expression will emit assembly code to
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        for (String name : names)
        {
            e.emit("var" + name + ": .word 0");
        }
    }
}