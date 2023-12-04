package src.ast;

import src.emitter.Emitter;
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

    /**
     * A method inherited from the Statement interface to compile the if else node. The method
     *      compiles the if else node by first compiling the condition. If the condition is false
     *      the program jumps to the else label. If the condition is true, the program executes
     *      the main statement inside the if node. After the main statement is executed, the
     *      compiled code jumps to the end if label.
     * @param e the emitter for which the expression will emit assembly code to
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        String label = "endif" + e.nextIfID();
        String elseLabel = "elseif" + e.nextIfID();
        condition.compile(e, elseLabel);
        statement.compile(e);
        e.emit("j " + label + "\t# jump to end of if statement");
        e.emit(elseLabel + ":");
        elseStatement.compile(e);
        e.emit(label + ":");
    }
}
