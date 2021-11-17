module HW2.T2
  ( concatList
  , distAnnotated
  , distExcept
  , distFun
  , distList
  , distOption
  , distPair
  , distPrioritised
  , distQuad
  , distStream
  , wrapAnnotated
  , wrapExcept
  , wrapFun
  , wrapList
  , wrapOption
  , wrapPair
  , wrapPrioritised
  , wrapQuad
  , wrapStream
  ) where

import HW2.T1 (Annotated ((:#)), Except (Error, Success), Fun (F), List (Nil, (:.)),
               Option (None, Some), Pair (P), Prioritised (High, Low, Medium), Quad (Q),
               Stream ((:>)))

distOption :: (Option a, Option b) -> Option (a, b)
distOption (_         , None       ) = None
distOption (None      , _          ) = None
distOption (Some first, Some second) = Some (first, second)

wrapOption :: a -> Option a
wrapOption = Some

distPair :: (Pair a, Pair b) -> Pair (a, b)
distPair (P ffirst fsecond, P sfirst ssecond)
  = P (ffirst, sfirst) (fsecond, ssecond)

wrapPair :: a -> Pair a
wrapPair a = P a a

distQuad :: (Quad a, Quad b) -> Quad (a, b)
distQuad (Q ffirst fsecond fthird ffourh, Q sfirst ssecond sthird sfourth)
  = Q (ffirst, sfirst) (fsecond, ssecond) (fthird, sthird) (ffourh, sfourth)

wrapQuad :: a -> Quad a
wrapQuad a = Q a a a a

distAnnotated :: Semigroup e => (Annotated e a, Annotated e b) -> Annotated e (a, b)
distAnnotated (firstHead :# firstTail, secondHead :# secondTail)
  = (firstHead, secondHead) :# (firstTail <> secondTail)

wrapAnnotated :: Monoid e => a -> Annotated e a
wrapAnnotated a = a :# mempty

distExcept :: (Except e a, Except e b) -> Except e (a, b)
distExcept (Error error  , _             ) = Error error
distExcept (_            , Error error   ) = Error error
distExcept (Success first, Success second) = Success (first, second)

wrapExcept :: a -> Except e a
wrapExcept = Success

distPrioritised :: (Prioritised a, Prioritised b) -> Prioritised (a, b)
distPrioritised (Low    first, Low    second) = Low    (first, second)
distPrioritised (Low    first, Medium second) = Medium (first, second)
distPrioritised (Low    first, High   second) = High   (first, second)
distPrioritised (Medium first, Low    second) = Medium (first, second)
distPrioritised (Medium first, Medium second) = Medium (first, second)
distPrioritised (Medium first, High   second) = High   (first, second)
distPrioritised (High   first, Low    second) = High   (first, second)
distPrioritised (High   first, Medium second) = High   (first, second)
distPrioritised (High   first, High   second) = High   (first, second)

wrapPrioritised :: a -> Prioritised a
wrapPrioritised = Low

distStream :: (Stream a, Stream b) -> Stream (a, b)
distStream (firstHead :> firstTail, secondHead :> secondTail)
  = (firstHead, secondHead) :> distStream (firstTail, secondTail)

wrapStream :: a -> Stream a
wrapStream a = a :> wrapStream a

distList :: (List a, List b) -> List (a, b)
distList (_, Nil) = Nil
distList (Nil, _) = Nil
distList (firstHead :. firstTail, second)
 = concatList (append firstHead second) (distList (firstTail, second))
   where
     append :: a -> List b -> List (a, b)
     append _       Nil              = Nil
     append element (_head :. _tail) = (element, _head) :. append element _tail

concatList :: List a -> List a -> List a
concatList a                        Nil    = a
concatList Nil                      a      = a
concatList (firstHead :. firstTail) second = firstHead :. concatList firstTail second

wrapList :: a -> List a
wrapList a = a :. Nil

distFun :: (Fun i a, Fun i b) -> Fun i (a, b)
distFun (F first, F second)
  = F $ \element -> (first element, second element)

wrapFun :: a -> Fun i a
wrapFun a = F $ const a

