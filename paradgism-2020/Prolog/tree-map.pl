checkTree(tree(TreeKey, _, _, _, _), TreeKey) :- !.
checkTree(tree(TreeKey, _, _, _, Right), Key) :-
	Key > TreeKey,
	checkTree(Right, Key), !.
checkTree(tree(TreeKey, _, _, Left, _), Key) :-
	Key < TreeKey,
	checkTree(Left, Key), !.

map_put(nil, Key, Value, tree(Key, Value, Priority, nil, nil)) :- rand_int(1000, Priority).
map_put(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, Value, ResultTree) :-
	\+ checkTree(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key),
	add(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, Value, ResultTree), !.
map_put(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, Value, ResultTree) :-
	checkTree(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key),
	map_remove(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, NewTree),
	add(NewTree, Key, Value, ResultTree), !.

add(nil, Key, Value, tree(Key, Value, Priority, nil, nil)) :- rand_int(1000, Priority), !.
add(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, Value, ResultTree) :-
	split(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, NewTreeA, NewTreeB),
	rand_int(1000, NewPriority),
	merge(NewTreeA, tree(Key, Value, NewPriority, nil, nil), NewTree),
	merge(NewTree, NewTreeB, ResultTree).

split(nil, _, nil, nil) :- !.
split(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, tree(TreeKey, TreeValue, TreePriority, Left, NewTree), ResultTreeB) :-
	Key > TreeKey,
	split(Right, Key, NewTree, ResultTreeB), !.
split(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, ResultTreeA, tree(TreeKey, TreeValue, TreePriority, NewTree, Right)) :-
	Key =< TreeKey,
	split(Left, Key, ResultTreeA, NewTree), !.

merge(nil, nil, nil) :- !.
merge(nil, Tree, Tree) :- Tree \= nil, !.
merge(Tree, nil, Tree) :- Tree \= nil, !.
merge(tree(TreeKeyA, TreeValueA, TreePriorityA, LeftA, RightA), tree(TreeKeyB, TreeValueB, TreePriorityB, LeftB, RightB), tree(TreeKeyA, TreeValueA, TreePriorityA, LeftA, NewTree)) :-
	TreePriorityA > TreePriorityB,
	merge(RightA, tree(TreeKeyB, TreeValueB, TreePriorityB, LeftB, RightB), NewTree), !.
merge(tree(TreeKeyA, TreeValueA, TreePriorityA, LeftA, RightA), tree(TreeKeyB, TreeValueB, TreePriorityB, LeftB, RightB), tree(TreeKeyB, TreeValueB, TreePriorityB, NewTree, RightB)) :-
	TreePriorityA =< TreePriorityB, 
	merge(tree(TreeKeyA, TreeValueA, TreePriorityA, LeftA, RightA), LeftB, NewTree), !.

map_remove(nil, _, nil) :- !.
map_remove(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, ResultTree) :-
	split(tree(TreeKey, TreeValue, TreePriority, Left, Right), Key, NewTreeA, NewTreeB),
	NewKey is Key + 1,
	split(tree(TreeKey, TreeValue, TreePriority, Left, Right), NewKey, NewTreeC, NewTreeD),
	merge(NewTreeA, NewTreeD, ResultTree).

map_get(tree(TreeKey, TreeValue, _, _, _), TreeKey, TreeValue).
map_get(tree(TreeKey, _, _, _, Right), Key, Value) :-
	Key > TreeKey,
	map_get(Right, Key, Value).
map_get(tree(TreeKey, _, _, Left, _), Key, Value) :-
	Key < TreeKey,
	map_get(Left, Key, Value).

map_build([], nil).
map_build([(Key, Value) | Tail], ResultTree) :-
	map_build(Tail, NewTree),
	map_put(NewTree, Key, Value, ResultTree).

map_minKey(tree(TreeKey, _, _, nil, Right), TreeKey).
map_minKey(tree(_, _, _, Left, Right), Key) :-
	Left \= nil,
	map_minKey(Left, Key).

map_maxKey(tree(TreeKey, _, _, Left, nil) ,TreeKey).
map_maxKey(tree(_, _, _, Left, Right), Key) :-
	Right \= nil,
	map_maxKey(Right, Key).