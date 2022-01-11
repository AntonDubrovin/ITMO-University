module HW3.Pretty
  ( prettyValue
  ) where

import Data.ByteString               as BS (ByteString, length)
import Data.ByteString.Char8         as C8 (unpack)
import Data.Char                     (ord)
import Data.Map                      (Map, assocs)
import Data.Maybe                    (isNothing)
import Data.Ratio                    (denominator, numerator)
import Data.Scientific               (FPFormat (Fixed),
                                                formatScientific,
                                                fromRationalRepetendUnlimited)
import Data.Sequence                 as S (Seq, length, mapWithIndex)
import Data.Text                     as T (Text, unpack)
import Data.Time.Clock               (UTCTime)
import HW3.Base                      (HiAction (..), HiFun (..),
                                                HiValue (..))
import Numeric                       (showHex)
import Prettyprinter                 (Doc, pretty)
import Prettyprinter.Render.Terminal (AnsiStyle)

-- | A callable function fot pretty output.
-- Just invokes makePretty from value.
prettyValue :: HiValue -> Doc AnsiStyle
prettyValue value = pretty $ makePretty value

-- | Function for pretty output by types.
-- Just invokes helper function for all types.
makePretty :: HiValue -> String
makePretty (HiValueNumber number)     = makePrettyNumber number
makePretty (HiValueBool boolean)      = makePrettyBoolean boolean
makePretty (HiValueFunction function) = makePrettyFunction function
makePretty HiValueNull                = makePrettyNull
makePretty (HiValueString string)     = makePrettyString string
makePretty (HiValueList list)         = makePrettyList list
makePretty (HiValueBytes bytes)       = makePrettyBytes bytes
makePretty (HiValueAction action)     = makePrettyAction action
makePretty (HiValueTime time)         = makePrettyTime time
makePretty (HiValueDict dict)         = makePrettyDict dict

-- | Helper function for output numbers.
-- If the number is integer - just outputs it.
-- Else checks, is it possible to write a number as a decimal using a dot.
-- If yes, then outputs it.
-- Else displays number as wholePart '-'/'+' remainder '/' denominator.
makePrettyNumber :: Rational -> String
makePrettyNumber number =
  let numeratorValue   = numerator number
      denominatorValue = denominator number
   in
     if denominatorValue == 1
     then show numeratorValue
     else
       let
         (result, decimal) = fromRationalRepetendUnlimited number
       in
         if isNothing decimal
         then formatScientific Fixed Nothing result
         else
           let
             (wholePart, remainder) = quotRem numeratorValue denominatorValue
           in
             if wholePart == 0
             then show numeratorValue ++ "/" ++ show denominatorValue
             else
               if remainder < 0
               then show wholePart ++ " - " ++ show (abs remainder) ++ "/" ++ show denominatorValue
               else show wholePart ++ " + " ++ show remainder ++ "/" ++ show denominatorValue

-- | Helper function for output booleans.
-- If boolean is True - outputs "true", else "false".
makePrettyBoolean :: Bool -> String
makePrettyBoolean boolean =
  if boolean
  then "true"
  else "false"

-- |  Helper function for output functions.
-- Outputs the corresponding function name from its wrapper.
makePrettyFunction :: HiFun -> String
makePrettyFunction function =
  case function of
     HiFunAdd            -> "add"
     HiFunSub            -> "sub"
     HiFunMul            -> "mul"
     HiFunDiv            -> "div"
     HiFunAnd            -> "and"
     HiFunOr             -> "or"
     HiFunLessThan       -> "less-than"
     HiFunGreaterThan    -> "greater-than"
     HiFunEquals         -> "equals"
     HiFunNotLessThan    -> "not-less-than"
     HiFunNotGreaterThan -> "not-greater-than"
     HiFunNotEquals      -> "not-equals"
     HiFunNot            -> "not"
     HiFunIf             -> "if"
     HiFunLength         -> "length"
     HiFunToUpper        -> "to-upper"
     HiFunToLower        -> "to-lower"
     HiFunReverse        -> "reverse"
     HiFunTrim           -> "trim"
     HiFunList           -> "list"
     HiFunRange          -> "range"
     HiFunFold           -> "fold"
     HiFunPackBytes      -> "pack-bytes"
     HiFunUnpackBytes    -> "unpack-bytes"
     HiFunEncodeUtf8     -> "encode-utf8"
     HiFunDecodeUtf8     -> "decode-utf8"
     HiFunZip            -> "zip"
     HiFunUnzip          -> "unzip"
     HiFunSerialise      -> "serialise"
     HiFunDeserialise    -> "deserialise"
     HiFunRead           -> "read"
     HiFunWrite          -> "write"
     HiFunMkDir          -> "mkdir"
     HiFunChDir          -> "cd"
     HiFunParseTime      -> "parse-time"
     HiFunRand           -> "rand"
     HiFunEcho           -> "echo"
     HiFunCount          -> "count"
     HiFunKeys           -> "keys"
     HiFunValues         -> "values"
     HiFunInvert         -> "invert"

