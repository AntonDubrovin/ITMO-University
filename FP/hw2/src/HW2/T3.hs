module HW2.T3
  ( joinAnnotated
  , joinExcept
  , joinFun
  , joinList
  , joinOption
  ) where

import HW2.T1 (Annotated ((:#)), Except (Error, Success),
               Fun (F), List (Nil, (:.)), Option (None, Some))
import HW2.T2 (concatList)

joinOption :: Option (Option a) -> Option a
joinOption None          = None
joinOption (Some option) = option

joinExcept :: Except e (Except e a) -> Except e a
joinExcept (Error   error ) = Error error
joinExcept (Success except) = except

joinAnnotated :: Semigroup e => Annotated e (Annotated e a) -> Annotated e a
joinAnnotated ((firstHead :# firstTail) :# second)
  = firstHead :# (second <> firstTail)

joinFun :: Fun i (Fun i a) -> Fun i a
joinFun (F function) = F $ \element -> getFunction (function element) element
  where
    getFunction :: Fun i a -> i -> a
    getFunction (F _function) = _function

joinList :: List (List a) -> List a
joinList (head :. tail) = concatList head $ joinList tail
joinList Nil            = Nil

