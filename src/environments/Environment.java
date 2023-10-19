package src.environments;

import java.util.HashMap;
import java.util.Map;
import src.ast.ProcedureDeclaration;

/**
 * The Environment class represents the environment of the program. It stores the
 *      variables in a hash map and provides methods to modify and access the
 *      variables in the hash map. The environment also stores the loop depth
 *      of the program to handle the break and continue statements.
 * @author Daniel Gergov
 * @version 10/16/23
 */
public class Environment
{
    private Map<String, Integer> envSymtab;
    private Map<String, ProcedureDeclaration> envProceduretab;
    private int loopDepth;
    private Environment parent;

    /**
     * Constructor for the Environment class that creates an environment for the program.
     *      The environment stores the variables/methods in a hash map and provides methods to
     *      modify and access the variables/methods in the hash map. The environment also stores
     *      the loop depth of the program to handle the break and continue statements.
     */
    public Environment()
    {
        envSymtab = new HashMap<String, Integer>();
        envProceduretab = new HashMap<String, ProcedureDeclaration>();
        loopDepth = 0;
        parent = null;
    }

    /**
     * The setParent method sets the parent of the environment to the given environment to add
     *      support for procedures modifying and accessing a copy of variables rather than
     *      modifying and accessing the original global variables, possibly affecting other
     *      statements and methods.
     * @param e type Environment, the environment to set the parent of the current environment
     */
    public void setParent(Environment e)
    {
        this.parent = e;
    }

    /**
     * Associates the given variable name with the given value by storing the identifier
     *       type String and the value type int in the symbol table hash map.
     * @param variable type String, the name of the variable
     * @param value type int, the value of the variable
     * @precondition the variable and value is not null
     * @postcondition the variable is stored in the hash map
     */
    public void setVariable(String variable, int value)
    {
        envSymtab.put(variable, value);
    }

    /**
     * Modifies the loop depth by incrementing or decrementing the loop depth by 1 as specified
     *      by the inc parameter. The loop depth is used to handle the break and continue
     *      statements since break and continue statements can only be used inside loops.
     *      Therefore, if the loop depth is 0, the break and continue statements cannot be used
     *      and if the loop depth is greater than 0, the break and continue statements can be used.
     * @param inc type boolean, true if the loop depth is incremented and false if the loop depth
     *      is decremented
     * @precondition parameter inc is not null
     * @postcondition the loop depth is modified
     */
    public void modifyLoopDepth(boolean inc)
    {
        loopDepth += inc ? 1 : -1;
    }

    /**
     * Returns the loop depth of the program by returning the loopDepth instance
     *      variable.
     * @precondition none
     * @postcondition the loop depth is returned
     * @return type int the loop depth of the program
     */
    public int getLoopDepth()
    {
        return loopDepth;
    }

    /**
     * Returns the value associated with the given variable by looking up the variable
     *      in the hash map and returning the value associated with the variable.
     *      The method recursively looks up the tree of environments until the variable
     *      is found.
     * @param variable type String, the name of the variable to look up
     * @precondition the variable is not null
     * @postcondition the value of the variable is returned
     * @return type int the value of the variable in the hash map symbol table
     */
    public int getVariable(String variable)
    {
        Environment current = this;
        while (current != null)
        {
            if (current.envSymtab.containsKey(variable))
            {
                return current.envSymtab.get(variable);
            }
            current = current.parent;
        }
        throw new RuntimeException("Variable " + variable + " not declared.");
    }

    /**
     * Associates the given ProcedureDeclaration name with the given id by storing the identifier
     *       type String and the ProcedureDeclaration type ProcedureDeclaration in the symbol
     *      table hash map. This association is made in the global environment.
     * @param id the identiter to associate the ProcedureDeclaration with
     * @param procedure the ProcedureDeclaration to associate with the id
     * @precondition the id and procedure is not null
     * @postcondition the id and procedure are associated with each other in the hash map
     */
    public void setProcedure(String id, ProcedureDeclaration procedure)
    {
        Environment current = this;
        while (current.parent != null)
        {
            current = current.parent;
        }
        current.envProceduretab.put(id, procedure);
    }

    /**
     * Returns the ProcedureDeclaration associated with the given id by looking up the id
     *      in the procedure symbol table hash map and returning the ProcedureDeclaration
     *      associated with the id.
     * @param id type String, the name of the procedure to look up
     * @return type ProcedureDeclaration the ProcedureDeclaration associated with the id
     * @precondition the id is not null
     * @postcondition the ProcedureDeclaration associated with the id is returned
     */
    public ProcedureDeclaration getProcedure(String id)
    {
        Environment current = this;
        while (current.parent != null)
        {
            current = current.parent;
        }
        return current.envProceduretab.get(id);
    }
}