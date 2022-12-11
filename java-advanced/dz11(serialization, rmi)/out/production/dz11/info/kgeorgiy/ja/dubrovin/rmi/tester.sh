#!/bin/bash

java -cp ../../../../../../lib/junit-4.13.1.jar:../../../../../../lib/hamcrest-core-1.3.jar:classes org.junit.runner.JUnitCore info.kgeorgiy.ja.dubrovin.rmi.ServerTest

java -cp ../../../../../../lib/junit-4.13.1.jar:../../../../../../lib/hamcrest-core-1.3.jar:classes org.junit.runner.JUnitCore info.kgeorgiy.ja.dubrovin.rmi.ClientTest
