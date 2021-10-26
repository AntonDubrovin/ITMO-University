Homework #1
Task 1
Create a module named HW1.T1 and define the following data type in it:

data Day = Monday | Tuesday | ... | Sunday
(Obviously, fill in the ... with the rest of the week days).

Do not derive Enum for Day, as the derived toEnum is partial:

ghci> toEnum 42 :: Day
*** Exception: toEnum{Day}: tag (42) is outside of enumeration's range (0,6)
Implement the following functions:

-- | Returns the day that follows the day of the week given as input.
nextDay :: Day -> Day

-- | Returns the day of the week after a given number of days has passed.
afterDays :: Natural -> Day -> Day

-- | Checks if the day is on the weekend.
isWeekend :: Day -> Bool

-- | Computes the number of days until the next Friday.
daysToParty :: Day -> Natural
Task 2
Create a module named HW1.T2 and define the following data type in it:

data N = Z | S N
Implement the following operations:

nplus :: N -> N -> N        -- addition
nmult :: N -> N -> N        -- multiplication
nsub :: N -> N -> Maybe N   -- subtraction     (Nothing if result is negative)
ncmp :: N -> N -> Ordering  -- comparison      (Do not derive Ord)

nFromNatural :: Natural -> N
nToNum :: Num a => N -> a
(Advanced) Implement the following operations:

nEven, nOdd :: N -> Bool    -- parity checking
ndiv :: N -> N -> N         -- integer division
nmod :: N -> N -> N         -- modulo operation
Task 3
Create a module named HW1.T3 and define the following data type in it:

data Tree a = Leaf | Branch Int (Tree a) a (Tree a)
Functions operating on this tree must maintain the following invariants:

Sorted: The elements in the left subtree are less than the head element of a branch, and the elements in the right subtree are greater.
Unique: There are no duplicate elements in the tree (follows from Sorted).
CachedSize: The Int field stores the size of the tree.
(Advanced) Balanced: For any given Branch _ l _ r, the ratio between the size of l and the size of r never exceeds 3.
These invariants enable efficient processing of the tree.

Implement the following functions:

-- | Size of the tree, O(1).
tsize :: Tree a -> Int

-- | Depth of the tree.
tdepth :: Tree a -> Int

-- | Check if the element is in the tree, O(log n)
tmember :: Ord a => a -> Tree a -> Bool

-- | Insert an element into the tree, O(log n)
tinsert :: Ord a => a -> Tree a -> Tree a

-- | Build a tree from a list, O(n log n)
tFromList :: Ord a => [a] -> Tree a
Tip 1: in order to maintain the CachedSize invariant, define a helper function:

mkBranch :: Tree a -> a -> Tree a -> Tree a
Tip 2: the Balanced invariant is the hardest to maintain, so implement it last. Search for “tree rotation”.

Task 4
Create a module named HW1.T4.

Using the Tree data type from HW1.T3, define the following function:

tfoldr :: (a -> b -> b) -> b -> Tree a -> b
It must collect the elements in order:

treeToList :: Tree a -> [a]    -- output list is sorted
treeToList = tfoldr (:) []
This follows from the Sorted invariant.

You are encouraged to define tfoldr in an efficient manner, doing only a single pass over the tree and without constructing intermediate lists.

Task 5
Create a module named HW1.T5.

Implement the following function:

splitOn :: Eq a => a -> [a] -> NonEmpty [a]
Conceptually, it splits a list into sublists by a separator:

ghci> splitOn '/' "path/to/file"
["path", "to", "file"]

ghci> splitOn '/' "path/with/trailing/slash/"
["path", "with", "trailing", "slash", ""]
Due to the use of NonEmpty to enforce that there is at least one sublist in the output, the actual GHCi result will look slightly differently:

ghci> splitOn '/' "path/to/file"
"path" :| ["to","file"]
Do not let that confuse you. The first element is not in any way special.

Implement the following function:

joinWith :: a -> NonEmpty [a] -> [a]
It must be the inverse of splitOn, so that:

(joinWith sep . splitOn sep)  ≡  id
Example usage:

ghci> "import " ++ joinWith '.' ("Data" :| "List" : "NonEmpty" : [])
"import Data.List.NonEmpty"
Task 6
Create a module named HW1.T6.

Using Foldable methods, implement the following function:

mcat :: Monoid a => [Maybe a] -> a
Example usage:

ghci> mcat [Just "mo", Nothing, Nothing, Just "no", Just "id"]
"monoid"

ghci> Data.Monoid.getSum $ mcat [Nothing, Just 2, Nothing, Just 40]
42
Using Foldable methods, implement the following function:

epart :: (Monoid a, Monoid b) => [Either a b] -> (a, b)
Example usage:

ghci> epart [Left (Sum 3), Right [1,2,3], Left (Sum 5), Right [4,5]]
(Sum {getSum = 8},[1,2,3,4,5])
Task 7
Create a module named HW1.T7.

Define the following data type and a lawful Semigroup instance for it:

data ListPlus a = a :+ ListPlus a | Last a
infixr 5 :+
Define the following data type and a lawful Semigroup instance for it:

data Inclusive a b = This a | That b | Both a b
The instance must not discard any values:

This i  <>  This j  =  This (i <> j)   -- OK
This i  <>  This _  =  This i          -- This is not the Semigroup you're looking for.
Define the following data type:

newtype DotString = DS String
Implement a Semigroup instance for it, such that the strings are concatenated with a dot:

ghci> DS "person" <> DS "address" <> DS "city"
DS "person.address.city"
Implement a Monoid instance for it. Make sure that the laws hold:

mempty <> a  ≡  a
a <> mempty  ≡  a
Define the following data type:

newtype Fun a = F (a -> a)
Implement lawful Semigroup and Monoid instances for it.
