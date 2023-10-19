package src.scanner;

/**
 * The token class is responsible for creating a user-friendly Token object that can describe
 *      many different types of tokens. The token class is also responsible for organizing and 
 *      creating helpful user methods such as getType, getValue, hashCode, toString, and equals.
 * @author Daniel Gergov
 * @version 4/21/23
 */
public final class Token
{
    private final Scanner.TOKEN_TYPE type;
    private final String value;

    /**
     * The Token constructor creates a Token object that contains the type and value of the token.
     * @param type the type of the Token referring to the Scanner.TOKEN_TYPE enum information
     * @param value the value of the token
     */
    public Token(Scanner.TOKEN_TYPE type, String value)
    {
        this.type = type;
        this.value = value;
    }

    /**
     * A getter method that returns the type of the token
     * @return Scanner.TOKEN_TYPE the type of the token
     */
    public Scanner.TOKEN_TYPE getType()
    {
        return type;
    }

    /**
     * A getter method that returns the value of the token
     * @return String the value of the token
     */
    public String getValue()
    {
        return value;
    }

    @Override
    /**
     * An overrided toString method that converts the current Token object to a readable
     *      and informative string that contains the type and value of the token:
     *      Ex: WORD: Hello
     * @return String the string representation of the Token object
     */
    public String toString()
    {
        return type + ": " + value;
    }

    @Override
    /**
     * An overrided equals method of the object class that is responsible for comparing two
     *      Token objects. The method returns true if the two Token objects are equal and false
     *      if they are not equal. Additionally, the method throws an IllegalArgumentException
     *      if the given object is not an instance of Token.
     * @param obj type Object, object to be compared to the current Token object
     * @return boolean true if the two Token objects are equal and false if they are not equal
     */
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Token))
        {
            throw new IllegalArgumentException("Given object for comparison is not of type token");
        }
        Token o = (Token) obj;
        return o.getType() == this.type && o.getValue().equals(this.value);
    }

    @Override
    /**
     * An overrided hashCode method that returns a unique hashCode for any Token object.
     *      The method utilizes Java's hashCode method for String objects and adds the hashCode's
     *      together for extra efficacy.
     * @return type int the hash code of the Token object
     */
    public int hashCode()
    {
        return (this.type.name() + this.value).hashCode();
    }
}
