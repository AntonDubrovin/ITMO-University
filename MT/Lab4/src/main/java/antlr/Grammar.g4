grammar Grammar;
start: firstLine code EOF;

firstLine: 'grammar ' WORD SEMILOCON NEWLINE;

code: (terminal | nonTerminal)*;

nonTerminal: WORD WS? arguments? WS? returnType? ': ' rules SEMILOCON NEWLINE;

rules: ruleOnly (OR ruleOnly)*;

ruleOnly: (WS? (oneRule WS?)+ WS? returnn? WS?);

oneRule: WORD receivedArguments?;

receivedArguments: LBRACKET (WORD | DOT | OPERATIONS | WS | '$')+ RBRACKET;

arguments: LBRACKET argument (', ' argument)* RBRACKET;

argument:  WORD ': ' RETURNTYPE;

returnType: RETURNS WS? RETURNTYPE;

returnn: FIGURELBRACKET RETURN WS? '$'? (WORD | DOT | LBRACKET | RBRACKET)+ FIGURERBRACKET;

terminal: WORD ': ' WS? (STRING | REGEX | '\'' OPERATIONS '\'') WS? SEMILOCON NEWLINE;

STRING: '\'' (WORD | LBRACKET | RBRACKET | ' ')+ '\'';
OPERATIONS: ('*' | '/' | '+' | '-');
DOT: '.';
LBRACKET: '(';
RBRACKET: ')';

FIGURELBRACKET: '{';
FIGURERBRACKET: '}';

REGEX: '[a-z]' | '[0-9]+';

OR: ' | ';

RETURNTYPE: 'Int' | 'String' | 'Boolean' | 'Double';
RETURNS: 'returns';
RETURN: 'return';

SEMILOCON: ';';
WS: ' '+;
NEWLINE: '\n'+;
WORD: [a-zA-Z0-9_]+;