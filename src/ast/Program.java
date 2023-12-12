package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

/**
 * The Program class represents the program node of the AST. It stores the procedure
 *      declarations and the statement inside the program. The class also provides a
 *      method to execute the program node of the AST. The method executes the procedure
 *      declarations and the statement inside the program node of the AST.
 * @author Daniel Gergov
 * @version 10/19/23
 */
public class Program implements Statement
{
    private VariableDeclaration[] variableDeclarations;
    private ProcedureDeclaration[] procedureDeclarations;
    private Statement statement;

    /**
     * Constructor for objects of class Program creating a Program node for the AST.
     * @param procedureDeclarations type ProcedureDeclaration[] the procedure declarations
     * @param statement type Statement the statement inside the program
     */
    public Program(VariableDeclaration[] variableDeclarations, ProcedureDeclaration[] procedureDeclarations, Statement statement)
    {
        this.variableDeclarations = variableDeclarations;
        this.procedureDeclarations = procedureDeclarations;
        this.statement = statement;
    }

    /**
     * A method inherited from the Statement interface to execute the Program node
     *      of the AST. The method executes the procedure declarations and the statement
     *      inside the program node of the AST.
     * @precondition env is not null
     * @postcondition the procedure declarations and the statement inside the program
     *     are executed
     * @param env type Environment the environment of where the exec method will run
     * @throws RuntimeException if the break or continue statement is not inside a loop
     */
    @Override
    public void exec(Environment env)
    {
        for (ProcedureDeclaration procedureDeclaration : procedureDeclarations)
        {
            procedureDeclaration.exec(env);
        }
        try
        {
            statement.exec(env);
        }
        catch (ParseErrorException e)
        {
            if (e instanceof ContinueException || e instanceof BreakException)
            {
                throw new RuntimeException("Break/Continue statement not inside loop");
            }
        }
    }

    /**
     * A method inherited from the Statement interface to compile the Program node.
     *      The method compiles the program node by appending the start of a MIPS
     *      program to the emitter. The method adds the compiled main program statement
     *      to the emitter. After compiling the main code, the method compiles the
     *      procedure decalarations. Then, the method prepends the
     *      variable section of an assembly program to the emitter. The emitter's .getVariables()
     *      method gives this method all the variables seen in the program.
     * @param e type Emitter the emitter that will emit the compiled code
     * @param args a varargs parameter type Object, the arguments passed to the compile method
     * @precondition the args parameter contains the output file and is of length 1
     * @postcondition the AST node is compiled into MIPS assembly
     */
    @Override
    public void compile(Emitter e, Object... args)
    {
        if (args.length != 1 || !(args[0] instanceof String))
        {
            throw new IllegalArgumentException("Expected 1 argument of type String");
        }
        String outputFile = (String) args[0];
        e = new Emitter(outputFile);
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        statement.compile(e);
        e.emit("program_exit:"); // program exit
        e.emit("li $v0 10" + "\t# exit the program");
        e.emit("syscall");
        e.emit("program_error:"); // program error
        e.emit("li $v0 4" + "\t# print error message");
        e.emit("la $a0 error_message");
        e.emit("syscall");
        e.emit("j program_exit");

        for (ProcedureDeclaration procedureDeclaration : procedureDeclarations)
        {
            procedureDeclaration.compile(e);
        }

        for (VariableDeclaration variableDeclaration : variableDeclarations)
        {
            variableDeclaration.compile(e);
        }
        e.prepend("new_line: .asciiz \"\\n\"" + "\t# new line variable");
        e.prepend("error_message: .asciiz \"RuntimeError: null assignment, program quit unexpectedly\"" + "\t# error message");
        e.prepend("null: .word 0" + "\t# null variable");
        e.prepend(".data");

        e.push();
        e.close();
    }
}