package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The Condition class represents a condition node in the AST which is used to represent a
 *      condition in any if statement or while loop header. The condition is an expression
 *      that evaluates to either 0 or 1 reperesenting false or true respectively.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Condition implements Expression
{
    private Expression left;
    private Expression right;
    private String op;

    /**
     * Constructor for the Condition class
     * @param left the left side of the condition expression of type Expression
     * @param right the right side of the condition expression of type Expression
     * @param op the operator of the condition representing a relative operator like =, <>, <=,
     *      >=, <, >
     */
    public Condition(Expression left, Expression right, String op)
    {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    /**
     * A method inherited from the Statement interface to execute the condition
     *      node of the AST. The method checks the type of the operator with every relative
     *      opeerator and evaluates the left and right side expressions and returns 0 or 1
     *      representing a boolean value.
     * @precondition env is not null
     * @postcondition the expression is evaluated to 0 or 1
     * @param env type Environment the environment of where the exec method will run
     * @throws IllegalArgumentException if the operator is not recognized
     */
    @Override
    public int eval(Environment env)
    {
        switch(this.op)
        {
            case "=":
                return left.eval(env) == right.eval(env) ? 1 : 0;
            case "<>":
                return left.eval(env) != right.eval(env) ? 1 : 0;
            case "<=":
                return left.eval(env) <= right.eval(env) ? 1 : 0;
            case ">=":
                return left.eval(env) >= right.eval(env) ? 1 : 0;
            case ">":
                return left.eval(env) > right.eval(env) ? 1 : 0;
            case "<":
                return left.eval(env) < right.eval(env) ? 1 : 0;
            default:
                throw new IllegalArgumentException("Unexpected operator in expression evaluation");
        }
    }

    public void compile(Emitter e, Object... args)
    {
        if (args.length != 1 || !(args[0] instanceof String))
        {
            throw new IllegalArgumentException("Expected 1 argument for condition compilation");
        }
        left.compile(e);
        e.emitPush("$v0");
        right.compile(e);
        e.emitPop("$t0");
        String targetLabel = (String) args[0];
        switch (op)
        {
            case "=":
                e.emit("bne $t0, $v0, " + targetLabel);
                break;
            case "<>":
                e.emit("beq $t0, $v0, " + targetLabel);
                break;
            case "<=":
                e.emit("bgt $t0, $v0, " + targetLabel);
                break;
            case ">=":
                e.emit("blt $t0, $v0, " + targetLabel);
                break;
            case ">":
                e.emit("ble $t0, $v0, " + targetLabel);
                break;
            case "<":
                e.emit("bge $t0, $v0, " + targetLabel);
                break;
            default:
                throw new IllegalArgumentException("Unexpected operator in expression evaluation");
        }
    }
}
