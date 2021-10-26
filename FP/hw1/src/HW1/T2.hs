module HW1.T2
  ( N (..)
  , ncmp
  , ndiv
  , nEven
  , nFromNatural
  , nmod
  , nmult
  , nOdd
  , nplus
  , nsub
  , nToNum
  ) where

import GHC.Natural (Natural)
import Data.Maybe (fromMaybe)

data N
  = Z
  | S N
  deriving Show

nplus :: N -> N -> N
nplus Z               number       = number
nplus number          Z            = number
nplus (S firstNumber) secondNumber = S $ nplus firstNumber secondNumber

nmult :: N -> N -> N
nmult Z               _            = Z
nmult _               Z            = Z
nmult (S Z)           number       = number
nmult number          (S Z)        = number
nmult (S firstNumber) secondNumber = nplus (nmult firstNumber secondNumber) secondNumber

nsub :: N -> N -> Maybe N
nsub Z               Z                = Just Z
nsub Z               _                = Nothing
nsub number          Z                = Just number
nsub (S firstNumber) (S secondNumber) = nsub firstNumber secondNumber

ncmp :: N -> N -> Ordering
ncmp Z               Z                = EQ
ncmp Z               _                = LT
ncmp _               Z                = GT
ncmp (S firstNumber) (S secondNumber) = ncmp firstNumber secondNumber

nFromNatural :: Natural -> N
nFromNatural 0      = Z
nFromNatural number = S $ nFromNatural $ number - 1

nToNum :: Num a => N -> a
nToNum number = _nToNum number 0
  where
    _nToNum :: Num a => N -> a -> a
    _nToNum Z           result = result
    _nToNum (S _number) result = _nToNum _number $ result + 1

nEven :: N -> Bool
nEven Z              = True
nEven (S Z)          = False
nEven (S (S number)) = nEven number

nOdd :: N -> Bool
nOdd Z              = False
nOdd (S Z)          = True
nOdd (S (S number)) = nOdd number

ndiv :: N -> N -> N
ndiv _     Z      = error "div by zero"
ndiv first second = _ndiv (Just first) second Z
  where
    _ndiv Nothing       _       Z          = undefined
    _ndiv Nothing       _       (S result) = result
    _ndiv (Just _first) _second result     = _ndiv (nsub _first _second) _second $ S result

nmod :: N -> N -> N
nmod _     Z      = error "div by zero"
nmod first second =
  if ncmp first second == LT
  then first
  else nmod (fromMaybe undefined $ nsub first second) second

