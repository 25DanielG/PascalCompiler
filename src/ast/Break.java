package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * THe Break class represents a break node in the AST which is used to break
 *      from loops by throwing errors.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Break implements Statement
{
    private Statement statement;
    private int id;

    public Break(Statement statement, int id)
    {
        if (!(statement instanceof While) && !(statement instanceof For))
        {
            throw new IllegalArgumentException("Break statement requires a While or For statement.");
        }
        this.statement = statement;
        this.id = id;
    }

    /**
     * A method inherited from the Statement interface to execute the break node of
     *      the AST. The method breaks from the loop that the node is currently inside of
     *      by throwing a BreakException.
     * @precondition env is not null
     * @postcondition the break statement is executed
     * @param env type Environment the environment of where the exec method will run
     * @throws BreakException by design and function
     * @throws IllegalArgumentException if the break statement is not used inside a loop
     */
    @Override
    public void exec(Environment env) throws BreakException
    {
        if (env.getLoopDepth() == 0) {
            throw new IllegalArgumentException("Break statement can only be used within a loop.");
        }
        throw new BreakException();
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
        e.emit("j term_" + loop + this.id);
    }
}
