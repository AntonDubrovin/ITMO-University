module HW3.Parser
  ( parse
  ) where

import Control.Monad                  (void)
import Control.Monad.Combinators.Expr (Operator (..), makeExprParser)
import Data.ByteString                as BS (pack)
import Data.Char                      (digitToInt, isAlpha,
                                                 isAlphaNum)
import Data.Text                      as T (pack)
import Data.Void                      (Void)
import HW3.Base                       (HiAction (..), HiExpr (..),
                                                 HiFun (..), HiValue (..))
import Text.Megaparsec                (Parsec, choice, eof, many,
                                                 manyTill, notFollowedBy,
                                                 optional, runParser, satisfy,
                                                 sepBy, sepBy1, sepEndBy, try,
                                                 (<|>))
import Text.Megaparsec.Byte           (string)
import Text.Megaparsec.Char           (char, hexDigitChar, space1)
import Text.Megaparsec.Char.Lexer     as L (charLiteral, scientific,
                                                      signed, skipBlockComment,
                                                      skipLineComment, space)
import Text.Megaparsec.Error          (ParseErrorBundle)

type Parser = Parsec Void String

-- | Initial function in the parser.
-- Just runs our parser on the given string.
parse :: String -> Either (ParseErrorBundle String Void) HiExpr
parse = runParser (parseOperators <* eof) ""

-- | Parser for functions.
-- Returns the required function wrapper by the corresponding function name.
parseFun :: Parser HiFun
parseFun =
  choice
    [ HiFunAdd            <$ string "add",
      HiFunSub            <$ string "sub",
      HiFunMul            <$ string "mul",
      HiFunDiv            <$ string "div",
      HiFunAnd            <$ string "and",
      HiFunOr             <$ string "or",
      HiFunLessThan       <$ string "less-than",
      HiFunGreaterThan    <$ string "greater-than",
      HiFunEquals         <$ string "equals",
      HiFunNotLessThan    <$ string "not-less-than",
      HiFunNotGreaterThan <$ string "not-greater-than",
      HiFunNotEquals      <$ string "not-equals",
      HiFunNot            <$ string "not",
      HiFunIf             <$ string "if",
      HiFunLength         <$ string "length",
      HiFunToUpper        <$ string "to-upper",
      HiFunToLower        <$ string "to-lower",
      HiFunReverse        <$ string "reverse",
      HiFunTrim           <$ string "trim",
      HiFunList           <$ string "list",
      HiFunRange          <$ string "range",
      HiFunFold           <$ string "fold",
      HiFunPackBytes      <$ string "pack-bytes",
      HiFunUnpackBytes    <$ string "unpack-bytes",
      HiFunEncodeUtf8     <$ string "encode-utf8",
      HiFunDecodeUtf8     <$ string "decode-utf8",
      HiFunZip            <$ string "zip",
      HiFunUnzip          <$ string "unzip",
      HiFunSerialise      <$ string "serialise",
      HiFunDeserialise    <$ string "deserialise",
      HiFunRead           <$ string "read",
      HiFunWrite          <$ string "write",
      HiFunMkDir          <$ string "mkdir",
      HiFunChDir          <$ string "cd",
      HiFunParseTime      <$ string "parse-time",
      HiFunRand           <$ string "rand",
      HiFunEcho           <$ string "echo",
      HiFunCount          <$ string "count",
      HiFunKeys           <$ string "keys",
      HiFunValues         <$ string "values",
      HiFunInvert         <$ string "invert"
    ]

-- | Parser for values.
-- Tries to parse the function.
-- Wraps the return value (function wrapper) into a common wrapper of functions
-- or tries to parse the numbers (signed)
-- or tries to parse booleans/null/strings/bytes or commands "cwd" or "now".
parseValue :: Parser HiValue
parseValue =
  sc *>
  choice
    [ HiValueFunction <$> parseFun
    , HiValueNumber . toRational <$> L.signed sc L.scientific
    , parseBool
    , parseNull
    , parseString
    , parseBytes
    , parseCwd
    , parseNow
    ]
  <* sc

-- | Parser for booleans.
-- Returns the required boolean wrapper by the corresponding boolean name.
parseBool :: Parser HiValue
parseBool =
  choice
    [ HiValueBool True  <$ string "true"
    , HiValueBool False <$ string "false"
    ]

