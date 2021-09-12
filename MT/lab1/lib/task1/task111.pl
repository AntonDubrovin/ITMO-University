#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

#2 ones contract or two 1 with even zeroes between
#3 ones with odd zeroes between
#combinations
while (<>) {
    print if /^(1(01*0)*1|0*)*$/;
}