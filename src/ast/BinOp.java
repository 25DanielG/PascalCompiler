package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The BinOp class represents a binary operator node on the AST
 *      which is a combination of a left and right hand side
 *      each expressions and a binary operator spanning from
 *      *, +, -, /, and mod.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class BinOp implements Expression
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
     * A method inherited from the Statement interface to execute the binary
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

    @Override
    public void compile(Emitter e, Object... args)
    {
        left.compile(e);
        e.emitPush("$v0");
        right.compile(e);
        e.emitPop("$t0");
        switch(this.op)
        {
            case "+":
                e.emit("add $v0, $t0, $v0");
                break;
            case "-":
                e.emit("sub $v0, $t0, $v0");
                break;
            case "*":
                e.emit("mult $t0, $v0");
                e.emit("mflo $v0");
                break;
            case "/":
                e.emit("div $t0, $v0");
                e.emit("mflo $v0");
                break;
            case "mod":
                e.emit("div $t0, $v0");
                e.emit("mfhi $v0");
                break;
            default:
                throw new IllegalArgumentException("Unexpected operator in expression evaluation");
        }
    }
}
