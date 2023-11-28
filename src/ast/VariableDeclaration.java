package src.ast;

import src.emitter.Emitter;
import src.environments.Environment;

public class VariableDeclaration implements Statement
{
    private String[] names;

    public VariableDeclaration(String[] names)
    {
        this.names = names;
    }

    @Override
    public void exec(Environment env)
    {
        for (String name : names)
        {
            env.declareVariable(name, null);
        }
    }

    @Override
    public void compile(Emitter e, Object... args)
    {
        for (String name : names)
        {
            e.emit("var" + name + ": .word 0");
        }
    }
}