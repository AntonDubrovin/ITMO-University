#!/bin/bash

./build.sh
./startRMI.sh
./startServer.sh 
./runClient.sh Anton Dubrovin 9216 228 100
