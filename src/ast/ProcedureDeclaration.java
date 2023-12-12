package src.ast;

import src.emitter.Emitter;
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
    private VariableDeclaration[] locals;

    /**
     * Constructor for objects of class ProcedureDeclaration
     * @param id type String the name of the procedure
     * @param parameters type Variable[] the parameters of the procedure
     * @param statement type Statement the statement that the procedure executes
     */
    public ProcedureDeclaration(String id, Variable[] parameters, Statement statement, VariableDeclaration[] locals)
    {
        this.id = id;
        this.parameters = parameters;
        this.statement = statement;
        this.locals = locals;
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

    /**
     * Returns the local variables stored inside the procedure declaration node of the AST.
     *      The local variables of the declaration is used by the emitter to detect the local
     *      variables of a procedure.
     * @return type VariableDeclaration[], the local variables stored inside the procedure declaration
     *      node
     * @precondition none
     * @postcondition the local variables stored in the ProcedureDeclaration node is returned
     */
    public VariableDeclaration[] getLocals()
    {
        return locals;
    }

    /**
     * Returns the identifier stored inside the procedure declaration node of the AST.
     *      The method essentially returns the name of the procedure as a String,
     * @return type String, the identifier stored inside the procedure declaration node
     */
    public String getId()
    {
        return id;
    }

    @Override
    public void compile(Emitter e, Object... args)
    {
        e.emit("proc" + id + ":");
        e.emit("la $t0, null");
        e.emit("move $v0, $t0\t# load null into $v0"); // put null in method return value
        e.emitPush("$v0");
        for (VariableDeclaration v : locals)
        {
            e.emit("move $v0, $t0\t# load null into local variable");
            e.emitPush("$v0"); // push null for local variables
        }
        e.setProcedureContext(this);
        e.emitPush("$ra");
        this.statement.compile(e);
        e.emitPop("$ra");
        for (VariableDeclaration v : locals)
        {
            e.emitPop("$v0"); // pop local variables
        }
        e.emitPop("$v0"); // pop method return value
        e.emit("jr $ra\t# return");
        e.clearProcedureContext();
    }
}