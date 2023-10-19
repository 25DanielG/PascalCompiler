package src.ast;

import src.environments.Environment;

/**
 * The Number class represents a Number node in the AST for which the number node
 *      which stores an integer value and returns the value when evaluated in the
 *      eval method.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Number extends Expression
{
    private int value;

    /**
     * Constructor for the Number class creating a Number node for the AST.
     * @param value the value of the number node to store
     */
    public Number(int value)
    {
        this.value = value;
    }

    /**
     * A method inherited from the abstract Statement class to execute the number
     *      node of the AST. The method returns the value of the number node in
     *      evaluation.
     * @precondition env is not null
     * @postcondition the expression is evaluated
     * @param env type Environment the environment of where the exec method will run
     */
    @Override
    public int eval(Environment env)
    {
        return value;
    }
}
