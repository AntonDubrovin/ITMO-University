{-# LANGUAGE BlockArguments             #-}
{-# LANGUAGE DerivingStrategies         #-}
{-# LANGUAGE GeneralizedNewtypeDeriving #-}

module HW2.T6
  ( ParseError (..)
  , Parser (..)
  , pAbbr
  , pChar
  , pEof
  , parseError
  , parseExpr
  , pDoubles
  , pIntegers
  , pWhiteSpaces
  , runP
  ) where

import Control.Applicative (Alternative (..), optional)
import Control.Monad       (MonadPlus, mfilter, void)
import Data.Char           (digitToInt, isDigit, isUpper)
import Data.Foldable       (Foldable (foldl'))
import Data.Scientific     (scientific, toRealFloat)
import GHC.Natural         (Natural)
import HW2.T1              (Annotated ((:#)), Except (Error, Success))
import HW2.T4              (Expr (..), Prim (..))
import HW2.T5              (ExceptState (ES), runES)

data ParseError = ErrorAtPos Natural

newtype Parser a = P (ExceptState ParseError (Natural, String) a)
  deriving newtype (Functor, Applicative, Monad)

runP :: Parser a -> String -> Except ParseError a
runP (P parser) expression = case runES parser (0, expression) of
  Error   err           -> Error   err
  Success (result :# _) -> Success result

-- if s is [] then pChar throws Error in position = pos
-- else it returns Success, and add 1 to current position (pos + 1)
pChar :: Parser Char
pChar = P $ ES \(pos, s) ->
  case s of
    []     -> Error   (ErrorAtPos pos)
    (c:cs) -> Success (c :# (pos + 1, cs))

parseError :: Parser a
parseError = P $ ES \(pos, _) -> Error(ErrorAtPos pos)

instance Alternative Parser where
  empty = parseError
  (<|>) (P firstParser) (P secondParser) = P $ ES $ \(pos, s) ->
    case runES firstParser (pos, s) of
      Success _ -> runES firstParser  (pos, s)
      Error   _ -> runES secondParser (pos, s)

instance MonadPlus Parser

pEof :: Parser ()
pEof = P $ ES \(pos, s) ->
  case s of
    []    -> Success (() :# (pos, s))
    (_:_) -> Error   (ErrorAtPos pos)

pFilter :: Char -> Parser Char
pFilter filterChar = mfilter (== filterChar) pChar

pFilterFun :: (Char -> Bool) -> Parser Char
pFilterFun filterCharFun = mfilter filterCharFun pChar

pAbbr :: Parser String
pAbbr = do
  abbr <- some $ pFilterFun isUpper
  pEof
  pure abbr

pWhiteSpaces :: Parser String
pWhiteSpaces = many $ pFilter ' '

pIntegers :: Parser String
pIntegers = some $ pFilterFun isDigit

pDoubles :: Parser Double
pDoubles = do
  void pWhiteSpaces
  wholePart      <- pIntegers
  void $ optional $ pFilter '.'
  fractionalPart <- pIntegers <|> pure []
  let scientificPart    = wholePart ++ fractionalPart
  let scientificIntPart = foldl' (\x y -> x * 10 + toInteger (digitToInt y)) 0 scientificPart
  pure $ toRealFloat $ scientific (toInteger scientificIntPart) $ -(length fractionalPart)

parseExpr :: String -> Except ParseError Expr
parseExpr = runP $ parseE <* pEof

parseE :: Parser Expr
parseE = pET1 parseT1 parseE'

parseE' :: Expr -> Parser Expr
parseE' = pE'T1' parseT1 parseE' Add Sub '+' '-'

parseT1 :: Parser Expr
parseT1 = pET1 parseT2 parseT1'

parseT1' :: Expr -> Parser Expr
parseT1' = pE'T1' parseT2 parseT1' Mul Div '*' '/'

parseT2 :: Parser Expr
parseT2 = do
  void pWhiteSpaces
  pFilterBrackets <|> Val <$> pDoubles

pET1 :: Parser Expr -> (Expr -> Parser Expr) -> Parser Expr
pET1 internalFunction externalFunction = do
  void pWhiteSpaces
  expression <- internalFunction
  externalFunction expression

pE'T1'
  :: Parser Expr
  -> (Expr -> Parser Expr)
  -> (Expr -> Expr -> Prim Expr)
  -> (Expr -> Expr -> Prim Expr)
  -> Char
  -> Char
  -> Expr
  -> Parser Expr
pE'T1' functionSign functionResult firstPrim secondPrim firstSign secondSign expression = do
  void pWhiteSpaces
  resultExpression <- optional $
    pSign functionSign firstPrim  firstSign  expression <|>
    pSign functionSign secondPrim secondSign expression
  case resultExpression of
    (Just result) -> functionResult result
    Nothing       -> pure expression

pSign :: Parser Expr -> (Expr -> Expr -> Prim Expr) -> Char -> Expr -> Parser Expr
pSign function sign filterChar expression = do
  void pWhiteSpaces
  void $ pFilter filterChar
  Op . sign expression <$> function

pFilterBrackets :: Parser Expr
pFilterBrackets = do
  void $ pFilter '('
  expression <- parseE
  void $ pFilter ')'
  pure expression

