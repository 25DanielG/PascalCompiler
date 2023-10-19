package src.parser;

/**
 * The Variable class is responsible for storing a variable's type and value. The type is
 *      stored as a String and the value is stored as an Object. The value can be casted
 *      to the appropriate type when needed. This class is used in the Parser class to
 *      store variables in the symbol table. In the future, this class can store scope as well.
 * @author Daniel Gergov
 * @version 10/03/2023
 */
public class Variable
{
    private String type;
    private Object value;

    /**
     * Constructor for the Variable class that takes in a type and value.
     * @param type the type of the variable as a String
     * @param value the value of the variable as an Object
     */
    public Variable(String type, Object value)
    {
        this.type = type;
        this.value = value;
    }

    /**
     * The getType method returns the type of the variable as a String.
     * @return type String the type of the variable
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * The getValue method returns the value of the variable as an Object.
     * @return type Object the value of the variable
     */
    public Object getValue()
    {
        return this.value;
    }
}
