module HW3.Evaluator
  ( eval
  , evalAdd
  , evalDiv
  , evalMul
  , evalSub
  ) where

import Codec.Compression.Zlib     (bestCompression, compressLevel,
                                             compressWith, decompressWith,
                                             defaultCompressParams,
                                             defaultDecompressParams)
import Codec.Serialise            (deserialise, serialise)
import Control.Monad.Trans.Except (ExceptT (..), runExceptT, throwE)
import Data.ByteString            as BS (length, pack, reverse,
                                                   unpack)
import Data.ByteString.Char8      as C8 (concat, unpack)
import Data.ByteString.Lazy       (fromStrict, toStrict)
import Data.Char                  (ord)
import Data.Foldable              as F (foldlM, toList)
import Data.Map                   as M (fromList)
import Data.Map.Strict            as MS (Map, assocs, elems, empty,
                                                   insertWith, keys, lookup,
                                                   map)
import Data.Ratio                 (denominator, numerator)
import Data.Semigroup             (stimes)
import Data.Sequence              as S (Seq (Empty, (:<|)), drop,
                                                  fromList, index, length,
                                                  mapWithIndex, reverse, sort,
                                                  take, (><))
import Data.Text                  as T (drop, index, length, pack,
                                                  reverse, strip, take, toLower,
                                                  toUpper, unpack)
