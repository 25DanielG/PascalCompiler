package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The Writeln class represents a Writeln node in the AST for which the writeln
 *      statement prints a certain expression to console. The class inherits the
 *      Statement interface and provides an exec method to execute the writeln statement
 *      which uses Java's System.out.println() method to print the write to console.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Writeln implements Statement
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
     * A method inherited from the Statement interface to execute the writeln by
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

    /**
     * A method inherited from the Statement interface to compile the writeln. The method
     *      compiles the expression and prints the returned value to console. The method
     *      then loads the new line variable into the $a0 register and prints a new line.
     * @param e the emitter for which the expression will emit assembly code to
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the emitter object is not null, and the args parameter is empty
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        exp.compile(e);
        e.emit("move $a0, $v0");
        e.emit("li $v0, 1" + "\t# print the expression");
        e.emit("syscall");
        e.emit("la $a0, new_line" + "\t# print a new line");
        e.emit("li $v0, 4");
        e.emit("syscall");
    }
}