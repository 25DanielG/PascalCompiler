package src.emitter;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Emitter
{
	private PrintWriter out;
    private String code;
    private int loopID;
    private int ifID;
    private Set<String> variables;

	/**
     * creates an emitter for writing to a new file with given name
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
     * prints one line of code to file (with non-labels indented)
     */
	public void emit(String code)
	{
		if (!code.endsWith(":") && !code.startsWith("."))
			code = "\t" + code;
        code += "\n";
		this.code += code;
	}

    public void prepend(String code)
    {
        if (!code.endsWith(":") && !code.startsWith("."))
			code = "\t" + code;
        code += "\n";
        this.code = code + this.code;
    }

    public void push()
    {
        out.println(this.code);
    }

	/**
     * closes the file.  should be called after all calls to emit.
     */
	public void close()
	{
		out.close();
	}

    public void emitPush(String reg)
    {
        this.emit("subu $sp, $sp, 4");
        this.emit("sw " + reg + ", ($sp)");
    }

    public void emitPop(String reg)
    {
        this.emit("lw " + reg + ", ($sp)");
        this.emit("addu $sp, $sp, 4");
    }

    public int nextLoopID()
    {
        return loopID++;
    }

    public int nextIfID()
    {
        return ifID++;
    
    }

    public void addVariable(String var)
    {
        variables.add(var);
    }

    public Set<String> getVariables()
    {
        return variables;
    }
}