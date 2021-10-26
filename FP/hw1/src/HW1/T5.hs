module HW1.T5
  ( splitOn
  , joinWith
  ) where

import Data.List.NonEmpty

joinWith :: a -> NonEmpty [a] -> [a]
joinWith joinChar (h:|t) = foldl (_joinWith joinChar) h t
  where
    _joinWith :: a -> [a] -> [a] -> [a]
    _joinWith _joinChar joinedElement element = joinedElement ++ [_joinChar]  ++ element

splitOn :: Eq a => a -> [a] -> NonEmpty [a]
splitOn splitChar list = fromList $ foldl (_splitOn splitChar) [[]] list
  where
    _splitOn :: Eq a => a -> [[a]] -> a -> [[a]]
    _splitOn _splitChar result current
      | _splitChar == current = result ++ [[]]
      | otherwise             = Prelude.init result ++ [Prelude.last result ++ [current]]

