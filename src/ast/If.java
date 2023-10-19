package src.ast;

import src.environments.Environment;

/**
 * The If class represents an If node in the AST for which the if node for which
 *      if the condition is true, the statement inside the If will be executed.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class If extends Statement
{
    private Condition condition;
    private Statement statement;

    /**
     * Constructor for the If class creating an If node for the AST.
     * @param condition the condition that the If node will check to be true or false
     * @param statement the statement to execute if the condition is true
     */
    public If(Condition condition, Statement statement)
    {
        this.condition = condition;
        this.statement = statement;
    }

    /**
     * A method inherited from the abstract Statement class to execute the if node of
     *      the AST. The method evaluates the condition expression, and if it is true, the node
     *      executes the statement inside the if node.
     * @precondition env is not null
     * @postcondition the if statement is executed
     * @param env type Environment the environment of where the exec method will run
     */
    @Override
    public void exec(Environment env) throws ContinueException, BreakException
    {
        if (condition.eval(env) == 1)
        {
            try
            {
                statement.exec(env);
            }
            catch (ContinueException e)
            {
                throw new ContinueException("");
            }
            catch (BreakException e)
            {
                throw new BreakException("");
            }
        }
    }
}
