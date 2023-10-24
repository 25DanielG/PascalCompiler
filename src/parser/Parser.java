package src.parser;
import java.util.HashMap;
import java.util.Map;
import src.scanner.ScanErrorException;
import src.scanner.Scanner;
import src.scanner.Token;
import java.util.ArrayList;
import src.ast.*;
import src.ast.Number;

/**
 * The Parser class is responsible for parsing a given input stream and based on grammar rules,
 *      and returning an AST consisting of types of nodes found in the ast package. Each node
 *      is executable or evaluatable with their corresponding exec or eval methods.
 * @author Daniel Gergov
 * @version 10/02/2023
 * program → PROCEDURE id (maybeparms) ; stmt program | stmt .
 * stmt → WRITELN ( expr ) ; | BEGIN stmts END ; | id := expr ; | IF cond THEN stmt
 *      | WHILE cond DO stmt | FOR id := expr TO expr DO stmt | CONTINUE ; | BREAK ;
 *      | IF cond THEN stmt ELSE stmt | EXIT ;
 * maybeparms → parms | ε
 * parms → parms , id | id
 * stmts → stmts stmt | ε
 * expr → expr + term | expr - term | term
 * term → term * factor | term / factor | factor
 * factor → ( expr ) | - factor | num | id | id(maybeargs)
 * maybeargs → args | ε
 * args → args , expr | expr
 * cond → expr relop expr
 * relop → = | <> | < | > | <= | >=
 */
