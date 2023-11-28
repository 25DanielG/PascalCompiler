package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * THe Break class represents a continue node in the AST which is used to continue
 *      in loops by throwing errors.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Continue implements Statement
{
    private Statement statement;
    private int id;

    public Continue(Statement statement, int id)
    {
        if (!(statement instanceof While) && !(statement instanceof For))
        {
            throw new IllegalArgumentException("Continue statement requires a While or For statement.");
        }
        this.statement = statement;
        this.id = id;
    }

    /**
     * A method inherited from the Statement interface to execute the continue node of
     *      the AST. The method continues in the loop that the node is currently inside of
     *      by throwing a ContinueException.
     * @precondition env is not null
     * @postcondition the continue statement is executed
     * @param env type Environment the environment of where the exec method will run
     * @throws ContinueException by design and function
     * @throws IllegalArgumentException if the break statement is not used inside a loop
     */
    @Override
    public void exec(Environment env) throws ContinueException
    {
        if (env.getLoopDepth() == 0) {
            throw new IllegalArgumentException("Continue statement can only be used within a loop.");
        }
        throw new ContinueException("");
    }

    public void compile(Emitter e, Object... args)
    {
        String loop;
        if (this.statement instanceof While)
        {
            loop = "while";
        }
        else
        {
            loop = "for";
        }
        e.emit("j " + loop + this.id);
    }
}
