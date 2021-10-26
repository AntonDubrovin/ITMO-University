module HW1.T1
  ( Day (..)
  , afterDays
  , daysToParty
  , isWeekend
  , nextDay
  ) where

import GHC.Natural (Natural)

data Day
  = Monday
  | Tuesday
  | Wednesday
  | Thursday
  | Friday
  | Saturday
  | Sunday
  deriving Show

nextDay :: Day -> Day
nextDay day =
  case day of
    Monday    -> Tuesday
    Tuesday   -> Wednesday
    Wednesday -> Thursday
    Thursday  -> Friday
    Friday    -> Saturday
    Saturday  -> Sunday
    Sunday    -> Monday

afterDays :: Natural -> Day -> Day
afterDays 0 day = day
afterDays n day = afterDays (n - 1) $ nextDay day

isWeekend :: Day -> Bool
isWeekend Saturday = True
isWeekend Sunday   = True
isWeekend _        = False

daysToParty :: Day -> Natural
daysToParty day = _daysToParty day 0
  where
    _daysToParty :: Day -> Natural -> Natural
    _daysToParty Friday n = n
    _daysToParty _day   n = _daysToParty (nextDay _day) $ n + 1

