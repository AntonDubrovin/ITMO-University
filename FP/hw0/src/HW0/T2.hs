module HW0.T2
  ( Not
  , doubleNeg
  , reduceTripleNeg
  ) where

import Data.Void (Void)

type Not a = a -> Void

-- a -> (a -> Void) -> Void
doubleNeg :: a -> Not (Not a)
doubleNeg a voidFun = voidFun a

-- (((a -> Void) -> Void) -> Void) -> a -> Void
reduceTripleNeg :: Not (Not (Not a)) -> Not a
reduceTripleNeg voidFun a = voidFun $ doubleNeg a
