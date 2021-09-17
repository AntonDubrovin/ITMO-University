#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/(\()([^)]+)(\))/$1$3/g;
    print;
}