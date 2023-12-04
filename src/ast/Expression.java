package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The expression interface that represents all AST nodes of type Expression
 *      for which that they can be evaluated to a specific value using the current
 *      environment.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public interface Expression
{
    /**
     * A method that represents the evalution of any expression AST node.
     * @param env the environment for which the expression will evaluate in.
     * @precondition env of type Environment is not null
     * @postcondition the current expression is evaluated and its valud is returned
     * @return type int, the integer for which the expression evaluates to
     */
    public int eval(Environment env);

    /**
     * A method that represents the compilation of any expression AST node.
     * @param e the emitter for which the expression will emit assembly code to
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null
     * @postcondition the AST node is compiled into MIPS assembly
     */
    public void compile(Emitter e, Object... args);
}
