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
        e.emit("li $v0 10");
        e.emit("syscall");

        for (String var : e.getVariables())
        {
            e.prepend("var" + var + ": .word 0");
        }
        e.prepend("new_line: .asciiz \"\\n\"");
        e.prepend(".data");

        e.push();
        e.close();
    }
}