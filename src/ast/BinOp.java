package src.ast;

import src.environments.Environment;

/**
 * The BinOp class represents a binary operator node on the AST
 *      which is a combination of a left and right hand side
 *      each expressions and a binary operator spanning from
 *      *, +, -, /, and mod.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class BinOp extends Expression
{
    private Expression left;
    private Expression right;
    private String op;

    /**
     * Constructor for the BinOp class
     * @param left the left side of the expression, type Expression
     * @param right the right side of the expression, type Expression
     * @param op the operator between the left and right side, type String
     */
    public BinOp(Expression left, Expression right, String op)
    {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    /**
     * A method inherited from the abstract Statement class to execute the binary
     *      operator node of the AST. The method checks the type of the operator
     *      evalutaed the left and right side expressions and returns the resulting value.
     * @precondition env is not null
     * @postcondition the expression is evaluated
     * @param env type Environment the environment of where the exec method will run
     * @throws IllegalArgumentException if the operator is not recognized
     */
    @Override
    public int eval(Environment env)
    {
        switch(this.op)
        {
            case "+":
                return left.eval(env) + right.eval(env);
            case "-":
                return left.eval(env) - right.eval(env);
            case "*":
                return left.eval(env) * right.eval(env);
            case "/":
                return left.eval(env) / right.eval(env);
            case "mod":
                return left.eval(env) % right.eval(env);
            default:
                throw new IllegalArgumentException("Unexpected operator in expression evaluation");
        }
    }
}
