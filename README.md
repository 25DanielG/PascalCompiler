Run with:
```javac */*/*.java && java src/parser/ParserTester && rm -rf */*/*.class```

Language grammar:
program → VAR ids ; program | VAR ids ; PROCEDURE id (maybeparms) ; stmt program | 
      PROCEDURE id (maybeparms) ; stmt program | stmt .
<br>
stmt → VAR ids ; | WRITELN ( expr ) ; | BEGIN stmts END ; | id := expr ; | IF cond THEN stmt
      | WHILE cond DO stmt | FOR id := expr TO expr DO stmt | CONTINUE ; | BREAK ;
      | IF cond THEN stmt ELSE stmt | EXIT ;
<br>
ids → ids , id | id
<br>
maybeparms → parms | ε
<br>
parms → parms , id | id
<br>
stmts → stmts stmt | ε
<br>
expr → expr + term | expr - term | term
<br>
term → term * factor | term / factor | factor
<br>
factor → ( expr ) | - factor | num | id | id(maybeargs)
<br>
maybeargs → args | ε
<br>
args → args , expr | expr
<br>
cond → expr relop expr
<br>
relop → = | <> | < | > | <= | >=