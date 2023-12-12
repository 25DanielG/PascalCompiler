package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The Variable class represents a Variable node in the AST for which the variable
 *      is an identifier and can be evaluated by looking up the identifier in the environment
 *      hash map.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Variable implements Expression
{
    private String name;

    /**
     * Constructor for the Variable class creating a Variable node for the AST.
     * @param name the name of the variable stored in the Variable node
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Returns the name of the variable stored in the Variable node.
     * @return type String, the name of the variable stored in the Variable node
     */
    public String getName()
    {
        return name;
    }

    /**
     * A method inherited from the Statement interface to execute the variable which
     *      evaluates the variable node by looking up the identifier in the environment
     *      hash map and returning the returned value.
     * @parameter type Environment the env for which the expression evaluates in
     * @precondition the env is not null
     * @postcondition the expression is evaluated
     * @return type int, the value of the variable node
     */
    @Override
    public int eval(Environment env)
    {
        return env.getVariable(this.name);
    }

    /**
     * A method inherited from the Statement interface to compile the variable. The method
     *      loads the variable value into the $v0 register by loading its address to the $t0
     *      register. The method also tells the emitter by calling .addVariable() to store
     *      the variable name in order to allocate space for the variable in the .data section.
     *      This call adds support for using variables without declaring them.
     * @param e the emitter for which the expression will emit assembly code to
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        if (e.isLocal(this.name))
        {
            e.emit("lw $v0, " + e.getOffset(this.name) + "($sp)" + "\t# load the variable into $v0");
        }
        else
        {
            e.emit("la $t0, var" + this.name);
            e.emit("lw $v0, ($t0)" + "\t# load the variable into $v0");
        }
    }
}