-- | Parser for null.
-- Just return null wrapper if it encounters a string null.
parseNull :: Parser HiValue
parseNull = HiValueNull <$ string "null"

-- | Parser for strings.
-- If our parser has encountered a value equal to a quote, a set of chars and a quote,
-- then converts a string to text and wraps it into a string wrapper.
parseString :: Parser HiValue
parseString = HiValueString . T.pack <$> (char '"' >> manyTill L.charLiteral (char '"'))

-- | Parser for lists.
-- Parses the opening square bracket, inside parses any operators,
-- and parses the closing square bracket.
-- In the end checks if result is empty then returns an empty list,
-- wrapped in a (expr -> value -> fun list) wrapper.
-- Otherwise returns received list in the same wrapper.
parseList :: Parser HiExpr
parseList = do
    void sc
    void $ string "["
    void sc
    expression <- sepBy parseOperators $ string ","
    void sc
    void $ string "]"
    void sc
    case Prelude.length expression of
      0 -> return $ HiExprApply (HiExprValue (HiValueFunction HiFunList)) []
      _ -> return $ HiExprApply (HiExprValue (HiValueFunction HiFunList)) expression

-- | Parser for dictionaries.
-- Parses "{" and "}" and invokes parsePair function
-- that parses pairs in brackets.
parseDictionaries :: Parser [(HiExpr, HiExpr)]
parseDictionaries = do
  void sc
  void $ string "{"
  void sc
  expression <- sepBy parsePairs (string ",")
  void sc
  void $ string "}"
  void sc
  case Prelude.length expression of
    0 -> return []
    _ -> return expression

-- | Parser for pairs in dictionaries.
parsePairs :: Parser (HiExpr, HiExpr)
parsePairs = do
  void sc
  key <- parseOperators
  void sc
  void $ string ":"
  void sc
  value <- parseOperators
  void sc
  return (key, value)

-- | Parser for something, then goes after dot.
parseDot :: Parser String
parseDot = Prelude.foldl1
  (\acc current -> acc ++ "-" ++ current) <$>
  (((:) <$> satisfy isAlpha <*> many (satisfy isAlphaNum)) `sepBy1` char '-')

-- | Parser for bytes.
-- Parses the opening square bracket and hash (start of bytes),
-- then parses any number of bytes(2 hexDigitChar).
parseBytes :: Parser HiValue
parseBytes = do
  void sc
  void $ string "[#"
  void sc
  expression <-
     sepEndBy ((\parse1 parse2 ->
          (\[first, second] ->
              first * 16 + second
          ) $ Prelude.map digitToInt [parse1, parse2]
      ) <$> hexDigitChar <*> hexDigitChar) (try $ string " " <* sc)
  void sc
  void $ string "#]"
  return . HiValueBytes $ BS.pack $ Prelude.map fromIntegral expression

-- | Parser for cwd command.
-- Returns a wrapper (action -> cwd) if it encounters a string "cwd".
parseCwd :: Parser HiValue
parseCwd = HiValueAction HiActionCwd <$ string "cwd"

-- | Parser for now command.
-- Returns a wrapper (action -> now) if it encounters a string "now".
parseNow :: Parser HiValue
parseNow = HiValueAction HiActionNow <$ string "now"

-- | Parser for apply wrapper(unkown parseValue).
-- Parses value by invoking parseValue function, wraps the received result into exprValue,
-- then parses function arguments by invoking parseArguments function,
-- and wraps the finally result into exprApply
parseApply :: Parser HiExpr
parseApply = HiExprApply <$> (HiExprValue <$> parseValue) <*> parseArguments

-- | Parser for apply wrapper(known parseValue)
-- Do the same as the previous function,
-- but we know the function
parseValueApply :: Parser HiExpr -> Parser HiExpr
parseValueApply value = HiExprApply <$> value <*> parseArguments

-- | Parser for most expressions.
-- Executes apply, value, list or dictionaries.
parseExpr :: Parser HiExpr
parseExpr =
  choice
    [ try parseApply
    , HiExprValue <$> parseValue
    , parseList
    , HiExprDict <$> parseDictionaries
    ]

