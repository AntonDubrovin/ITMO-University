composite(1) :- !.
composite(N) :- compositeTable(N), !.
composite(N) :- N > 2, \+ prime(N).

prime(2) :- !.
prime(3) :- !.
prime(N) :- primesTable(N), !.
prime(N) :- N > 3, 0 < mod(N, 2), 0 < mod(N, 3), isPrime(N, 5), isPrime(N, 7), assert(primesTable(N)).


isPrime(N, C) :-
	C > sqrt(N), !.

isPrime(N, C) :-
	0 < mod(N, C),
	C1 is C + 6,
	isPrime(N, C1).


masMulti([H], R) :- R is H.
masMulti([H | T], R) :-
	masMulti(T, R1),
	R is R1 * H.

prime_divisors(N, [H | T]) :- number(H), getN_isPrimeDivisors(N, [H | T]), !.
getN_isPrimeDivisors(1, []).
getN_isPrimeDivisors(N, [H]) :- N is H, !.
getN_isPrimeDivisors(N, [H | T]) :- 
	masMulti([H | T], R), 
	N is R, 
	checkDivisors([H | T], R1), 
	R1 is 1.

checkDivisors([H], 1).
checkDivisors([H1, H2 | T], R) :-
	H1 =< H2,
	checkDivisors([H2 | T], R), !.
	
checkDivisors([H1, H2 | T], R) :-
	H1 > H2,
	R is 0, !.


prime_divisors(1, []) :- !.
prime_divisors(N, [H | T]) :- \+ number(H), number(N), getPrimeDivisors(N, [H | T], 2).

getPrimeDivisors(N, [N], C) :- C > sqrt(N).
getPrimeDivisors(N, [H | T], C) :-
	C =< sqrt(N),
	0 < mod(N, C),
	C1 is C + 1,
	getPrimeDivisors(N, [H | T], C1), !.
getPrimeDivisors(N, [H | T], C) :-
	C =< sqrt(N),
	0 is mod(N, C),
	H is C,
	N1 is div(N, C),
	getPrimeDivisors(N1, T, C).


nth_prime(N, P) :-
 getNthPrime(N, 2, P).

getNthPrime(0, CPRIME, R) :- R is CPRIME - 1, !.
getNthPrime(N, CPRIME, R) :-
    prime(CPRIME),
    N1 is N - 1,
    NPRIME is CPRIME + 1,
    getNthPrime(N1, NPRIME, R), !.
getNthPrime(N, CPRIME, R) :-
    composite(CPRIME),
    NPRIME is CPRIME + 1,
    getNthPrime(N, NPRIME, R), !.