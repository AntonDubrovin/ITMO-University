cmake_minimum_required(VERSION 3.17)
project(percolation)

set(CMAKE_CXX_STANDARD 14)

add_executable(percolation src/main.cpp include/Percolation.h include/PercolationStats.h src/Percolation.cpp src/PercolationStats.cpp)