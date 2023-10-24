package src.ast;

import src.environments.Environment;

/**
 * The ProcedureDeclaration class represents the procedure declaration node of the AST.
 *      It stores the procedure identifier and the statement inside the procedure. The
 *      class also provides a method to execute the procedure declaration node of the
 *      AST. The method associates the procedure identifier and the statement inside
 *      the procedure together in the passed environment.
 * @author Daniel Gergov
 * @version 10/19/23
 */
public class ProcedureDeclaration implements Statement
{
    private String id;
    private Variable[] parameters;
    private Statement statement;

    /**
     * Constructor for objects of class ProcedureDeclaration
     * @param id type String the name of the procedure
     * @param parameters type Variable[] the parameters of the procedure
     * @param statement type Statement the statement that the procedure executes
     */
    public ProcedureDeclaration(String id, Variable[] parameters, Statement statement)
    {
        this.id = id;
        this.parameters = parameters;
        this.statement = statement;
    }

    /**
     * A method inherited from the Statement interface to execute the ProcedureDeclaration
     *      node of the AST. The method associates the procedure identifier and the statement
     *      inside the procedure together in the environment.
     * @precondition env is not null
     * @postcondition the procedure identifier and the statement inside the procedure are
     *      associated together
     * @param env type Environment the environment of where the exec method will run
     */
    @Override
    public void exec(Environment env)
    {
        env.setProcedure(id, this);
    }

    /**
     * Returns the statement stored inside the procedure declaration node of the AST.
     *      The statement is the statement that the procedure executes and is used
     *      by the ProcedureCall node of the AST to evaluate the value of the ProcedureCall
     *      node.
     * @return type Statement, the statement stored inside the procedure declaration node
     * @precondition none
     * @postcondition the statement stored in the ProcedureDeclaration node is returned
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Returns the parameter variables stored inside the procedure declaration node of the AST.
     *      The parameters of the declaration is used by the ProcedureCall node of the AST
     *      to evaluate the value of the ProcedureCall node.
     * @return type Variable[], the parameters stored inside the procedure declaration node
     * @precondition none
     * @postcondition the parameters stored in the ProcedureDeclaration node is returned
     */
    public Variable[] getParameters()
    {
        return parameters;
    }
}