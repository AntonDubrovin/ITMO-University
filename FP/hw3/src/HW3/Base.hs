{-# LANGUAGE DeriveAnyClass     #-}
{-# LANGUAGE DeriveGeneric      #-}
{-# LANGUAGE DerivingStrategies #-}

module HW3.Base
  ( HiAction(..)
  , HiError(..)
  , HiExpr(..)
  , HiFun(..)
  , HiValue(..)
  , HiMonad(..)
  ) where

import Codec.Serialise       (Serialise)
import Data.ByteString.Char8 (ByteString)
import Data.Map.Internal     (Map)
import Data.Sequence         (Seq)
import Data.Text             (Text)
import Data.Time             (UTCTime)
import GHC.Generics          (Generic)

class Monad m => HiMonad m where
  runAction :: HiAction -> m HiValue

-- | Data about our actions.
data HiAction =
  -- Task 7: File I/O.
    HiActionRead  FilePath
  | HiActionWrite FilePath ByteString
  | HiActionMkDir FilePath
  | HiActionChDir FilePath
  | HiActionCwd
  -- Task 8: Date and time.
  | HiActionNow
  -- Task 9: Random numbers.
  | HiActionRand Int Int
  -- Task 10: Short-circuit evaluation.
  | HiActionEcho Text
  deriving (Show, Ord, Eq)
  deriving stock (Generic)
  deriving anyclass (Serialise)

-- | Data about our functions.
data HiFun =
  -- Task 1: Numbers and arithmetic.
    HiFunDiv
  | HiFunMul
  | HiFunAdd
  | HiFunSub
  -- Task 2: Booleans and comparison.
  | HiFunNot
  | HiFunAnd
  | HiFunOr
  | HiFunLessThan
  | HiFunGreaterThan
  | HiFunEquals
  | HiFunNotLessThan
  | HiFunNotGreaterThan
  | HiFunNotEquals
  | HiFunIf
  -- Task 4: Strings and slices.
  | HiFunLength
  | HiFunToUpper
  | HiFunToLower
  | HiFunReverse
  | HiFunTrim
  -- Task 5: Lists and folds.
  | HiFunList
  | HiFunRange
  | HiFunFold
  -- Task 6: Bytes and serialisation.
  | HiFunPackBytes
  | HiFunUnpackBytes
  | HiFunEncodeUtf8
  | HiFunDecodeUtf8
  | HiFunZip
  | HiFunUnzip
  | HiFunSerialise
  | HiFunDeserialise
  -- Task 7: File I/O.
  | HiFunRead
  | HiFunWrite
  | HiFunMkDir
  | HiFunChDir
  -- Task 8: Date and time.
  | HiFunParseTime
  -- Task 9: Random numbers.
  | HiFunRand
  -- Task 10: Short-circuit evaluation.
  | HiFunEcho
  -- Task 11: Dictionaries.
  | HiFunCount
  | HiFunKeys
  | HiFunValues
  | HiFunInvert
  deriving (Show, Ord, Eq)
  deriving stock (Generic)
  deriving anyclass (Serialise)

-- | Data about our values.
data HiValue =
  -- Task 6: Bytes and serialisation.
  HiValueBytes ByteString
  -- Task 5: Lists and folds.
  | HiValueList (Seq HiValue)
  -- Task 4: Strings and slices.
  | HiValueString Text
  | HiValueNull
  -- Task 2: Booleans and comparison.
  |  HiValueBool Bool
  -- Task 1: Numbers and arithmetic.
  | HiValueFunction HiFun
  | HiValueNumber Rational
  -- Task 7: File I/O.
  | HiValueAction HiAction
  -- Task 8: Date and time.
  | HiValueTime UTCTime
  -- Task 11: Dictionaries.
  | HiValueDict (Map HiValue HiValue)
  deriving (Show, Ord, Eq)
  deriving stock (Generic)
  deriving anyclass (Serialise)

-- | Data about our expressions.
data HiExpr =
  -- Task 1: Numbers and arithmetic.
    HiExprValue HiValue
  | HiExprApply HiExpr [HiExpr]
  -- Task 7: File I/O.
  | HiExprRun HiExpr
  -- Task 11: Dictionaries.
  | HiExprDict [(HiExpr, HiExpr)]
  deriving (Show, Eq)

-- | Data about our errors.
data HiError =
  -- Task 1: Numbers and arithmetic.
    HiErrorInvalidArgument
  | HiErrorInvalidFunction
  | HiErrorArityMismatch
  | HiErrorDivideByZero
  deriving (Show, Eq)
  deriving stock (Generic)
  deriving anyclass (Serialise)
