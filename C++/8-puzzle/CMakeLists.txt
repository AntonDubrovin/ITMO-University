cmake_minimum_required(VERSION 3.17)
project(8_puzzle)

set(CMAKE_CXX_STANDARD 14)

add_executable(8_puzzle src/main.cpp include/board.h include/solver.h src/board.cpp src/solver.cpp)