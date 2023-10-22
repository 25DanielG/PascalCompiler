package src.ast;

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
public class ProcedureCall implements Expression, Statement
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
        newEnv.declareVariable(id, null); // default return value
        newEnv.setParent(env);
        try
        {
            Variable[] parameters = procedure.getParameters();
            for (int i = 0; i < arguments.length; i++)
            {
                newEnv.declareVariable(parameters[i].getName(), arguments[i].eval(env));
            }
            procedure.getStatement().exec(newEnv);
        }
        catch (ParseErrorException e)
        {
            if (e instanceof ContinueException || e instanceof BreakException)
            {
                throw new RuntimeException("Break/Continue statement not inside loop");
            }
        }
        Integer returnValue = newEnv.getVariable(id);
        if (returnValue == null)
        {
            throw new RuntimeException("Cannot evaluate expression with void return value");
        }
        return newEnv.getVariable(id);
    }

    /**
     * 
     */
    @Override
    public void exec(Environment env)
    {
        ProcedureDeclaration procedure = env.getProcedure(id);
        Environment newEnv = new Environment();
        newEnv.setParent(env);
        try
        {
            Variable[] parameters = procedure.getParameters();
            for (int i = 0; i < arguments.length; i++)
            {
                newEnv.declareVariable(parameters[i].getName(), arguments[i].eval(env));
            }
            procedure.getStatement().exec(newEnv);
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