-- |  Helper function for output nulls.
-- Just outputs "null".
makePrettyNull :: String
makePrettyNull =  "null"

-- | Helper function for output strings.
-- Just outputs received string.
makePrettyString :: Text -> String
makePrettyString string = show $ T.unpack string

-- | Helper function for output lists.
-- If length of received list if 0,
-- then outputs "[ ]".
-- Else outputs "[ ", then elements of list separated by commas, and then " ]".
makePrettyList :: Seq HiValue -> String
makePrettyList list =
  case S.length list of
    0 -> "[ ]"
    _ ->
      "[ "
        ++ Prelude.foldl1
          (\acc current -> acc ++ ", " ++ current)
          (S.mapWithIndex (\_ curr -> makePretty curr) list)
        ++ " ]"

-- | Helper function for output bytes.
-- If length of bytes if 0,
-- then outputs "[# #]".
-- Else outputs "[# ", and then outputs elements.
-- If length of element is 1 instead of 2, then added 0 to the beginning of the element.
-- In the end outputs " #]".
makePrettyBytes :: ByteString -> String
makePrettyBytes bytes =
  case BS.length bytes of
    0 -> "[# #]"
    _ ->
      "[# "
        ++ Prelude.foldl1
          (\acc current -> acc ++ " " ++ current)
          (Prelude.map (\x ->
          let res = showHex (ord x) ""
          in
            if Prelude.length res == 1
            then "0" ++ res
            else res) (C8.unpack bytes))
      ++ " #]"

-- | Helper function for output actions.
-- Just outputs what was received.
makePrettyAction :: HiAction -> String
makePrettyAction action =
  case action of
    (HiActionRead readValue) -> "read(\"" ++ readValue ++ "\")"
    (HiActionWrite writeValue stringValue) ->
      "write(\"" ++ writeValue ++ "\", " ++ makePretty (HiValueBytes stringValue) ++ ")"
    (HiActionMkDir mkdirValue) -> "mkdir(\"" ++ mkdirValue ++ "\")"
    (HiActionChDir chdirValue) -> "cd(\"" ++ chdirValue ++ "\")"
    HiActionCwd -> "cwd"
    HiActionNow -> "now"
    HiActionRand begin end -> "rand(" ++ show begin ++ ", " ++ show end ++ ")"
    HiActionEcho string -> "echo(" ++ show string ++ ")"

-- | Helper function for output time.
-- Just outputs "parse-time("x")", where x is received time.
makePrettyTime :: UTCTime -> String
makePrettyTime time = "parse-time(\"" ++ show time ++ "\")"

-- | Helper function for output dictionaries.
-- If length of dictionary is 0, then outputs { }.
-- Else outputs {, then outputs keys and values,
-- with : between them.
makePrettyDict :: Map HiValue HiValue -> String
makePrettyDict dict =
  case Prelude.length dict of
    0 -> "{ }"
    _ ->
      "{ "
      ++ Prelude.foldl1
      (\acc current -> acc ++ ", " ++ current)
      (Prelude.map (\(k, v) -> makePretty k ++ ": " ++ makePretty v)
      $ assocs dict)
      ++ " }"
