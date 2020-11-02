#include "../include/solver.h"

#include <iostream>
#include <ctime>


int main()
{
    srand(time(nullptr));
    Board board(2);
    std::cout << board << std::endl;
    Solver solver(board);
    std::cout << solver.moves() << std::endl;
    for (auto move : solver) {
        std::cout << move << std::endl;
    }

}