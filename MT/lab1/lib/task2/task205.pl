#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/(\w)(\w)(\w*)/$2$1$3/g;
    print;
}