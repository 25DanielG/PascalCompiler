package src.ast;

import src.environments.Environment;

/**
 * The While class represents a While ndoe in the AST for which the for node
 *      loops over and executes a statement a dictated by the beginning
 *      condition in the while heading as long as the condition stays true.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class While extends Statement
{
    private Condition condition;
    private Statement statement;

    /**
     * Constructor for the While class creating a While node for the AST.
     * @param condition the condition in the while heading to execute the statements while the
     *      conidition is true
     * @param statement the statement to execute repeatedly while the condition is true
     */
    public While(Condition condition, Statement statement)
    {
        this.condition = condition;
        this.statement = statement;
    }

    /**
     * A method inherited from the abstract Statement class to execute the while node of
     *      the AST. The method evaluates the initial condition and executes the statement
     *      as long as the condition stays true after each iteration of the while loop.
     * @precondition env is not null
     * @postcondition the loop statement is executed
     * @param env type Environment the environment of where the exec method will run
     */
    @Override
    public void exec(Environment env)
    {
        env.modifyLoopDepth(true);
        while (condition.eval(env) == 1)
        {
            try
            {
                statement.exec(env);
            }
            catch (BreakException e)
            {
                break;
            }
            catch (ContinueException e)
            {
                continue;
            }
        }
        env.modifyLoopDepth(false);
    }
}