package src.ast;

import src.environments.Environment;

/**
 * The Variable class represents a Variable node in the AST for which the variable
 *      is an identifier and can be evaluated by looking up the identifier in the environment
 *      hash map.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Variable implements Expression
{
    private String name;

    /**
     * Constructor for the Variable class creating a Variable node for the AST.
     * @param name the name of the variable stored in the Variable node
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Returns the name of the variable stored in the Variable node.
     * @return type String, the name of the variable stored in the Variable node
     */
    public String getName()
    {
        return name;
    }

    /**
     * A method inherited from the Statement interface to execute the variable which
     *      evaluates the variable node by looking up the identifier in the environment
     *      hash map and returning the returned value.
     * @parameter type Environment the env for which the expression evaluates in
     * @precondition the env is not null
     * @postcondition the expression is evaluated
     * @return type int, the value of the variable node
     */
    @Override
    public int eval(Environment env)
    {
        return env.getVariable(this.name);
    }
}
