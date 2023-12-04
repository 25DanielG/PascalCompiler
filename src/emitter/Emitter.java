package src.emitter;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

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
    private Set<String> variables;

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
        variables = new HashSet<String>();
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
        this.emit("subu $sp, $sp, 4");
        this.emit("sw " + reg + ", ($sp)");
    }

    /**
     * Emits the MIPS code to the emitter to pop the given register off the stack.
     *      The method also increments the stack pointer to allow for more space on the stack.
     * @param reg the register to pop off the stack
     */
    public void emitPop(String reg)
    {
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
     * Adds a variable to the variable set to keep track of all the variables in a program.
     *      This method creates support for using variables without needing to declare them.
     * @precondition var is not null
     * @postcondition the variable is added to the variable set
     */
    public void addVariable(String var)
    {
        variables.add(var);
    }

    /**
     * Returns the set of variables in the program.
     * @return type Set<String> the set of variables in the program
     */
    public Set<String> getVariables()
    {
        return variables;
    }
}