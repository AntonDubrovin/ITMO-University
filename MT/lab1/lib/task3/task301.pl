#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

use constant false => 0;
use constant true => 1;

my $result = "";
my $current;
my $startFlag = true;
my $betweenFlag = false;
while (<>) {
    $current = $_;
    if ($startFlag) {
        if (s/^\s+$//) {
        }
        else {
            $startFlag = false;
        }
    }
    if (!$startFlag) {
        if (s/^\s+$//) {
            $betweenFlag = true;
        }
        else {
            if ($betweenFlag) {
                $result = $result . "\n";
                $betweenFlag = false;
            }
            $current =~ s/^(\s+)(\S+)(.*)$/$2$3/;
            $current =~ s/^(.*)(\S+)(\s+)$/$1$2/;
            $current =~ s/(\s+)/ /g;
            $result = $result . $current . "\n";
        }
    }
}

print($result);