import Data.Text.Encoding         (decodeUtf8', encodeUtf8)
import Data.Time
import HW3.Base                   (HiAction (..), HiError (..),
                                             HiExpr (..), HiFun (..), HiMonad,
                                             HiValue (..), runAction)
import Text.Read                  (readMaybe)

-- | Evaluator entry point.
-- Just invokes evalExceptT and getting rid of Either.
eval :: HiMonad m => HiExpr -> m (Either HiError HiValue)
eval = runExceptT . evalExceptT

-- | Evaluator with exceptT instead of Either.
-- Evaluates the required calculations.
evalExceptT :: HiMonad m => HiExpr -> ExceptT HiError m HiValue
-- ^ If received value is just exprValue - returns it.
evalExceptT (HiExprValue value) = pure value
-- ^ If received value is action,
-- then invokes evalRun and checks if received value if action.
-- If yes, then invokes runAction.
-- Else throws invalid arguments error.
evalExceptT (HiExprRun run) = do
  evalRun <- evalExceptT run
  case evalRun of
    (HiValueAction action) -> ExceptT $ Right <$> runAction action
    _                      -> throwE HiErrorInvalidArgument
-- ^ If received value is function.
evalExceptT (HiExprApply (HiExprValue (HiValueFunction function)) arguments) =
  case function of
    -- Three functions, for which we don't want to evaluate arguments,
    -- Because of laziness.
    HiFunIf  -> evalIf  arguments
    HiFunAnd -> evalAnd arguments
    HiFunOr  -> evalOr  arguments
    _ -> do
      -- For other functions evaluates arguments,
      -- and then invokes correspond eval functions.
      resultArguments <- evalArguments arguments
      case function of
        HiFunAdd            -> evalAdd            resultArguments
        HiFunSub            -> evalSub            resultArguments
        HiFunMul            -> evalMul            resultArguments
        HiFunDiv            -> evalDiv            resultArguments
        HiFunNot            -> evalNot            resultArguments
        HiFunLessThan       -> evalLessThan       resultArguments
        HiFunGreaterThan    -> evalGreaterThan    resultArguments
        HiFunEquals         -> evalEquals         resultArguments
        HiFunNotLessThan    -> evalNotLessThan    resultArguments
        HiFunNotGreaterThan -> evalNotGreaterThan resultArguments
        HiFunNotEquals      -> evalNotEquals      resultArguments
        HiFunLength         -> evalLength         resultArguments
        HiFunToUpper        -> evalToUpper        resultArguments
        HiFunToLower        -> evalToLower        resultArguments
        HiFunReverse        -> evalReverse        resultArguments
        HiFunTrim           -> evalTrim           resultArguments
        HiFunList           -> evalList           resultArguments
        HiFunRange          -> evalRange          resultArguments
        HiFunFold           -> evalFold           resultArguments
        HiFunPackBytes      -> evalPackBytes      resultArguments
        HiFunUnpackBytes    -> evalUnpackBytes    resultArguments
        HiFunEncodeUtf8     -> evalEncodeUtf8     resultArguments
        HiFunDecodeUtf8     -> evalDecodeUtf8     resultArguments
        HiFunZip            -> evalZip            resultArguments
        HiFunUnzip          -> evalUnzip          resultArguments
        HiFunSerialise      -> evalSerialise      resultArguments
        HiFunDeserialise    -> evalDeserialise    resultArguments
        HiFunRead           -> evalRead           resultArguments
        HiFunWrite          -> evalWrite          resultArguments
        HiFunMkDir          -> evalMkDir          resultArguments
        HiFunChDir          -> evalChDir          resultArguments
        HiFunParseTime      -> evalParseTime      resultArguments
        HiFunRand           -> evalRand           resultArguments
        HiFunEcho           -> evalEcho           resultArguments
        HiFunCount          -> evalCount          resultArguments
        HiFunKeys           -> evalKeys           resultArguments
        HiFunValues         -> evalValues         resultArguments
        HiFunInvert         -> evalInvert         resultArguments
        _                   -> undefined
-- ^ If received function if number - then throws invalid function error.
evalExceptT (HiExprApply (HiExprValue (HiValueNumber _)) _) = throwE HiErrorInvalidFunction
-- ^ If received expression is dictionary and there is 1 argument - then evaluate index,
-- and just invokes helper evalDictIndex function.
evalExceptT (HiExprApply (HiExprValue (HiValueDict dict)) [ind]) = do
  resultIndex <- evalExceptT ind
  evalDictIndex dict resultIndex
-- ^ If there is some expression with 1 arguments after, then evaluate index,
-- then if index if not number - throws invalid arguments error.
-- If number, then checks, if number is integer and in integer bounds.
-- If not - then throws invalid arguments error.
-- If integer - just invokes helper evalIndex function.
evalExceptT (HiExprApply (HiExprValue expression) [ind]) = do
  resultIndex <- evalExceptT ind
  case resultIndex of
    (HiValueNumber resultIndexNumber) ->
      if denominator resultIndexNumber == 1
      && evalInteger resultIndexNumber
      then evalIndex expression resultIndexNumber
      else throwE HiErrorInvalidArgument
    _ -> throwE HiErrorInvalidFunction
-- ^ If there is some expression with 2 arguments after,
-- then evaluate both indexes, and evaluate length of expression.
-- Then checks if length, and indexes is numbers, (if no - throws invalid arguments error)
-- then evaluate finalIndexes by invokes helper evalTrueIndex function(advanced).
-- Then checks, if indexes is integers and in integer bounds, then invokes helper evalSlice function.
evalExceptT (HiExprApply (HiExprValue expression) [begin, end]) = do
  evalBegin      <- evalExceptT begin
  evalEnd        <- evalExceptT end
  evalLengthExpr <- evalLength [expression]
  case evalLengthExpr of
    (HiValueNumber lengthExpr) ->
      case evalBegin of
        (HiValueNumber numberBegin) ->
          let finalBegin = evalTrueIndex lengthExpr numberBegin
          in
            case evalEnd of
              (HiValueNumber numberEnd) ->
                let finalEnd = evalTrueIndex lengthExpr numberEnd
                in
                  if denominator finalBegin == 1
                  && denominator finalEnd == 1
                  && evalInteger finalBegin
                  && evalInteger finalEnd
                  then evalSlice expression finalBegin finalEnd
                  else throwE HiErrorInvalidArgument
              HiValueNull ->
                if denominator finalBegin == 1
                && evalInteger finalBegin
                then evalSlice expression finalBegin lengthExpr
                else throwE HiErrorInvalidArgument
              _ -> throwE HiErrorInvalidArgument
        HiValueNull ->
          case evalEnd of
            (HiValueNumber numberEnd) ->
              let finalEnd = evalTrueIndex lengthExpr numberEnd
              in
                if denominator finalEnd == 1
                && evalInteger finalEnd
                then evalSlice expression 0 finalEnd
                else throwE HiErrorInvalidArgument
            HiValueNull ->
              evalSlice expression 0 lengthExpr
            _ -> throwE HiErrorInvalidArgument
        _ -> throwE HiErrorInvalidArgument
    _ -> throwE HiErrorInvalidArgument
-- ^ If there is not 1 and 2 arguments - then throws arity mismatch error.
evalExceptT (HiExprApply (HiExprValue _) _) = throwE HiErrorArityMismatch
-- ^ If there is dictionary, then invokes helper evalDict function.
evalExceptT (HiExprDict dict) = evalDict dict
-- ^ If there is some expression - recursively calls itself with evaluated expression value.
evalExceptT (HiExprApply innerExpr arguments) = do
  innerExprResult <- evalExceptT innerExpr
  evalExceptT $ HiExprApply (HiExprValue innerExprResult) arguments

-- | Evaluate all of arguments.
-- If argument is number - then checks, if it in integers bound by invoking helper evalInteger function.
evalArguments :: HiMonad m => [HiExpr] -> ExceptT HiError m [HiValue]
evalArguments (a : b) = do
  evalA <- evalExceptT a
  F.foldlM
    (\array current -> do
      evalExpr <- evalExceptT current
      pure $ array ++ [evalExpr]
    ) [evalA] b
evalArguments [] = pure []

-- | Function for calculating addition.
-- Takes two arguments and invokes a addition helper function.
-- If there is not 2 arguments - throws arity mismatch error.
evalAdd :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalAdd [first, second] = addArguments first second
evalAdd _               = throwE HiErrorArityMismatch

-- | Helper function for addition.
addArguments :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
-- ^ If 2 received arguments is numbers - just adds first and second.
addArguments (HiValueNumber firstNumber) (HiValueNumber secondNumber) =
  pure . HiValueNumber $ firstNumber + secondNumber
-- ^ If 2 received arguments is strings - just concatenates them.
addArguments (HiValueString firstString) (HiValueString secondString) =
  pure . HiValueString $ T.pack $ T.unpack firstString ++ T.unpack secondString
-- ^ If 2 received arguments is lists - just concatenates them.
addArguments (HiValueList firstList) (HiValueList secondList) =
  pure . HiValueList $ firstList >< secondList
-- ^ If 2 received arguments is bytes - just concatenates them.
addArguments (HiValueBytes firstBytes) (HiValueBytes secondBytes) =
  pure . HiValueBytes $ C8.concat [firstBytes, secondBytes]
-- ^ If first argument is time, and second is number,
-- then adds to the time number amount of time.
addArguments (HiValueTime time) (HiValueNumber number) =
  pure . HiValueTime $ addUTCTime (realToFrac number) time
-- ^ Otherwise throws invalid arguments error.
addArguments _ _ = throwE HiErrorInvalidArgument

-- | Function for calculating subtraction.
-- Takes two arguments and invokes a subtraction helper function.
-- If there is not 2 arguments - throws arity mismatch error.
evalSub :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalSub [first, second] = subArguments first second
evalSub _               = throwE HiErrorArityMismatch

-- | Helper function for subtraction.
subArguments :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
-- ^ If 2 arguments is numbers - just subtracts second from the first.
subArguments (HiValueNumber first) (HiValueNumber second) =
  pure . HiValueNumber $ first - second
-- ^ If 2 arguments is times - subtracts secnd time from the first.
subArguments (HiValueTime firstTime) (HiValueTime secondTime) =
  pure . HiValueNumber . toRational $ diffUTCTime firstTime secondTime
-- ^ Otherwise returns invalid arguments error
subArguments _ _ = throwE HiErrorInvalidArgument

-- | Function for calculating multiplication.
-- Takes two argument and invokes a multiplication helper function.
-- If there is not 2 arguments - throws arity mismatch error.
evalMul :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalMul [first, second] = mulArguments first second
evalMul _               = throwE HiErrorArityMismatch

-- | Helper function for multiplication.
mulArguments :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
-- ^ If 2 arguments if numbers - just multiplies them.
mulArguments (HiValueNumber first) (HiValueNumber second) =
  pure . HiValueNumber $ first * second
-- ^ If first arguments is something except number, but second is number,
-- then checks,
-- if number > 0 and integer, then invokes 2nd helper function - mulStringListBytes.
-- else throws invalid arguments error.
mulArguments something (HiValueNumber number) =
  if number > 0 && denominator number == 1
  then mulStringsListBytes something $ truncate number
  else throwE HiErrorInvalidArgument
-- ^ If arguments if something else - throws invalid arguments error.
mulArguments _ _ = throwE HiErrorInvalidArgument

-- | Second helper function for multiplication.
-- Checks if first arguments is string, list or bytes,
-- then repeats this number times.
-- Else throws invalid arguments error.
mulStringsListBytes :: HiMonad m => HiValue -> Int -> ExceptT HiError m HiValue
mulStringsListBytes something number =
  case something of
    (HiValueString string) -> pure . HiValueString $ stimes number string
    (HiValueList list)     -> pure . HiValueList $ stimes number list
    (HiValueBytes bytes)   -> pure . HiValueBytes $ stimes number bytes
    _                      -> throwE HiErrorInvalidArgument

-- | Function for calculating the division.
-- Takes two arguments and invokes a division helper function.
-- If there is not 2 arguments - throws arity mismatch error.
evalDiv :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalDiv [first, second] = divArguments first second
evalDiv _               = throwE HiErrorArityMismatch

-- | Helper function for division.
divArguments :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
-- ^ If first argument is number, and second is 0 - throws divide by zero error.
divArguments (HiValueNumber _) (HiValueNumber 0) = throwE HiErrorDivideByZero
-- ^ If 2 arguments is numbers - just divides first by the second.
divArguments (HiValueNumber first) (HiValueNumber second) =
  pure . HiValueNumber $ first / second
-- ^ If 2 arguments is string - just concatenates them, adding '/' between.
divArguments (HiValueString firstString) (HiValueString secondString) =
  pure . HiValueString . T.pack $ T.unpack firstString ++ "/" ++ T.unpack secondString
-- ^ If arguments is something else - throws invalid arguments error.
divArguments _ _ = throwE HiErrorInvalidArgument

-- | Function for calculating not operation.
evalNot :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If received value is true - returns false
evalNot [HiValueBool True ] = pure $ HiValueBool False
-- ^ If received value is false - returns true
evalNot [HiValueBool False] = pure $ HiValueBool True
-- ^ If there is 1 arguments, but not boolean - throws invalid arguments error.
evalNot [_]                 = throwE HiErrorInvalidArgument
-- ^ Otherwise returns arity mismatch error.
evalNot _                   = throwE HiErrorArityMismatch

-- | Function for lazy calculation and operation.
evalAnd :: HiMonad m => [HiExpr] -> ExceptT HiError m HiValue
-- ^ If there are 2 arguments, then evaluates first,
-- then if there is false or null - just returns it.
-- Else returns evaluated second value.
evalAnd [first, second] = do
  evalFirst <- evalExceptT first
  case evalFirst of
    (HiValueBool False) -> pure evalFirst
    HiValueNull         -> pure evalFirst
    _                   -> evalExceptT second
-- ^ Otherwise returns arity mismatch error.
evalAnd _ = throwE HiErrorArityMismatch

-- | Function for lazy calculation or operation.
evalOr :: HiMonad m => [HiExpr] -> ExceptT HiError m HiValue
-- ^ If there are 2 arguments, then evaluates first,
-- then returns evaluated second.
-- Else returns evaluated first.
evalOr [first, second] = do
  evalFirst <- evalExceptT first
  case evalFirst of
    (HiValueBool False) -> evalExceptT second
    HiValueNull         -> evalExceptT second
    _                   -> pure evalFirst
-- ^ Otherwise returns arity mismatch error.
evalOr _ = throwE HiErrorArityMismatch

-- | Function for calculating less-than operation.
-- If there are 2arguments, then
-- just returns if first if less than second.
evalLessThan :: HiMonad m => [HiValue]  -> ExceptT HiError m HiValue
evalLessThan [a, b] = pure . HiValueBool $ a < b
-- ^ Otherwise throws arity mismatch error
evalLessThan _      = throwE HiErrorArityMismatch

-- | Function for calculating greater-than operation.
evalGreaterThan :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there are 2 arguments, then
-- just invokes evalLessThan passing arguments swapping them.
evalGreaterThan [first, second] = evalLessThan [second, first]
-- ^ Otherwise throws arity mismatch error.
evalGreaterThan _               = throwE HiErrorArityMismatch

-- | Function for calculating not-less-than operation.
evalNotLessThan :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ Just evaluate if first < second by invoking evalLessThan,
-- then invokes evalNot with result.
evalNotLessThan arguments = do
  evalLess <- evalLessThan arguments
  evalNot [evalLess]

-- | Function for not-greater-than operation.
evalNotGreaterThan:: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ Just evaluate if first > second by invoking evalGraterThan,
-- then invokes evalNot with result.
evalNotGreaterThan arguments = do
  evalGreater <- evalGreaterThan arguments
  evalNot [evalGreater]

-- | Function for calculating equals operation.
evalEquals :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there are 2 arguments, then just returns, if first == second.
evalEquals [first, second] = pure . HiValueBool $ first == second
-- ^ Otherwise throws arity mismatch error.
evalEquals _               = throwE HiErrorArityMismatch

-- | Function for calculating not-equals operation.
evalNotEquals:: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ Just evaluate if first == second by invoking evalNotEquals,
-- then invokes evalNot with result.
evalNotEquals arguments = do
  evalEq <- evalEquals arguments
  evalNot [evalEq]

-- | Function for lazy calculating if operation.
evalIf :: HiMonad m => [HiExpr] -> ExceptT HiError m HiValue
-- ^ If there are 3 arguments, then
-- evaluate first arguments.
-- If there is bool value, then
-- If true - return evaluate first.
-- If false - return evaluate second.
-- Else - return invalid arguments error.
evalIf [bool, first, second] = do
  evalBool <- evalExceptT bool
  case evalBool of
    (HiValueBool True)  -> evalExceptT first
    (HiValueBool False) -> evalExceptT second
    _                   -> throwE HiErrorInvalidArgument
-- ^ Otherwise throws invalid arguments error.
evalIf _ = throwE HiErrorArityMismatch

-- | Function for calculating length operation.
evalLength :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is string, list or bytes,
-- then just invokes helper evalLengthHelper function.
evalLength [HiValueString string] = evalLengthHelper T.length string
evalLength [HiValueList list]     = evalLengthHelper S.length list
evalLength [HiValueBytes bytes]   = evalLengthHelper BS.length bytes
-- ^ If there is no string, list or bytes - throws invalid arguments error
evalLength [_]                    = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalLength _                      = throwE HiErrorArityMismatch

-- | Helper function for length.
-- Just calculates string, list or bytes length via received function.
evalLengthHelper :: HiMonad m => (a -> Int) -> a -> ExceptT HiError m HiValue
evalLengthHelper lengthFunction something =
  pure . HiValueNumber. fromIntegral $ lengthFunction something

-- | Function for calculation to-upper operation.
evalToUpper :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 argument, and it is string - converts all letters to upper.
evalToUpper [HiValueString string] =
  pure . HiValueString $ T.toUpper string
-- ^ If 1 argument is not string - throws invalid arguments error.
evalToUpper [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalToUpper _   = throwE HiErrorArityMismatch

-- | Function for calculation to-lower operation.
evalToLower :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 argument, and it is string - converts all letters to lower.
evalToLower [HiValueString string] =
  pure . HiValueString $ T.toLower string
-- ^ If 1 arguments is not string - throws invalid arguments error.
evalToLower [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalToLower _   = throwE HiErrorArityMismatch

-- | Function for calculation reverse operation.
evalReverse :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 argument, and it is string, list or bytes - just reverse it.
evalReverse [HiValueString string] =
  pure . HiValueString $ T.reverse string
evalReverse [HiValueList list] =
  pure . HiValueList $ S.reverse list
evalReverse [HiValueBytes bytes] =
  pure . HiValueBytes $ BS.reverse bytes
-- ^ If 1 argument is not string,list or bytes - throws invalid arguments error.
evalReverse [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalReverse _   = throwE HiErrorArityMismatch

-- | Function for calculation trim operation.
evalTrim :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 arguments and it is string,
-- then just remove whitespaces from the beginning and the end.
evalTrim [HiValueString string] =
  pure . HiValueString $ T.strip string
-- ^ If 1 argument is not a string - throws invalid arguments error.
evalTrim [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalTrim _   = throwE HiErrorArityMismatch

-- | Function for calculation list operation.
-- Just convert received arguments to list.
evalList :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalList arguments = pure . HiValueList $ S.fromList arguments

-- | Function for calculation range operation.
evalRange :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there are 2 numbers, then just do range beginning with begin,
-- ending with the end.
evalRange [HiValueNumber begin, HiValueNumber end] =
     pure . HiValueList . S.fromList $ Prelude.map HiValueNumber [begin..end]
-- ^ If some of arguments is not number - throws invalid arguments error.
evalRange [_, _] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error
evalRange _      = throwE HiErrorArityMismatch

-- | Function for calculation fold operation.
evalFold :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If first arguments is function, and second is list,
-- then applies the function to the entire list in order.
evalFold [HiValueFunction function, HiValueList (h :<| t)] =
  foldlM
    (\acc current -> do
        evalExceptT
          (HiExprApply
              (HiExprValue (HiValueFunction function))
              [HiExprValue acc, HiExprValue current]
          )
    ) h t
-- ^ If list is empty, and first is function - returns null.
evalFold [HiValueFunction _, HiValueList _] = pure HiValueNull
-- ^ If 2 arguments is something else, then throws invalid arguments error
evalFold [_, _] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error
evalFold _ = throwE HiErrorArityMismatch

-- | Function for returns element/char by index in strings, list.
-- In the beginning checks, if index < 0 or >= length of data structure,
-- then returns null. Else return element by index.
evalIndex :: HiMonad m => HiValue -> Rational -> ExceptT HiError m HiValue
evalIndex (HiValueString string) ind = do
  if ind < 0 || truncate ind >= T.length string
  then pure HiValueNull
  else pure . HiValueString $ T.pack [T.index string (truncate ind)]
evalIndex (HiValueList list) ind = do
  if ind < 0 || truncate ind >= S.length list
  then pure HiValueNull
  else pure $ S.index list (truncate ind)
evalIndex (HiValueBytes bytes) ind = do
  evalBytes <- evalUnpackBytes [HiValueBytes bytes]
  evalIndex evalBytes ind
evalIndex _ _ = throwE HiErrorInvalidFunction

-- | Function for returns element in dictionary by index.
-- Just returns element by index or null, if there is no such key in dictionary.
evalDictIndex :: HiMonad m => MS.Map HiValue HiValue -> HiValue -> ExceptT HiError m HiValue
evalDictIndex dict ind = do
  case MS.lookup ind dict of
    (Just val) -> pure val
    Nothing    -> pure HiValueNull

-- | Function for evaluate slices in strings, lists or bytes.
-- Received already final indexes(advanced) and returns slice by them.
evalSlice :: HiMonad m => HiValue -> Rational -> Rational -> ExceptT HiError m HiValue
evalSlice (HiValueString string) begin end =
  pure . HiValueString . T.drop (truncate begin) $ T.take (truncate end) string
evalSlice (HiValueList list) begin end =
  pure . HiValueList . S.drop (truncate begin) $ S.take (truncate end) list
evalSlice (HiValueBytes bytes) begin end = do
  evalBytes      <- evalUnpackBytes [HiValueBytes bytes]
  evalSliceBytes <- evalSlice evalBytes begin end
  evalPackBytes [evalSliceBytes]
evalSlice _ _ _ = throwE HiErrorInvalidArgument

-- | Helper function for evaluate indexes for advanced modify.
-- If index < 0 then return length of data structure - |index|.
-- If index >= length of data structure, then returns this length.
-- Otherwise returns index.
evalTrueIndex :: Rational -> Rational -> Rational
evalTrueIndex lengthOf beginEndIndex = do
  if beginEndIndex < 0
  then toRational $ lengthOf - abs beginEndIndex
  else
    if beginEndIndex >= lengthOf
    then toRational lengthOf
    else beginEndIndex

-- | Function for calculation pack-bytes operation.
evalPackBytes :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is empty list - throws invalid arguments error.
evalPackBytes [HiValueList Empty] = throwE HiErrorInvalidArgument
-- ^ If arguments is list, then checks, if all elements is integers numbers and >= 0 and <= 255.
-- If yes, then just converts elements to bytes.
-- If no - throws invalid arguments error.
evalPackBytes [HiValueList list] =
  let
    flag = Prelude.foldl
      (\acc current ->
        case current of
          (HiValueNumber currentNumber) ->
            let
              denominatorCurrent = denominator currentNumber
            in
              (denominatorCurrent == 1
                && currentNumber >= 0
                && currentNumber <= 255) && acc
          _ -> False
      ) True list
  in
    if flag
    then
      pure . HiValueBytes . BS.pack . F.toList $ S.mapWithIndex
        (\_ x ->
          case x of
            (HiValueNumber xNumber) -> fromIntegral $ numerator xNumber
            _                       -> undefined
        ) list
    else throwE HiErrorInvalidArgument
-- ^ If arguments is not list - throws invalid arguments error.
evalPackBytes _ = throwE HiErrorInvalidArgument

-- | Function for calculation unpack-bytes operation.
evalUnpackBytes :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If argument is bytes then just converts it to list.
evalUnpackBytes [HiValueBytes bytes] =
  pure . HiValueList . S.fromList $ Prelude.map (HiValueNumber . toRational) (BS.unpack bytes)
-- ^ Otherwise throws invalid arguments error.
evalUnpackBytes _ = throwE HiErrorInvalidArgument

-- | Function for calculation encodeUtf8 operation.
evalEncodeUtf8 :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If argument is string then just convert string to bytes.
evalEncodeUtf8 [HiValueString string] =
  pure . HiValueBytes $ encodeUtf8 string
-- ^ Otherwise throws invalid arguments error.
evalEncodeUtf8 _ = throwE HiErrorInvalidArgument

-- | Function for calculation decodeUtf8 operation.
evalDecodeUtf8 :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If argument is bytes, then tries to decode them.
-- ^ If success, then return result string, else null.
evalDecodeUtf8 [HiValueBytes bytes] =
  case decodeUtf8' bytes of
    (Left  _        ) -> pure   HiValueNull
    (Right decodeStr) -> pure $ HiValueString decodeStr
-- ^ Otherwise throws invalid arguments error.
evalDecodeUtf8 _ = throwE HiErrorInvalidArgument

-- | Function for calculation zip operation.
evalZip :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If argument is bytes, then zip them with specify bestCompression.
evalZip [HiValueBytes bytes] =
  pure . HiValueBytes . toStrict .
    compressWith (defaultCompressParams { compressLevel = bestCompression }) $ fromStrict bytes
-- ^ Otherwise throws invalid arguments error.
evalZip _ = throwE HiErrorInvalidArgument

-- | Function for calculation unzip operation.
evalUnzip :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If argument is bytes, then just decompress bytes.
evalUnzip [HiValueBytes bytes] =
  pure . HiValueBytes . toStrict . decompressWith defaultDecompressParams $ fromStrict bytes
-- ^ Otherwise throws invalid arguments error.
evalUnzip _ = throwE HiErrorInvalidArgument

-- | Function for calculation serialise operation.
evalSerialise :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 argument, then just do serialise for this element.
evalSerialise [value] = pure . HiValueBytes . toStrict $ serialise value
-- ^ Otherwise throws arity mismatch error.
evalSerialise _       = throwE HiErrorArityMismatch

-- | Function for calculation deserialise operation.
evalDeserialise :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 argument and it is bytes, then just deserialise this bytes to something.
evalDeserialise [HiValueBytes bytes] = pure . deserialise $ fromStrict bytes
-- ^ if arguments is not bytes - throws invalid arguments error.
evalDeserialise [_]                  = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error
evalDeserialise _                    = throwE HiErrorArityMismatch

-- | Function for read operation.
evalRead :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 arguments and it is stirng, then just wraps it into (action -> read) wrapper.
evalRead [HiValueString string] =
  pure . HiValueAction . HiActionRead $ T.unpack string
-- ^ If arguments is not string - throws invalid arguments error.
evalRead [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismtach error.
evalRead _   = throwE HiErrorArityMismatch

-- | Function for write operation.
evalWrite :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there are 2 arguments, 1 is string, and 2 is string,
-- then wrap them into (action -> write) wrapper, and encodes string to bytes.
evalWrite [HiValueString file, HiValueString string] =
  pure . HiValueAction . HiActionWrite (T.unpack file) $ encodeUtf8 string
-- ^ If there are 2 arguments, 1 is string, and 2 is bytes,
-- then just wraps them into (action -> write) wrapper.
evalWrite [HiValueString file, HiValueBytes bytes] =
  pure . HiValueAction $ HiActionWrite (T.unpack file) bytes
-- ^ If 2 arguments is something else - throws invalid arguments error.
evalWrite [_, _] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalWrite _      = throwE HiErrorArityMismatch

-- | Function for mkdir operation.
evalMkDir :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 argument, and it is string,
-- then just wraps it into (action -> mkdir) wrapper.
evalMkDir [HiValueString string] =
  pure . HiValueAction . HiActionMkDir $ T.unpack string
-- ^ If 1 arguments and it is something else - throws invalid arguments error.
evalMkDir [_] = throwE HiErrorInvalidArgument
-- | Otherwise throws aroty mismatch error.
evalMkDir _   = throwE HiErrorArityMismatch

-- | Function for cd operation.
evalChDir :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 argument, and it is string,
-- then wraps it into (action -> chdir) wrapper.
evalChDir [HiValueString string] =
  pure . HiValueAction . HiActionChDir $ T.unpack string
-- ^ If 1 arguments and it is something else - throws invalid arguments error.
evalChDir [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws aroty mismatch error.
evalChDir _   = throwE HiErrorArityMismatch

-- | Function for parse-time operation.
evalParseTime :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 arguments, and it is string,
-- then try to convert it to UTCTime. If success,
-- then wraps it into time wrapper.
-- else returns null
evalParseTime [HiValueString time] =
  case readMaybe (T.unpack time) of
    Nothing           -> pure   HiValueNull
    (Just reallyTime) -> pure $ HiValueTime reallyTime
-- ^ If 1 arguments and it is something else - throws invalid arguments error.
evalParseTime [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws aroty mismatch error.
evalParseTime _   = throwE HiErrorArityMismatch

-- | Function for rand operation.
evalRand :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there are 2 arguments, and they are numbers, and int
-- then just wraps them into (action -> rand), and casts to Int.
-- If numbers is not int - throws invalid arguments error.
evalRand [HiValueNumber begin, HiValueNumber end] =
  if denominator begin == 1
  && denominator end == 1
  && evalInteger begin
  && evalInteger end
  then pure . HiValueAction $ HiActionRand (truncate begin) (truncate end)
  else throwE HiErrorInvalidArgument
-- ^ If 2 arguments is something else - throws invalid arguments error.
evalRand [_, _] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalRand _      = throwE HiErrorArityMismatch

-- | Function for echo operation.
evalEcho :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
-- ^ If there is 1 argument and it is string,
-- then just wraps it into (action -> echo) wrapper.
evalEcho [HiValueString string] =
  pure . HiValueAction $ HiActionEcho string
-- ^ If there is another argument - throws invalid arguments error.
evalEcho [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalEcho _   = throwE HiErrorArityMismatch

-- | Function for calculate dictionaries.
evalDict :: HiMonad m => [(HiExpr, HiExpr)] -> ExceptT HiError m HiValue
evalDict dict = do
  evalDictionary <- evalDictHelper dict
  pure . HiValueDict $ M.fromList evalDictionary

-- | Helper function for dictionary.
-- Just calculate key and value and do pair of them,
-- and do it for all elements.
evalDictHelper :: HiMonad m => [(HiExpr, HiExpr)] -> ExceptT HiError m [(HiValue, HiValue)]
evalDictHelper = mapM $ \(key, value) -> do
  evalKey <- evalExceptT key
  evalValue <- evalExceptT value
  pure (evalKey, evalValue)

-- | Function for calculate count operation.
-- Just counts, how many of each of the elements are present in the structure.
evalCount :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalCount [HiValueList list] =
  pure . HiValueDict $ MS.map HiValueNumber $ Prelude.foldl
    (\acc current -> MS.insertWith
      (+) current 1 acc)
    MS.empty
    list
evalCount [HiValueString string] =
  pure . HiValueDict $ MS.map HiValueNumber $ Prelude.foldl
    (\acc current -> MS.insertWith
      (+) current 1 acc)
    MS.empty
    (Prelude.map (\c -> HiValueString $ T.pack [c]) $ T.unpack string)
evalCount [HiValueBytes bytes] =
  pure . HiValueDict $ MS.map HiValueNumber $ Prelude.foldl
    (\acc current -> MS.insertWith
      (+) current 1 acc)
    MS.empty
    (Prelude.map (HiValueNumber . toRational . ord) $ C8.unpack bytes)
-- ^ If there is 1 argument and it is not list,string or bytes,
-- then throws invalid arguments error.
evalCount [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalCount _ = throwE HiErrorArityMismatch

-- | Function for calculate keys operation.
evalKeys :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalKeys [HiValueDict dict] =
  pure . HiValueList . S.fromList $ MS.keys dict
evalKeys [_] = throwE HiErrorInvalidArgument
evalKeys _   = throwE HiErrorArityMismatch

-- | Function for calculate values operation.
evalValues :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalValues [HiValueDict dict] =
  pure . HiValueList . S.sort $ S.fromList $ MS.elems dict
evalValues [_] = throwE HiErrorInvalidArgument
evalValues _   = throwE HiErrorArityMismatch

-- | Function for calculate invert operation.
-- Just swaps keys and values in dictionaries.
-- New values is lists.
evalInvert :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalInvert [HiValueDict dict] =
  pure . HiValueDict . MS.map (HiValueList . S.fromList) $ Prelude.foldl
   (\acc (key, value) -> MS.insertWith
     (++) value [key] acc)
   MS.empty
   (MS.assocs dict)
-- ^ If there is 1 argument, and it is not dictionary,
-- then throws invalid arguments error.
evalInvert [_] = throwE HiErrorInvalidArgument
-- ^ Otherwise throws arity mismatch error.
evalInvert _ = throwE HiErrorArityMismatch

-- | Helper function for checks if number in integer bounds.
evalInteger :: Rational -> Bool
evalInteger number =
  number >= toRational (minBound ::Int) && number <= toRational (maxBound ::Int)
