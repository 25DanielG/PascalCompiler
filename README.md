Run with:
javac */*/*.java && java src/parser/ParserTester && rm -rf */*/*.class

Language grammar:
program → VAR ids ; program | VAR ids ; PROCEDURE id (maybeparms) ; stmt program | 
      PROCEDURE id (maybeparms) ; stmt program | stmt .
stmt → VAR ids ; | WRITELN ( expr ) ; | BEGIN stmts END ; | id := expr ; | IF cond THEN stmt
      | WHILE cond DO stmt | FOR id := expr TO expr DO stmt | CONTINUE ; | BREAK ;
      | IF cond THEN stmt ELSE stmt | EXIT ;
ids → ids , id | id
maybeparms → parms | ε
parms → parms , id | id
stmts → stmts stmt | ε
expr → expr + term | expr - term | term
term → term * factor | term / factor | factor
factor → ( expr ) | - factor | num | id | id(maybeargs)
maybeargs → args | ε
args → args , expr | expr
cond → expr relop expr
relop → = | <> | < | > | <= | >=