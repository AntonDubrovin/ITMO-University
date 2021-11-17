module HW2.T5
  ( EvaluationError (..)
  , ExceptState (..)
  , eval
  , joinExceptState
  , mapExceptState
  , modifyExceptState
  , throwExceptState
  , wrapExceptState
  ) where

import Control.Monad
import HW2.T1 (Annotated ((:#)), Except (Error, Success))
import HW2.T4 (Expr (Op, Val), Prim (Abs, Add, Div, Mul, Sgn, Sub))

data ExceptState e s a = ES { runES :: s -> Except e (Annotated s a) }

mapExceptState :: (a -> b) -> ExceptState e s a -> ExceptState e s b
mapExceptState function state = ES $ \s ->
  case runES state s of
    Success (first :# second) -> Success (function first :# second)
    Error   err               -> Error   err

wrapExceptState :: a -> ExceptState e s a
wrapExceptState a = ES $ \s -> Success(a :#s)

joinExceptState :: ExceptState e s (ExceptState e s a) -> ExceptState e s a
joinExceptState state = ES $ \s ->
  case runES state s of
    Success (first :# second) -> runES first second
    Error   err               -> Error err

modifyExceptState :: (s -> s) -> ExceptState e s ()
modifyExceptState function = ES $ \s -> Success(() :# function s)

throwExceptState :: e -> ExceptState e s a
throwExceptState err = ES $ \_ -> Error err

instance Functor (ExceptState e s) where
  fmap = mapExceptState

instance Applicative (ExceptState e s) where
  pure = wrapExceptState
  p <*> q = Control.Monad.ap p q

instance Monad (ExceptState e s) where
  m >>= f = joinExceptState $ fmap f m

data EvaluationError = DivideByZero
eval :: Expr -> ExceptState EvaluationError [Prim Double] Double
eval (Val x       ) = pure x
eval (Op (Add x y)) = binaryOperation x y Add (+)
eval (Op (Sub x y)) = binaryOperation x y Sub (-)
eval (Op (Mul x y)) = binaryOperation x y Mul (*)
eval (Op (Abs x  )) = unaryOperation  x   Abs abs
eval (Op (Sgn x  )) = unaryOperation  x   Sgn signum
eval (Op (Div x y)) = do
  first  <- eval x
  second <- eval y
  if second == 0
  then throwExceptState DivideByZero
  else modifyExceptState (Div first second:)
  pure $ first / second

binaryOperation
  :: Expr
  -> Expr
  -> (Double -> Double -> Prim Double)
  -> (Double -> Double -> Double)
  -> ExceptState EvaluationError [Prim Double] Double
binaryOperation x y operation sign = do
  first  <- eval x
  second <- eval y
  modifyExceptState (operation first second:)
  pure $ first `sign` second

unaryOperation
  :: Expr
  -> (Double -> Prim Double)
  -> (Double -> Double)
  -> ExceptState EvaluationError [Prim Double] Double
unaryOperation x operation sign = do
  num <- eval x
  modifyExceptState (operation num:)
  pure $ sign num

