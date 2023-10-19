package src.ast;

import src.environments.Environment;

/**
 * The For class represents a For ndoe in the AST for which the for node
 *      loops over and executes a statement a certain number of times dictated
 *      by the begin statement and end expression.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class For extends Statement
{
    private Statement begin;
    private Expression end;
    private Statement statement;

    /**
     * Constructor for the For class creating a For node for the AST.
     * @param begin the begin statement which is an Assignment node that decalares the start
     *      variable
     * @param end the end value for which the start variable will reach
     * @param statement the statement to loop over and execute repeatedly
     */
    public For(Statement begin, Expression end, Statement statement)
    {
        this.begin = begin;
        this.end = end;
        this.statement = statement;
    }

    /**
     * A method inherited from the abstract Statement class to execute the for node of
     *      the AST. The method assigns the start variable to the evaluated expression and puts
     *      loops from the start variable to the end expression and executes the statement while
     *      the start variable is less than the end expression
     * @precondition env is not null
     * @postcondition the loop statement is executed
     * @param env type Environment the environment of where the exec method will run
     */
    @Override
    public void exec(Environment env)
    {
        Assignment assign = (Assignment) begin;
        assign.exec(env);
        int count = env.getVariable(assign.getName());
        int bound = end.eval(env);
        env.modifyLoopDepth(true);
        while (count <= bound)
        {
            try
            {
                statement.exec(env);
                env.setVariable(assign.getName(), ++count);
            }
            catch (BreakException e)
            {
                break;
            }
            catch (ContinueException e)
            {
                env.setVariable(assign.getName(), ++count);
                continue;
            }
        }
        env.modifyLoopDepth(false);
    }
}
