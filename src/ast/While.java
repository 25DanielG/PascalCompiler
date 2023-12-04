package src.ast;

import java.util.HashMap;
import java.util.Map;
import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The While class represents a While ndoe in the AST for which the for node
 *      loops over and executes a statement a dictated by the beginning
 *      condition in the while heading as long as the condition stays true.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class While implements Statement
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
     * A method inherited from the Statement interface to execute the while node of
     *      the AST. The method evaluates the initial condition and executes the statement
     *      as long as the condition stays true after each iteration of the while loop.
     * @precondition env is not null
     * @postcondition the loop statement is executed
     * @param env type Environment the environment of where the exec method will run
     */
    @Override
    public void exec(Environment env) throws ParseErrorException
    {
        env.modifyLoopDepth(true);
        Map<Class<?>, Integer> map = new HashMap<Class<?>, Integer>();
        map.put(ContinueException.class, 1);
        map.put(BreakException.class, 2);
        map.put(ExitException.class, 3);
        while (condition.eval(env) == 1)
        {
            try
            {
                statement.exec(env);
            }
            catch (ParseErrorException e)
            {
                switch(map.get(e.getClass()))
                {
                    case 1:
                        continue;
                    case 2:
                        break;
                    case 3:
                        throw new ExitException();
                }
            }
        }
        env.modifyLoopDepth(false);
    }

    /**
     * A method inherited from the Statement interface to compile the while node of the AST.
     *      The method starts the compilation by adding the start label to the while loop, then
     *      compiling the condition expression, and if the condition is false, the assembly jumps
     *      to the term loop label. The method then compiles the main statement inside the while
     *      loop.
     * @param e type Emitter the emitter that will emit the compiled code
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        int id = e.nextLoopID();
        String label = "while" + id;
        e.emit(label + ":");
        String end = "term_while" + id;
        condition.compile(e, end);
        statement.compile(e);
        e.emit("j " + label + "\t# repeat the while loop");
        e.emit(end + ":");
    }
}
