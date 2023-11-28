package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The Assignment class represents an assignment node in the AST which
 *      contains a name and an expression to which a variable was assigned to.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Assignment implements Statement
{
    private String name;
    private Expression exp;

    /**
     * Constructor for the Assignment node of the AST
     * @param name the name of the variable that is being assigned
     * @param exp the expression to which the variable is assigned to
     */
    public Assignment(String name, Expression exp)
    {
        this.name = name;
        this.exp = exp;
    }

    /**
     * A getter method to get the name of the variable being assigned.
     * @precondition none
     * @postcondition returns the name of the variable being assigned
     * @return String the name of the variable
     */
    public String getName()
    {
        return this.name;
    }

    public void decrementExp()
    {
        Expression e = this.exp;
        BinOp b = new BinOp(e, new Number(1), "-");
        this.exp = b;
    }

    /**
     * A method inherited from the Statement interface to execute the assignment node of
     *      the AST. The method assigns the variable to the evaluated expression and puts
     *      the variable inside the environment hash map. If the variable is not defined
     *      in the environment, an exception is thrown.
     * @precondition env is not null and the variable is inside the environment
     * @postcondition the statement is executed
     * @param env type Environment the environment of where the exec method will run
     * @throws IllegalArgumentException if the variable is not defined in the environment
     */
    @Override
    public void exec(Environment env)
    {
        if (env.getVariable(name) == null)
        {
            throw new IllegalArgumentException("Variable " + name + " is not defined");
        }
        env.setVariable(name, exp.eval(env));
    }

    @Override
    public void compile(Emitter e, Object... args)
    {
        exp.compile(e);
        e.emit("sw $v0, var" + name);
    }
}
