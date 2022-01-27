grammar GrammarSecond;
start: e EOF;

e: t eHatch;

eHatch: OR t eHatch | EPS;

t: f tHatch;

tHatch: AND f tHatch | EPS;

f: g fHatch;

fHatch: XOR g fHatch | EPS;

g: LETTER | LBRACKET e RBRACKET | NOT LBRACKET e RBRACKET;

OR: ' or ';
AND: ' and ';
XOR: ' xor ';
NOT: 'not';
LETTER: [a-z];
LBRACKET: '(';
RBRACKET: ')';
