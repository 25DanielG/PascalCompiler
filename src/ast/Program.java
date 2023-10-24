package src.ast;

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
    private ProcedureDeclaration[] procedureDeclarations;
    private Statement statement;

    /**
     * Constructor for objects of class Program creating a Program node for the AST.
     * @param procedureDeclarations type ProcedureDeclaration[] the procedure declarations
     * @param statement type Statement the statement inside the program
     */
    public Program(ProcedureDeclaration[] procedureDeclarations, Statement statement)
    {
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
}