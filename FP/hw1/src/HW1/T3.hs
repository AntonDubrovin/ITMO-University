module HW1.T3
  ( Tree (..)
  , tdepth
  , tFromList
  , tinsert
  , tmember
  , tsize
  ) where

data Tree a
  = Leaf
  | Branch Int (Tree a) a (Tree a)
  deriving Show

tsize :: Tree a -> Int
tsize Leaf                = 0
tsize (Branch size _ _ _) = size

tdepth :: Tree a -> Int
tdepth Leaf                            = 0
tdepth (Branch _ leftTree _ rightTree) = max (tdepth leftTree) (tdepth rightTree) + 1

tmember :: Ord a => a -> Tree a -> Bool
tmember _ Leaf = False
tmember value (Branch _ leftTree x rightTree)
  | value == x = True
  | value <  x = tmember value leftTree
  | otherwise  = tmember value rightTree

tinsert :: Ord a => a -> Tree a -> Tree a
tinsert value Leaf = Branch 1 Leaf value Leaf
tinsert newValue (Branch _ leftTree value rightTree)
  | newValue > value = mkBranch leftTree value $ tinsert newValue rightTree
  | newValue < value = mkBranch (tinsert newValue leftTree) value rightTree
  | otherwise        = mkBranch leftTree newValue rightTree

tFromList :: Ord a => [a] -> Tree a
tFromList = foldl (flip tinsert) Leaf

_mkBranch :: Tree a -> a -> Tree a -> Tree a
_mkBranch leftTree value rightTree =
  Branch (tsize leftTree + tsize rightTree + 1) leftTree value rightTree

mkBranch :: Tree a -> a -> Tree a -> Tree a
mkBranch Leaf value Leaf = Branch 1 Leaf value Leaf
mkBranch Leaf value rightTree@(Branch _ rlTree _ rrTree) =
  if tdepth rightTree == 2
  then
    if tdepth rlTree <= tdepth rrTree
    then lRotation  $ _mkBranch Leaf value rightTree
    else lrRotation $ _mkBranch Leaf value rightTree
  else _mkBranch Leaf value rightTree
mkBranch leftTree@(Branch _ llTree _ lrTree) value Leaf =
  if tdepth leftTree == 2
  then
    if tdepth lrTree <= tdepth llTree
    then rRotation  $ _mkBranch leftTree value Leaf
    else rlRotation $ _mkBranch leftTree value Leaf
  else _mkBranch leftTree value Leaf
mkBranch
  leftTree @(Branch _ llTree _ lrTree)
  value
  rightTree@(Branch _ rlTree _ rrTree)
  | tdepth leftTree - tdepth rightTree == 2 =
    if tdepth llTree >= tdepth lrTree
    then rRotation  $ _mkBranch leftTree value rightTree
    else rlRotation $ _mkBranch leftTree value rightTree
  | tdepth rightTree - tdepth leftTree == 2 =
    if tdepth rlTree <= tdepth rrTree
    then lRotation  $ _mkBranch leftTree value rightTree
    else lrRotation $ _mkBranch leftTree value rightTree
  | otherwise = _mkBranch leftTree value rightTree

lRotation :: Tree a -> Tree a
lRotation (Branch _ leftTree value (Branch _ rlTree rValue rrTree)) =
  _mkBranch (_mkBranch leftTree value rlTree) rValue rrTree
lRotation a = a

rRotation :: Tree a -> Tree a
rRotation (Branch _ (Branch _ llTree lValue lrTree) value rightTree) =
  _mkBranch llTree lValue $ _mkBranch lrTree value rightTree
rRotation a = a

rlRotation :: Tree a -> Tree a
rlRotation (Branch _ (Branch _ llTree lValue (Branch _ lrlTree lrValue lrrTree)) value rTree) =
  _mkBranch (_mkBranch llTree lValue lrlTree) lrValue $ _mkBranch lrrTree value rTree
rlRotation a = a

lrRotation :: Tree a -> Tree a
lrRotation (Branch _ lTree value (Branch _ (Branch _ rllTree rlValue rlrTree) rValue rrTree)) =
  _mkBranch (_mkBranch lTree value rllTree)   rlValue $ _mkBranch rlrTree rValue rrTree
lrRotation a = a

