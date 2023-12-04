package src.ast;

import src.emitter.Emitter;
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
     * A method inherited from the Statement interface to execute the block
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

    /**
     * A method inherited from the Statement interface to compile the block node of the AST.
     *      The block node which consists of multiple statements is compiled by compiling each
     *      of the statements within the node.
     * @param e type Emitter the emitter that will emit the compiled code
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        for (Statement statement : statements)
        {
            statement.compile(e);
        }
    }
}
