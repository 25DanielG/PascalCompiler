package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The If class represents an If node in the AST for which the if node for which
 *      if the condition is true, the statement inside the If will be executed.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class If implements Statement
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
     * A method inherited from the Statement interface to execute the if node of
     *      the AST. The method evaluates the condition expression, and if it is true, the node
     *      executes the statement inside the if node.
     * @precondition env is not null
     * @postcondition the if statement is executed
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
        }
    }

    /**
     * A method inherited from the Statement interface to compile the if node of the AST.
     *      The method starts the compilation by compiling the condition expression, and if
     *      the condition is false, the assembly jumps to the end of the if statement. The
     *      method also compiles the body of the if statement in case the condition is true.
     * @param e type Emitter the emitter that will emit the compiled code
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        String label = "endif" + e.nextIfID();
        condition.compile(e, label);
        statement.compile(e);
        e.emit(label + ":");
    }
}