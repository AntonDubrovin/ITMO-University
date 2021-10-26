module HW1.T4
  ( tfoldr
  , treeToList
  ) where

import HW1.T3

tfoldr :: (a -> b -> b) -> b -> Tree a -> b
tfoldr _ value Leaf                         = value
tfoldr f value (Branch _ left valueT right) = tfoldr f (f valueT $ tfoldr f value right) left

treeToList :: Tree a -> [a]
treeToList = tfoldr (:) []

