eval(not(and(Left, Right)), R) :- eval(not(Left), A), eval(not(Right), B), eval(or(A, B), R), !.
eval(not(or(Left, Right)), R) :- eval(not(Left), A), eval(not(Right), B), eval(and(A, B), R), !.

eval(and(Var, not(Var)), 0) :- !.
eval(and(not(Var), Var), 0) :- !.
eval(or(Var, not(Var)), 1) :- !.
eval(or(not(Var), Var), 1) :- !.

eval(and(Left, 1), R) :- eval(Left, R), !.
eval(and(1, Right), R) :- eval(Right, R), !.
eval(and(Left, 0), 0) :- !.
eval(and(0, Right), 0) :- !.

eval(or(Left, 0), R) :- eval(Left, R), !.
eval(or(0, Right), R) :- eval(Right, R), !.
eval(or(Left, 1), 1) :- !.
eval(or(1, Right), 1) :- !.

eval(and(Var, Var), R) :- eval(Var, R), !.
eval(or(Var, Var), R) :- eval(Var, R), !.

% закон поглощения для ИЛИ
eval(or(Left, and(Left, Right)), R) :- eval(Left, R), !.
eval(or(and(Left, Right), Left), R) :- eval(Left, R), !.
eval(or(and(Left, Right), Right), R) :- eval(Right, R), !.
eval(or(Right, and(Left, Right)), R) :- eval(Right, R), !.

% закон поглощения для И
eval(and(Left, or(Left, Right)), R) :- eval(Left, R), !.
eval(and(or(Left, Right), Left), R) :- eval(Left, R), !.
eval(and(Right, or(Left, Right), R)) :- eval(Right, R), !.
eval(and(or(Left, Right), Right), R) :- eval(Right, R), !.

eval(not(not(Expr)), R) :- eval(Expr, R), !.
eval(not(V), not(V)) :- \+ number(V), !.

eval(not(Expr), R) :- eval(Expr, Expr1), eval(not(Expr1), R), !.

eval(and(or(Left, Right), or(Left1, Right1)), R) :-
	eval(Left, EvalLeft),
	eval(Right, EvalRight),
	eval(Left1, EvalLeft1),
	eval(Right1, EvalRight1),
	eval(and(EvalLeft, EvalLeft1), EvalAnd1),
	eval(and(EvalLeft, EvalRight1), EvalAnd2),
	eval(and(EvalRight, EvalLeft1), EvalAnd3),
	eval(and(EvalRight, EvalRight1), EvalAnd4),
	eval(or(EvalAnd1, EvalAnd2), EvalOr1), 
	eval(or(EvalAnd3, EvalAnd4), EvalOr2),
	eval(or(EvalOr1, EvalOr2), R), !.

eval(not(1), 0) :- !.
eval(not(0), 1) :- !.



eval(and(Left, or(LeftOr, RightOr)), R) :-
	eval(Left, EvalLeft),
	eval(LeftOr, EvalLeftOr),
	eval(RightOr, EvalRightOr),
	eval(and(EvalLeft, EvalLeftOr), EvalAndLeft),
	eval(and(EvalLeft, EvalRightOr), EvalAndRight),
	eval(or(EvalAndLeft, EvalAndRight), R), !.
eval(and(or(LeftOr, RightOr), Right), R) :- 
	eval(Right, EvalRight),
	eval(LeftOr, EvalLeftOr),
	eval(RightOr, EvalRightOr), 
	eval(and(EvalLeftOr, EvalRight), EvalAndLeft),
	eval(and(EvalRightOr, EvalRight), EvalAndRight),
	eva(or(EvalAndLeft, EvalAndRight), R), !.

eval(V, V) :- !.

% Метод, преобзразующий лог выраж в ДНФ.
% для теста and(a, or(b, not(c))) надо написать такой запрос: convert(and(a, or(b, not(c))), R)
convert(Expr, R) :-
	eval(Expr, R).