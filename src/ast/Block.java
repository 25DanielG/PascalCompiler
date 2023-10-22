package src.ast;

import src.environments.Environment;

/**
 * The Block class represents a block node in the AST which represents a list
 *      of statements wrapped inside a block.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Block implements Statement
{
    private Statement[] statements;

    /**
     * Constructor for the Block AST class
     * @param statements the statements that are wrapped inside the block class
     */
    public Block(Statement[] statements)
    {
        this.statements = statements;
    }

    /**
     * A method inherited from the abstract Statement class to execute the block
     *      node of the AST. The method checks loops over every statement contained in
     *      the block and executes each statement.
     * @precondition env is not null
     * @postcondition the statement is executed
     * @param env type Environment the environment of where the exec method will run
     * @throws ParseErrorException if a statement inside the block is a continue, break, exit node
     */
    @Override
    public void exec(Environment env) throws ParseErrorException
    {
        for (int i = 0; i < statements.length; ++i)
        {
            try
            {
                statements[i].exec(env);
            }
            catch (ParseErrorException e)
            {
                throw e;
            }
        }
    }
}
