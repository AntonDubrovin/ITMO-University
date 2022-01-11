module HW0.T4
  ( fac
  , fib
  , map'
  , repeat'
  ) where

import Data.Function (fix)
import GHC.Natural   (Natural)

repeat' :: a -> [a]
repeat' x = fix (x :)

map' :: (a -> b) -> [a] -> [b]
map' = fix $ \rec fun list ->
  case list of
    []    -> []
    h : t -> fun h : rec fun t

fib :: Natural -> Natural
fib n = fix $ const $ fib_ n 0 1
  where
    fib_ :: Natural -> Natural -> Natural -> Natural
    fib_ 0  first _      = first
    fib_ 1  _     second = second
    fib_ n_ first second = fib_ (n_ - 1) second (first + second)

fac :: Natural -> Natural
fac = fix $ \rec n ->
  if n == 0
  then 1
  else n * rec (n - 1)
