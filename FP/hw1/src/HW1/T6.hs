module HW1.T6 
  ( epart
  , mcat
  ) where

mcat :: Monoid a => [Maybe a] -> a
mcat = foldl (flip _mcat) mempty
  where
    _mcat :: Monoid a => Maybe a -> a -> a
    _mcat (Just monoid) acc = acc <> monoid
    _mcat Nothing       acc = acc

epart :: (Monoid a, Monoid b) => [Either a b] -> (a, b)
epart = foldl (flip _epart) (mempty, mempty)
  where
    _epart :: (Monoid a, Monoid b) => Either a b -> (a, b) -> (a, b)
    _epart (Left monoid)  (left, right) = (left <> monoid, right)
    _epart (Right monoid) (left, right) = (left, right <> monoid)

