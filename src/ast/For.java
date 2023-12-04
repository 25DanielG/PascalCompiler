package src.ast;

import java.util.HashMap;
import java.util.Map;
import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The For class represents a For ndoe in the AST for which the for node
 *      loops over and executes a statement a certain number of times dictated
 *      by the begin statement and end expression.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class For implements Statement
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
     * A method inherited from the Statement interface to execute the for node of
     *      the AST. The method assigns the start variable to the evaluated expression and puts
     *      loops from the start variable to the end expression and executes the statement while
     *      the start variable is less than the end expression
     * @precondition env is not null
     * @postcondition the loop statement is executed
     * @param env type Environment the environment of where the exec method will run
     */
    @Override
    public void exec(Environment env) throws ParseErrorException
    {
        Assignment assign = (Assignment) begin;
        assign.exec(env);
        int count = env.getVariable(assign.getName());
        int bound = end.eval(env);
        env.modifyLoopDepth(true);
        Map<Class<?>, Integer> map = new HashMap<Class<?>, Integer>();
        map.put(ContinueException.class, 1);
        map.put(BreakException.class, 2);
        map.put(ExitException.class, 3);
        whileLoop: // label the while loop
        while (count <= bound)
        {
            try
            {
                statement.exec(env);
                env.setVariable(assign.getName(), ++count);
            }
            catch (ParseErrorException e)
            {
                switch(map.get(e.getClass()))
                {
                    case 1:
                        env.setVariable(assign.getName(), ++count);
                        continue;
                    case 2:
                        break whileLoop; // break out of the while loop not the switch case
                    case 3:
                        throw new ExitException();
                }
            }
        }
        env.modifyLoopDepth(false);
    }

    /**
     * A method inherited from the Statement interface to compile the for node of the AST.
     *      The method compiles the for node by assigning the start variable to the evaluated
     *      expression assigned to the for loop variable. The method then subtracts 1 from the
     *      start variable in order to allow the incrementing step to be at the beginning of the
     *      for loop. The method then compiles the end expression and compares the start variable
     *      to the end variable. If the start variable is greater than the end variable, the for
     *      loop jumps to the term loop label.
     * @param e type Emitter the emitter that will emit the compiled code
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        int id = e.nextLoopID();
        String label = "for" + id;
        String term = "term_for" + id;
        Assignment assign = (Assignment) begin;
        assign.decrementExp();
        assign.compile(e);
        String variable = "var" + ((Assignment) begin).getName();
        e.emit("lw $t1, " + variable + "\t# load the start variable into $t1");
        e.emitPush("$t1");
        e.emit("li $t2, " + this.end.eval(null) + "\t# load the end number into $t2");
        e.emitPush("$t2");
        e.emit(label + ":");
        e.emitPop("$t2");
        e.emitPop("$t1");
        e.emit("addiu $t1, $t1, 1" + "\t# increment the for loop counter");
        e.emit("sw $t1, " + variable);
        e.emit("bgt $t1, $t2, " + term + "\t# check if the for loop is done");
        e.emitPush("$t1");
        e.emitPush("$t2");
        statement.compile(e);
        e.emit("j " + label + "\t# repeat the for loop");
        e.emit(term + ":");
    }
}
