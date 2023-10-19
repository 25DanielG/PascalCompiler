package src.ast;

import javax.swing.text.html.HTMLDocument.Iterator;
import src.environments.Environment;

/**
 * The ProcedureCall class represents a ProcedureCall node in the AST for which the
 *      procedure call executes the statement inside the procedure declaration. The
 *      class inherits the Expression class and provides an eval method to evaluate
 *      the procedure call by executing the statement inside the procedure declaration
 *      using the exec method of the statement class. The eval method returns 0 since
 *      it is the default return value.
 * @author Daniel Gergov
 * @version 10/19/23
 */
public class ProcedureCall extends Expression
{
    private String id;
    private Expression[] arguments;

    /**
     * Constructor for objects of class ProcedureCall creating a ProcedureCall node for the AST.
     * @param id type String the name of the procedure
     * @param arguments type Expression[] the arguments of the procedure
     */
    public ProcedureCall(String id, Expression[] arguments)
    {
        this.id = id;
        this.arguments = arguments;
    }

    /**
     * A method inherited from the abstract Expression class to evaluate the procedure call by
     *      executing the statement inside the procedure declaration using the exec method of
     *      the statement class. The method returns 0 since it is the default return value.
     * @param env type Environment the environment of where the eval method will run
     * @return type int, 0 since it is the default return value
     * @precondition env is not null
     * @postcondition the statement inside the procedure declaration is executed
     * @throws RuntimeException if the break or continue statement is not inside a loop
     */
    @Override
    public int eval(Environment env)
    {
        ProcedureDeclaration procedure = env.getProcedure(id);
        Environment newEnv = new Environment();
        newEnv.setVariable(id, 0); // default return value
        newEnv.setParent(env);
        try
        {
            Variable[] parameters = procedure.getParameters();
            for (int i = 0; i < arguments.length; i++)
            {
                newEnv.setVariable(parameters[i].getName(), arguments[i].eval(env));
            }
            procedure.getStatement().exec(newEnv);
        }
        catch (BreakException e)
        {
            throw new RuntimeException("Break statement not inside loop");
        }
        catch (ContinueException e)
        {
            throw new RuntimeException("Continue statement not inside loop");
        }
        return newEnv.getVariable(id);
    }
}
