#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/\b([Aa]+)\b/argh/;
    print;
}