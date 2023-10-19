package src.environments;

import java.util.HashMap;
import java.util.Map;

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
    private int loopDepth;

    public Environment()
    {
        envSymtab = new HashMap<String, Integer>();
        loopDepth = 0;
    }

    /**
     * Associates the given variable name with the given value by storing the identifie
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
     * @param variable type String, the name of the variable to look up
     * @precondition the variable is not null
     * @postcondition the value of the variable is returned
     * @return type int the value of the variable in the hash map symbol table
     */
    public int getVariable(String variable)
    {
        return envSymtab.get(variable);
    }
}