@SuppressWarnings("unused")
public class Parser
{
    private Scanner scanner;
    private Token currentToken;
    private Map<String, Variable> symtab;
    /**
     * Constructor for the Parser class that takes in a Scanner object and initializes the
     *      currentToken instance variable to the next token in the input stream.
     * @param scanner the Scanner object that is used to scan the input stream
     * @throws ScanErrorException if the Scanner object throws a ScanErrorException
     */
    public Parser(Scanner scanner) throws ScanErrorException
    {
        this.scanner = scanner;
        currentToken = this.scanner.nextToken();
        symtab = new HashMap<String, Variable>();
    }
    /**
     * The eat method is responsible for eating the current token and checking if the current
     *      token is equal to the expected token. If the current token is equal to the expected
     *      token, the currentToken instance variable is updated to the next token. If the current
     *      token is not equal to the expected token, an IllegalArgumentException is thrown.
     * @param expected the expect token to eat
     * @precondition the current token is not null
     * @postcondition the current token is updated to the next token
     * @throws IllegalArgumentException if the current token is not equal to the expected token
     */
    private void eat(Token expected)
    {
        if(expected.equals(currentToken))
        {
            try
            {
                currentToken = scanner.nextToken();
            }
            catch(ScanErrorException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Error found on line " + scanner.getLineno() + " and column " + scanner.getColno());
            throw new IllegalArgumentException("Expected " + expected + " but found " + currentToken);
        }
    }
    /**
     * The parseNumber method is responsible for parsing the next token which is required
     *      to be a number token. The method returns the parsed number and eats the token
     *      of type NUMBER.
     * @return the Number AST node containing value of the parsed number token
     * @precondition the current token is not null and of type NUMBER
     * @postcondition the current NUMBER token is eaten and the Number AST node value is returned
     */
    private Number parseNumber() {
        int number = Integer.parseInt(currentToken.getValue());
        eat(new Token(Scanner.TOKEN_TYPE.NUMBER, currentToken.getValue()));
        return new Number(number);
    }
    /**
     * 
     * @return
     */
    public Program parseProgram()
    {
        ArrayList<ProcedureDeclaration> procedures = new ArrayList<ProcedureDeclaration>();
        while(currentToken.getValue().equals("PROCEDURE"))
        {
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "PROCEDURE"));
            String id = currentToken.getValue();
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, currentToken.getValue()));
            eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "("));
            ArrayList<src.ast.Variable> parameters = new ArrayList<src.ast.Variable>();
            if(!currentToken.getValue().equals(")"))
            {
                String param = currentToken.getValue();
                eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, currentToken.getValue()));
                parameters.add(new src.ast.Variable(param));
                while(currentToken.getValue().equals(","))
                {
                    eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ","));
                    param = currentToken.getValue();
                    eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, currentToken.getValue()));
                    parameters.add(new src.ast.Variable(param));
                }
            }
            eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ")"));
            eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ";"));
            Statement statement = parseStatement(false);
            procedures.add(new ProcedureDeclaration(id, parameters.toArray(new src.ast.Variable[parameters.size()]), statement));
        }
        Statement statement = parseStatement(false);
        return new Program(procedures.toArray(new ProcedureDeclaration[procedures.size()]), statement);
    }
    /**
     * The parseStatement method is responsible for parsing a statement which is defined
     *      as WRITELN(NUMBER), an assignment, a loop, or if, or a block. The method eats
     *      the tokens of type IDENTIFIER, OPERATOR, but also calls parseNumber to parse
     *      the number token. The method returns the Statement AST node corresponding
     *      to the parsed statement.
     * @precondition the current token is not null and of type represents a possible Statement
     *      in the grammar
     * @postcondition the current token is updated to the token after the statement
     * @return type Statement the statement AST node corresponding to the parsed statement
     * @param ignoreSemi type boolean wheter to ignore the semicolon or not, used for for loops
     */
    public Statement parseStatement(boolean ignoreSemi)
    {
        if(currentToken.getValue().equals("WRITELN"))
        {
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "WRITELN"));
            eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "("));
            Expression exp = parseExpression();
            eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ")"));
            if(!ignoreSemi)
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ";"));
            }
            return new Writeln(exp);
        }
        else if(currentToken.getValue().equals("BEGIN"))
        {
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "BEGIN"));
            ArrayList<Statement> list = new ArrayList<Statement>();
            while(!currentToken.equals(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "END")))
            {
                list.add(parseStatement(false));
            }
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "END"));
            if(!ignoreSemi)
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ";"));
            }
            return new Block(list.toArray(new Statement[list.size()]));
        }
        else if(currentToken.getValue().equals("IF"))
        {
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "IF"));
            Condition cond = parseCondition();
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "THEN"));
            Statement statement = parseStatement(false);
            if(currentToken.getValue().equals("ELSE"))
            {
                eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "ELSE"));
                Statement elseStatement = parseStatement(false);
                return new IfElse(cond, statement, elseStatement);
            }
            else
            {
                return new If(cond, statement);
            }
        }
        else if(currentToken.getValue().equals("WHILE"))
        {
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "WHILE"));
            Condition cond = parseCondition();
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "DO"));
            Statement statement = parseStatement(false);
            return new While(cond, statement);
        }
        else if(currentToken.getValue().equals("FOR"))
        {
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "FOR"));
            Statement begin = parseStatement(true);
            if(!(begin instanceof Assignment))
            {
                throw new IllegalArgumentException("Expected assignment in for loop heading.");
            }
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "TO"));
            Expression end = parseExpression();
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "DO"));
            Statement statement = parseStatement(false);
            return new For(begin, end, statement);
        }
        else if(currentToken.getValue().equals("CONTINUE"))
        {
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "CONTINUE"));
            if(!ignoreSemi)
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ";"));
            }
            return new Continue();
        }
        else if(currentToken.getValue().equals("BREAK"))
        {
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "BREAK"));
            if(!ignoreSemi)
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ";"));
            }
            return new Break();
        }
        else if(currentToken.getValue().equals("EXIT"))
        {
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, "EXIT"));
            if(!ignoreSemi)
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ";"));
            }
            return new Exit();
        }
        else if(currentToken.getType() == Scanner.TOKEN_TYPE.IDENTIFIER)
        {
            String id = currentToken.getValue();
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, currentToken.getValue()));
            if(currentToken.getValue().equals(":="))
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ":="));
                Expression exp = parseExpression();
                Assignment assign = new Assignment(id, exp);
                if(!ignoreSemi)
                {
                    eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ";"));
                }
                return assign;
            }
            else if(currentToken.getValue().equals("("))
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "("));
                ArrayList<src.ast.Expression> arguments = new ArrayList<src.ast.Expression>();
                if(!currentToken.getValue().equals(")"))
                {
                    arguments.add(parseExpression());
                    while(currentToken.getValue().equals(","))
                    {
                        eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ","));
                        arguments.add(parseExpression());
                    }
                }
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ")"));
                if(!ignoreSemi)
                {
                    eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ";"));
                }
                return new src.ast.ProcedureCall(id, arguments.toArray(new src.ast.Expression[arguments.size()]));
            }
        }
        throw new IllegalArgumentException("Unexpected Statement, got: " + currentToken.getValue());
    }
    /**
     * The parseCondition method is responsible for parsing a condition which is defined as
     *      condition -> expression operator expression. The method returns the parsed condition
     *      as a Condition AST node.
     * @return type Condition the AST Condition node representing the condition
     * @precondition the current token is not null and is able to be parsed as an expression
     * @postcondition the current token is updated to the token after the condition
     */
    private Condition parseCondition()
    {
        Expression left = parseExpression();
        String op;
        if(currentToken.getType() == Scanner.TOKEN_TYPE.OPERATOR)
        {
            op = currentToken.getValue();
            eat(currentToken);
        }
        else
        {
            throw new IllegalArgumentException("Expecting operator in condition");
        }
        Expression right = parseExpression();
        return new Condition(left, right, op);
    }
    /**
     * The parseFactor method is responsible for parsing a factor which is defined as
     *      factor -> num | ( expr ) | - factor | id | id(). The method returns the parsed factor
     *     as an Expression AST node object.
     * @return an Expression AST ndoe representing the parsed factor
     * @precondition the current token is not null and of type NUMBER or OPERATOR
     * @postcondition the current token is updated to the token after the factor
     */
    private Expression parseFactor()
    {
        if(currentToken.getType() == Scanner.TOKEN_TYPE.OPERATOR)
        {
            if(currentToken.getValue().equals("("))
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "("));
                Expression factor = parseExpression();
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ")"));
                return factor;
            }
            else if(currentToken.getValue().equals("-"))
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "-"));
                return new BinOp(new Number(-1), parseFactor(), "*");
            }
        }
        else if(currentToken.getType() == Scanner.TOKEN_TYPE.NUMBER)
        {
            return parseNumber();
        }
        else if(currentToken.getType() == Scanner.TOKEN_TYPE.IDENTIFIER)
        {
            String id = currentToken.getValue();
            eat(new Token(Scanner.TOKEN_TYPE.IDENTIFIER, id));
            if(currentToken.getValue().equals("("))
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "("));
                ArrayList<src.ast.Expression> arguments = new ArrayList<src.ast.Expression>();
                if(!currentToken.getValue().equals(")"))
                {
                    arguments.add(parseExpression());
                    while(currentToken.getValue().equals(","))
                    {
                        eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ","));
                        arguments.add(parseExpression());
                    }
                }
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, ")"));
                return new src.ast.ProcedureCall(id, arguments.toArray(new src.ast.Expression[arguments.size()]));
            }
            return new src.ast.Variable(id);
        }
        throw new IllegalArgumentException("Expected factor but found " + currentToken);
    }
    /**
     * The parseTerm method parses terms that contain factors and operators. The method
     *      parses a factor and stores the value and then parses either a multiplication
     *      , division, or modulo operator, and then parses another operator.
     * @precondition the current token is not null and is able to be parsed as a factor
     * @postcondition parses the return and advances the current token to after the term
     * @return an Expression AST node representing the value of the parsed term / the value
     *      of the operation
     */
    private Expression parseTerm()
    {
        Expression left = parseFactor();
        Expression exp = null;
        while(currentToken.getType() == Scanner.TOKEN_TYPE.OPERATOR 
            && (currentToken.getValue().equals("*") 
            || currentToken.getValue().equals("/") 
            || (currentToken.getValue().equals("mod"))))
        {
            if(currentToken.getValue().equals("*"))
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "*"));
                Expression right = parseFactor();
                exp = new BinOp(left, right, "*");
            }
            else if(currentToken.getValue().equals("/"))
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "/"));
                Expression right = parseFactor();
                exp = new BinOp(left, right, "/");
            }
            else if(currentToken.getValue().equals("mod"))
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "mod"));
                Expression right = parseFactor();
                exp = new BinOp(left, right, "mod");
            }
        }
        return exp == null ? left : exp;
    }
    /**
     * The parseExpression method parses expressions that contain terms and operators. The method
     *      first parses a term and then constantly parses operator tokens that are either a +
     *      or - and then parses another term. This parse expression method is meant to ensure
     *      operator precedence (*, /, mod are evaluated before +, -)
     * @return an Expression AST node representing the value of the expression
     * @precondition the current token is not null and is able to be parsed as a term
     * @postcondition the current token is updated to the token after the expression
     */
    private Expression parseExpression()
    {
        Expression exp = null;
        Expression left = parseTerm();
        while(currentToken.getType() == Scanner.TOKEN_TYPE.OPERATOR 
            && (currentToken.getValue().equals("+") || currentToken.getValue().equals("-")))
        {
            if(currentToken.getValue().equals("+"))
            {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "+"));
                Expression right = parseTerm();
                exp = new BinOp(left, right, "+");
            }
            else if(currentToken.getValue().equals("-")) {
                eat(new Token(Scanner.TOKEN_TYPE.OPERATOR, "-"));
                Expression right = parseTerm();
                exp = new BinOp(left, right, "-");
            }
        }
        return exp == null ? left : exp;
    }
}