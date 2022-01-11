{-# LANGUAGE DerivingStrategies #-}
{-# LANGUAGE InstanceSigs       #-}

module HW3.Action where

import Control.Exception.Base (Exception, throwIO)
import Control.Monad          (ap, void)
import Data.ByteString        as BS (readFile, writeFile)
import Data.Sequence          as S (fromList)
import Data.Set.Internal      (Set, member)
import Data.Text              as T (pack, unpack)
import Data.Text.Encoding     (decodeUtf8')
import Data.Time
import HW3.Base               (HiAction (..), HiMonad (..),
                                         HiValue (..))
import System.Directory       (createDirectory, doesFileExist,
                                         getCurrentDirectory, listDirectory,
                                         setCurrentDirectory)
import System.Random          (getStdRandom, uniformR)

data HiPermission =
    AllowRead
  | AllowWrite
  | AllowTime
  deriving (Show, Ord, Eq)
  deriving (Enum, Bounded)

data PermissionException =
  PermissionRequired HiPermission deriving Show

instance Exception PermissionException

newtype HIO a = HIO { runHIO :: Set HiPermission -> IO a }

instance Functor HIO where
  fmap f a = HIO $ fmap f . runHIO a

instance Applicative HIO where
  pure a = HIO $ \_ -> pure a
  p <*> q = ap p q

instance Monad HIO where
  m >>= f = HIO $ \x -> do
    r <- runHIO m x
    runHIO (f r) x
  return x = HIO $ const $ return x

-- | Instance for HiMonad.
-- Has 1 function - runAction, which is invokes in evaluator,
-- when it gets valueAction.
-- Checks for each event if it has the appropriate permission.
instance HiMonad HIO where
  runAction :: HiAction -> HIO HiValue
  runAction hio =
    case hio of
      -- If action is read - firstly checks for the existence of a file in the path.
      -- If there is no file, returns list of directories by path.
      -- If file exists, reads text in it and tries to decode it to utf8.
      -- If decode is success, returns decode string, else just file text.
      HiActionRead path -> HIO $ \x ->
        if member AllowRead x
        then do
          fileExist <- doesFileExist path
          if fileExist
          then do
            text <- BS.readFile path
            let
              decodedText = decodeUtf8' text
            case decodedText of
              (Right string) -> pure $ HiValueString string
              _              -> pure $ HiValueBytes text
          else do
            listDir <- listDirectory path
            pure . HiValueList . S.fromList $ Prelude.map (HiValueString . T.pack) listDir
        else throwIO $ PermissionRequired AllowRead
      -- If action is write - write some text into file.
      HiActionWrite path string -> HIO $ \x ->
        if member AllowWrite x
        then do
          void $ BS.writeFile path string
          pure HiValueNull
        else throwIO $ PermissionRequired AllowWrite
      -- If action is mkdir - create directory by given path.
      HiActionMkDir path -> HIO $ \x ->
        if member AllowWrite x
        then do
          void $ createDirectory path
          pure HiValueNull
        else throwIO $ PermissionRequired AllowWrite
      -- If action is cd - sets current directory to path.
      HiActionChDir path -> HIO $ \x ->
        if member AllowRead x
        then do
          void $ setCurrentDirectory path
          pure HiValueNull
        else throwIO $ PermissionRequired AllowRead
      -- If action is cwd - returns current directory.
      HiActionCwd -> HIO $ \x ->
        if member AllowRead x
        then do
          curDir <- getCurrentDirectory
          pure . HiValueString $ T.pack curDir
        else throwIO $ PermissionRequired AllowRead
      -- If action is now - returns current time.
      HiActionNow -> HIO $ \x ->
        if member AllowTime x
        then do
          curTime <- getCurrentTime
          pure . HiValueTime $ curTime
        else throwIO $ PermissionRequired AllowTime
      -- If action is rand - return a random number from the first to the second border.
      HiActionRand begin end -> HIO $ \_ -> do
        getRand <- getStdRandom (uniformR (begin, end))
        pure . HiValueNumber $ toRational getRand
      -- If action is echo - outputs the passed string.
      HiActionEcho string -> HIO $ \x ->
        if member AllowWrite x
        then do
          void $ Prelude.putStrLn $ T.unpack string
          pure HiValueNull
        else throwIO $ PermissionRequired AllowWrite
