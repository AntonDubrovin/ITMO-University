cmake_minimum_required(VERSION 3.17)
project(seam_carving)

set(CMAKE_CXX_STANDARD 14)

add_executable(seam_carving src/main.cpp src/Image.cpp src/SeamCarver.cpp include/Image.h include/SeamCarver.h)