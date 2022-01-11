module HW0.T5
  ( Nat
  , nFromNatural
  , nmult
  , nplus
  , ns
  , nToNum
  , nz
  ) where

import GHC.Natural (Natural)

type Nat a = (a -> a) -> a -> a

-- (a -> a) -> a -> a
nz :: Nat a
nz _ a = a

-- ((a -> a) -> a -> a) -> (a -> a) -> a -> a
ns :: Nat a -> Nat a
ns nat fun a = fun $ nat fun a

-- ((a -> a) -> a -> a) -> ((a -> a) -> a -> a) -> (a -> a) -> a -> a
nplus :: Nat a -> Nat a -> Nat a
nplus nat1 nat2 fun a = nat1 fun $ nat2 fun a

-- ((a -> a) -> a -> a) -> ((a -> a) -> a -> a) -> (a -> a) -> a -> a
nmult :: Nat a -> Nat a -> Nat a
nmult nat1 nat2 fun = nat1 $ nat2 fun

nFromNatural :: Natural -> Nat a
nFromNatural 0 = nz
nFromNatural a = ns $ nFromNatural $ a - 1

nToNum :: Num a => Nat a -> a
nToNum nat = nat (+1) 0
