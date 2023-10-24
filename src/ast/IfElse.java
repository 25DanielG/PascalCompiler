package src.ast;

import src.environments.Environment;

/**
 * The IfElse class represents an IfElse node in the AST for which the if else node for which
 *      if the condition is true, the statement inside the If will be executed. If the condition
 *      is false, the statement inside the else will be executed instead.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class IfElse implements Statement
{
    private Condition condition;
    private Statement statement;
    private Statement elseStatement;

    /**
     * Constructor for the IfElse class creating an IfElse node for the AST.
     * @param condition the condition that the IfElse node will check to be true or false
     * @param statement the statement to execute if the condition is true
     * @param elseStatement the statement to execute if the condition is false
     */
    public IfElse(Condition condition, Statement statement, Statement elseStatement)
    {
        this.condition = condition;
        this.statement = statement;
        this.elseStatement = elseStatement;
    }

    /**
     * A method inherited from the Statement interface to execute the if else node of
     *      the AST. The method evaluates the condition expression, and if it is true, the node
     *      executes the statement inside the if node. If the condition is false, the node
     *      executes the statement inside the else.
     * @precondition env is not null
     * @postcondition the if else statement is executed
     * @param env type Environment the environment of where the exec method will run
     */
    @Override
    public void exec(Environment env) throws ParseErrorException
    {
        if (condition.eval(env) == 1)
        {
            try
            {
                statement.exec(env);
            }
            catch (ParseErrorException e)
            {
                throw e;
            }
        } else {
            try
            {
                elseStatement.exec(env);
            }
            catch (ParseErrorException e)
            {
                throw e;
            }
        }
    }
}
