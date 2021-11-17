module HW2.T4
  ( Expr (..)
  , Prim (..)
  , State (..)
  , eval
  , joinState
  , mapState
  , modifyState
  , wrapState
  ) where

import Control.Monad
import HW2.T1 (Annotated ((:#)))

data State s a = S { runS :: s -> Annotated s a }

mapState :: (a -> b) -> State s a -> State s b
mapState function state = S $ \s ->
  case runS state s of
    first :# second -> function first :# second

wrapState :: a -> State s a
wrapState a = S (a :#)

joinState :: State s (State s a) -> State s a
joinState state = S $ \s ->
  case runS state s of
    first :# second -> runS first second

modifyState :: (s -> s) -> State s ()
modifyState function = S $ \s -> () :# function s

instance Functor (State s) where
  fmap = mapState

instance Applicative (State s) where
  pure = wrapState
  p <*> q = Control.Monad.ap p q

instance Monad (State s) where
  m >>= f = joinState $ fmap f m

data Prim a =
    Add a a      -- (+)
  | Sub a a      -- (-)
  | Mul a a      -- (*)
  | Div a a      -- (/)
  | Abs a        -- abs
  | Sgn a        -- signum

data Expr
  = Val Double
  | Op  (Prim Expr)

instance Num Expr where
  x      +    y = Op  $ Add x y
  x      -    y = Op  $ Sub x y
  x      *    y = Op  $ Mul x y
  abs         x = Op  $ Abs x
  signum      x = Op  $ Sgn x
  fromInteger x = Val $ fromInteger x

instance Fractional Expr where
  x      /     y = Op  $ Div x y
  fromRational x = Val $ fromRational x

eval :: Expr -> State [Prim Double] Double
eval (Val x       ) = pure x
eval (Op (Add x y)) = binaryOperation x y Add (+)
eval (Op (Sub x y)) = binaryOperation x y Sub (-)
eval (Op (Mul x y)) = binaryOperation x y Mul (*)
eval (Op (Div x y)) = binaryOperation x y Div (/)
eval (Op (Abs x  )) = unaryOperation  x   Abs abs
eval (Op (Sgn x  )) = unaryOperation  x   Sgn signum

binaryOperation
  :: Expr
  -> Expr
  -> (Double -> Double -> Prim Double)
  -> (Double -> Double -> Double)
  -> State [Prim Double] Double
binaryOperation x y operation sign = do
  first  <- eval x
  second <- eval y
  modifyState (operation first second:)
  pure $ first `sign` second

unaryOperation
  :: Expr
  -> (Double -> Prim Double)
  -> (Double -> Double)
  -> State [Prim Double] Double
unaryOperation x operation sign = do
  num <- eval x
  modifyState (operation num:)
  pure $ sign num

