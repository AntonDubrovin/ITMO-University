module HW2.T1
  ( Annotated (..)
  , Except (..)
  , Fun (..)
  , List (..)
  , Option (..)
  , Pair (P)
  , Prioritised (..)
  , Quad (Q)
  , Stream (..)
  , Tree (..)
  , mapAnnotated
  , mapExcept
  , mapFun
  , mapList
  , mapOption
  , mapPair
  , mapPrioritised
  , mapQuad
  , mapStream
  , mapTree
  ) where

data Option a
  = None
  | Some a

mapOption :: (a -> b) -> Option a -> Option b
mapOption _        None           = None
mapOption function (Some element) = Some $ function element

data Pair a
  = P a a

mapPair :: (a -> b) -> Pair a -> Pair b
mapPair function (P first second)
  = P (function first) $ function second

data Quad a
  = Q a a a a

mapQuad :: (a -> b) -> Quad a -> Quad b
mapQuad function (Q first second third fourth)
  = Q (function first) (function second) (function third) $ function fourth

data Annotated e a
  = a :# e
infix 0 :#

mapAnnotated :: (a -> b) -> Annotated e a -> Annotated e b
mapAnnotated function (h :# t)
  = function h :# t

data Except e a
  = Error e
  | Success a

mapExcept :: (a -> b) -> Except e a -> Except e b
mapExcept _        (Error   err    ) = Error err
mapExcept function (Success element) = Success $ function element

data Prioritised a
  = Low a
  | Medium a
  | High a

mapPrioritised :: (a -> b) -> Prioritised a -> Prioritised b
mapPrioritised function (Low    element) = Low    $ function element
mapPrioritised function (Medium element) = Medium $ function element
mapPrioritised function (High   element) = High   $ function element

data Stream a
  = a :> Stream a
infixr 5 :>

mapStream :: (a -> b) -> Stream a -> Stream b
mapStream function (h :> t)
  = function h :> mapStream function t

data List a
  = Nil
  | a :. List a
infixr 5 :.

mapList :: (a -> b) -> List a -> List b
mapList _        Nil      = Nil
mapList function (h :. t) = function h :. mapList function t

data Fun i a
  = F (i -> a)

mapFun :: (a -> b) -> Fun i a -> Fun i b
mapFun function (F fun)
  = F $ function . fun

data Tree a
  = Leaf
  | Branch (Tree a) a (Tree a)

mapTree :: (a -> b) -> Tree a -> Tree b
mapTree _        Leaf = Leaf
mapTree function (Branch leftTree value rightTree)
  = Branch (mapTree function leftTree) (function value) $ mapTree function rightTree

