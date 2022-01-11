{-# LANGUAGE TypeOperators #-}
module HW0.T1
  ( type (<->) (Iso)
  , assocEither
  , assocPair
  , distrib
  , flipIso
  , runIso
  ) where

data a <-> b = Iso (a -> b) (b -> a)

flipIso :: (a <-> b) -> (b <-> a)
flipIso (Iso f g) = Iso g f

runIso :: (a <-> b) -> (a -> b)
runIso (Iso f _) = f

distrib :: Either a (b, c) -> (Either a b, Either a c)
distrib (Left  a)      = (Left  a, Left  a)
distrib (Right (b, c)) = (Right b, Right c)

assocPair :: (a, (b, c)) <-> ((a, b), c)
assocPair = Iso assocPair1 assocPair2

assocPair1 :: (a, (b, c)) -> ((a, b), c)
assocPair1 (a, (b, c)) = ((a, b), c)

assocPair2 :: ((a, b), c) -> (a, (b, c))
assocPair2 ((a, b), c) = (a, (b, c))

assocEither :: Either a (Either b c) <-> Either (Either a b) c
assocEither = Iso assocEither1 assocEither2

assocEither1 :: Either a (Either b c) -> Either (Either a b) c
assocEither1 (Left  a)         = Left  (Left a)
assocEither1 (Right (Left  b)) = Left  (Right b)
assocEither1 (Right (Right c)) = Right c

assocEither2 :: Either (Either a b) c -> Either a (Either b c)
assocEither2 (Left  (Left  a)) = Left  a
assocEither2 (Left  (Right b)) = Right (Left b)
assocEither2 (Right c)         = Right (Right c)
