grammar Grammar;
start: code EOF;

code: (condition | line)+;

condition: ifCond ':' NEWLINE conditionBody+ (elseCond NEWLINE conditionBody+)?;

conditionBody: TAB line | TAB condition;

ifCond: IF statement;

elseCond: TAB? ELSE;

statement:
      LBRACKET statement RBRACKET
    | statement COMPARE statement
    | statement BITWISE statement
    | expression;

line: (input | print) NEWLINE;

input: VARIABLE EQUALS expression | VARIABLE EQUAL (NUMBER | INPUT | VARIABLE | expression);

print: PRINT LBRACKET (VARIABLE | NUMBER | expression | ) RBRACKET;

expression:
      LBRACKET expression RBRACKET
    | expression OPERATION expression
    | VARIABLE
    | NUMBER;

TAB: ('    ')+;
IF: 'if ';
ELSE: 'else:';
NEWLINE: '\n';
LBRACKET: '(';
RBRACKET: ')';
COMPARE: ' >= ' | ' <= ' | ' > ' | ' < ' | ' != ' | ' == ';
BITWISE: ' and ' | ' or ' | ' ^ ' | ' & ' | ' | ';
BOOLEAN: 'True ' | ' False ';
EQUAL: ' = ';
EQUALS: ' -= ' | ' += ' | ' *= ' | ' /= ';
INPUT: 'int(input())' | 'float(input())';
PRINT: 'print';
OPERATION: ' + ' | ' - ' | ' * ' | ' / ';
VARIABLE: [a-zA-Z];
NUMBER:
      [1-9][0-9]* ('.' [0-9]*)?
    | '0'
    | '0.' [1-9]*;