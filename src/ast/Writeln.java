package src.ast;

import src.environments.Environment;

/**
 * The Writeln class represents a Writeln node in the AST for which the writeln
 *      statement prints a certain expression to console. The class inherits the
 *      Statement class and provides an exec method to execute the writeln statement
 *      which uses Java's System.out.println() method to print the write to console.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Writeln extends Statement
{
    private Expression exp;
    
    /**
     * Constructor for the Writeln class creating a Writeln node for the AST.
     * @param exp type Expression the exp to write to console using Java's System.out.println()
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * A method inherited from the abstract Statement class to execute the writeln by
     *      calling Java's System.out.println() method to write the expression to console.
     *      The method evaluates the expression and prints the returned value to console.
     * @param env the environment of where the exec method will run
     * @precondition env is not null
     * @postcondition the writeln statement is executed
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }
}