-- | Parser for next arguments.
-- Parses the arguments, which come after the current arguments.
-- For instance, add(1,2)(1,2) or if(..)(1,2).
parseNextArguments :: Parser HiExpr -> Parser HiExpr
parseNextArguments value = do
  nextValue <- parseValueApply value
  parseNextExclamation (return nextValue) <|> parseNextArguments (return nextValue) <|> return nextValue

-- | Parser for ! after some operations.
-- Just parse ! and something, that can be after him(including !).
parseNextExclamation :: Parser HiExpr -> Parser HiExpr
parseNextExclamation value = do
  nextValue <- value
  exclamation <- optional $ HiExprRun <$> (nextValue <$ string "!")
  void sc
  case exclamation of
    Nothing -> parseNextArguments (return nextValue) <|> return nextValue
    (Just parserExcl) ->
      parseNextArguments (return parserExcl) <|>
      parseNextExclamation (return parserExcl) <|>
      return parserExcl

-- | Parser for expressions.
-- Parse operators in brackets or invokes parseExpr.
-- After tries to parse 0 or more !, and then checks,
-- if there is 1 or more ! returns expression in the wrapper as many times as there are exclamations.
-- If there is no exclamations, then tries to parse next arguments, or returns expression.
parseExpression :: Parser HiExpr
parseExpression = do
  void sc
  expr <- string "(" *> parseOperators <* string ")" <|> parseExpr
  void sc
  parseNextExclamation (return expr) <|> parseNextArguments (return expr) <|> return expr

-- | Parser for operators.
-- Just invokes makeExprParser with our operator table.
parseOperators :: Parser HiExpr
parseOperators = makeExprParser parseExpression operatorTable

-- | Parser for arguments.
-- Parses arguments in brackets or after dot.
parseArguments :: Parser [HiExpr]
parseArguments = do
  dot <- optional $ string "."
  case dot of
    Nothing -> do
      void sc
      void $ string "("
      void sc
      expression <- sepBy parseOperators (string ",")
      void sc
      void $ string ")"
      void sc
      case Prelude.length expression of
        0 -> return []
        _ -> return expression
    _ -> do
      expr <- HiExprValue . HiValueString . T.pack <$> parseDot
      return [expr]

-- | Parser for whitespaces.
-- Parse many whitespaces.
sc :: Parser ()
sc = L.space
  space1
  (L.skipLineComment "//")
  (L.skipBlockComment "/*" "*/")

-- | Our table for operators.
-- Operator table with information about the
-- associativity of the operation, operation and wrapper.
operatorTable :: [[Operator Parser HiExpr]]
operatorTable =
  [ [ binaryLeft  "*"  HiFunMul
    , binaryDiv   "/"  HiFunDiv
    ]
  , [ binaryLeft  "+"  HiFunAdd
    , binaryLeft  "-"  HiFunSub
    ]
  , [ binaryNo    "<=" HiFunNotGreaterThan
    , binaryNo    ">=" HiFunNotLessThan
    , binaryNo    ">"  HiFunGreaterThan
    , binaryNo    "<"  HiFunLessThan
    , binaryNo    "==" HiFunEquals
    , binaryNo    "/=" HiFunNotEquals
    ]
  , [ binaryRight "&&" HiFunAnd]
  , [ binaryRight "||" HiFunOr]
  ]

-- | Left associativity function.
binaryLeft:: String -> HiFun -> Operator Parser HiExpr
binaryLeft name f = InfixL ((\a b -> HiExprApply (HiExprValue (HiValueFunction f)) [a, b]) <$ string name)

-- | Special function for division.
-- Needed to be able to determine division from unequal.
-- Ignores '=' after '/'
binaryDiv:: String -> HiFun -> Operator Parser HiExpr
binaryDiv name f =
  InfixL
    ((\a b -> HiExprApply (HiExprValue (HiValueFunction f)) [a, b]) <$
    try(string name <* notFollowedBy (string "=")))

-- | Right associativity function.
binaryRight :: String -> HiFun -> Operator Parser HiExpr
binaryRight name f = InfixR ((\a b -> HiExprApply (HiExprValue (HiValueFunction f)) [a, b]) <$ string name)

-- | No associativity function.
binaryNo :: String -> HiFun -> Operator Parser HiExpr
binaryNo name f = InfixN ((\a b -> HiExprApply (HiExprValue (HiValueFunction f)) [a, b]) <$ string name)
