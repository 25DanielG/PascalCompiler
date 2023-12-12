package src.emitter;

import java.beans.Expression;
import java.io.*;
import java.util.HashSet;
import java.util.Set;
import src.ast.ProcedureDeclaration;
import src.ast.Variable;
import src.ast.VariableDeclaration;

/**
 * The Emitter class represents an emitter for writing to a new file with given name and keeping
 *      track of all the variables in a program. The class provides methods to emit MIPS assembly
 *      to an output file.
 * @author Daniel Gergov
 * @version 12/3/23
 */
public class Emitter
{
	private PrintWriter out;
    private String code;
    private int loopID;
    private int ifID;
    public ProcedureDeclaration context;
    public int extraStack;

	/**
     * Creates an Emitter for writing to a new file with given name. The method creates a
     *      PrintWriter object to write to the file.
     * @param outputFileName the name of the file to write to
     * @precondition outputFileName is not null
     */
	public Emitter(String outputFileName)
	{
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
        code = "";
        loopID = 1;
        ifID = 1;
        extraStack = 0;
	}

	/**
     * Adds one line of code to the entire code string variable. Lines the start with a dot or
     *      that are labels are not indented; everything else is indented.
     * @param code the line of code to add to the code string
     * @precondition code is not null
     */
	public void emit(String code)
	{
		if (!code.endsWith(":") && !code.startsWith("."))
			code = "\t" + code;
        code += "\n";
		this.code += code;
	}

    /**
     * Prepends one line of code to the entire code string variable. Lines the start with a dot or
     *      that are labels are not indented; everything else is indented.
     * @param code the line of code to add to the code string
     * @precondition code is not null
     */
    public void prepend(String code)
    {
        if (!code.endsWith(":") && !code.startsWith("."))
			code = "\t" + code;
        code += "\n";
        this.code = code + this.code;
    }

    /**
     * Pushes the entire code string to the output file.
     * @precondition the code string is not null
     */
    public void push()
    {
        out.println(this.code);
    }

	/**
     * Closes the file.
     */
	public void close()
	{
		out.close();
	}

    /**
     * Emits the MIPS code to the emitter to push the given register onto the stack.
     *      The method also decrements the stack pointer to allow for more space on the stack.
     * @param reg the register to push onto the stack
     */
    public void emitPush(String reg)
    {
        this.incrementExtraStack(false);
        this.emit("addi $sp, $sp, -4");
        this.emit("sw " + reg + ", ($sp)");
    }

    /**
     * Emits the MIPS code to the emitter to pop the given register off the stack.
     *      The method also increments the stack pointer to allow for more space on the stack.
     * @param reg the register to pop off the stack
     */
    public void emitPop(String reg)
    {
        this.incrementExtraStack(true);
        this.emit("lw " + reg + ", ($sp)");
        this.emit("addu $sp, $sp, 4");
    }

    /**
     * Returns the next loop ID to ensure that each loop has a unique ID.
     * @return type int the next loop id
     */
    public int nextLoopID()
    {
        return loopID++;
    }

    /**
     * Returns the next if ID to ensure that each if has a unique ID.
     * @return type int the next if id
     */
    public int nextIfID()
    {
        return ifID++;
    
    }

    /**
     * Sets the procedure context of the emitter to the given procedure declaration.
     *      This method is needed in order to compile procedure calls and access
     *      local variables which should be distinguised from global variables.
     * @param proc type ProcedureDeclaration the procedure declaration to set the context to
     */
    public void setProcedureContext(ProcedureDeclaration proc)
    {
        this.extraStack = 0;
        this.context = proc;
    }

    /**
     * Sets the procedure context of the emitter to null. This method is needed
     *      when the code is not inside a procedure declaration and instead should
     *      be accessing global variables.
     */
    public void clearProcedureContext()
    {
        this.context = null;
    }

    /**
     * Checks if a given variable name is a local variable or global variable. If the variable name
     *      is one of the procedure's parameters, it is assumed to be a local variable. Otherwise,
     *      it is assumed to be a global variable.
     * @return type boolean true if the variable name is a local variable, false otherwise
     * @param name type String the name of the variable
     * @precondition name is not null
     */
    public boolean isLocal(String name)
    {
        if (context == null)
        {
            return false;
        }
        if (this.context.getId().equals(name)) // proc id
        {
            return true;
        }
        for (src.ast.VariableDeclaration v : context.getLocals()) // local variables
        {
            String s = v.getNames()[0];
            if (s.equals(name))
            {
                return true;
            }
        }
        for (src.ast.Variable p : context.getParameters()) // parameters
        {
            if (p.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the offset of the given variable name. The offset is the distance from the top of
     *      the stack to the local variable name given as a parameter, local variable, method
     *      name. The method assumes that the variable name is a local variable and throws an
     *      exception if it is not. The stack is organized from top to bottom as local variables
     *      , method name, and parameters.
     * @param name type String the name of the variable
     * @throws IllegalStateException if the method is called while not in a subroutine
     * @throws IllegalArgumentException if the variable name is not a local variable
     * @precondition name is not null and is a local variable
     * @postcondition the offset is returned
     * @return type int the offset of the variable name
     */
    public int getOffset(String name)
    {
        if (context == null)
        {
            throw new IllegalStateException("Cannot get offset while not in a subroutine.");
        }
        if (!isLocal(name))
        {
            throw new IllegalArgumentException("Variable " + name + " is not a local variable.");
        }
        int excess = extraStack * 4;

        src.ast.VariableDeclaration[] locals = context.getLocals();
        for (int offset = 0; offset < locals.length; ++offset)
        {
            if (locals[offset].getNames()[0].equals(name))
            {
                System.out.println("offset: " + offset + " locals.length: " + locals.length);
                System.out.println("Excess is " + excess);
                System.out.println("returning: " + (excess + (4 * (locals.length - 1 - offset))));
                return excess + (4 * (locals.length - 1 - offset));
            }
        }
        excess += 4 * locals.length;

        if (context.getId().equals(name))
        {
            return excess;
        }
        excess += 4;

        src.ast.Variable[] parameters = context.getParameters();
        for (int i = 0; i < parameters.length; ++i)
        {
            if (parameters[i].getName().equals(name))
            {
                System.out.println("Detecing parameter " + name + " at offset " + (excess + (4 * (parameters.length - 1 - i))));
                return excess + (4 * (parameters.length - 1 - i));
            }
        }
        throw new IllegalArgumentException("Variable " + name + " is not a local variable.");
    }

    /**
     * Increments the extra stack variable in order to keep track of where the procedure's local
     *      variables are stored in the stack as the height changes. If opp is true the method
     *      decrements the stack height, if it is false, the method increments the stack height.
     * @param opp type boolean true if the stack height should be decremented, false otherwise
     * @postcondition the extra stack variable is incremented or decremented
     */
    public void incrementExtraStack(boolean opp)
    {
        if (opp)
        {
            extraStack--;
        }
        else
        {
            extraStack++;
        }
    }
}