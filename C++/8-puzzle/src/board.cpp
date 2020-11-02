#include "../include/board.h"
#include <set>
#include <ctime>
#include <random>

Board Board::create_goal(const unsigned size) {
    std::vector<std::vector<unsigned>> goal(size, std::vector<unsigned>(size));
    for (size_t i = 0; i < size; i++) {
        for (size_t j = 0; j < size; j++) {
            goal[i][j] = i * size + j + 1;
        }
    }
    goal[size - 1][size - 1] = 0;
    return Board(goal);
}

Board::Board(const unsigned size) {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dis(0, size * size - 1);
    square.resize(size, std::vector<unsigned>(size));
    std::set<unsigned> used;
    unsigned ch;
    for (size_t i = 0; i < size; i++) {
        for (size_t j = 0; j < size; j++) {
            ch = dis(gen);
            unsigned set_size = used.size();
            used.insert(ch);
            while (set_size == used.size()) {
                ch = dis(gen);
                used.insert(ch);
            }
            square[i][j] = ch;
        }
    }
}

Board::Board(const std::vector<std::vector<unsigned>> &data) : square(data){

}

std::size_t Board::size() const {
    return square.size();
}

bool Board::is_goal() const {
    for (size_t i = 0; i < size(); i++) {
        for (size_t j = 0; j < size(); j++) {
            if (square[i][j] == 0) {
                if (i != size() - 1 || j != size() - 1) {
                    return false;
                }
            } else {
                if (square[i][j] != i * size() + j + 1) {
                    return false;
                }
            }
        }
    }
    return true;
}

unsigned Board::hamming() const {
    unsigned wrong_place = 0;
    for (size_t i = 0; i < size(); i++) {
        for (size_t j = 0; j < size(); j++) {
            if (square[i][j] == 0) {
                if (i != size() - 1 || j != size() - 1) {
                    wrong_place++;
                }
            } else {
                if (square[i][j] != i * size() + j + 1) {
                    wrong_place++;
                }
            }
        }
    }
    return wrong_place;
}

unsigned Board::manhattan() const {
    unsigned manhattan = 0;
    for (size_t i = 0; i < size(); i++) {
        for (size_t j = 0; j < size(); j++) {
            if (square[i][j] != 0) {
                manhattan += std::max((square[i][j] - 1) / size(), i) - std::min((square[i][j] - 1) / size(), i)
                             + std::max((square[i][j] - 1) % size(), j) - std::min((square[i][j] - 1) % size(), j);
            }
        }
    }
    return manhattan;
}

std::string Board::to_string() const {
    std::string str;
    for (size_t i = 0; i < size(); i++) {
        for (size_t j = 0; j < size(); j++) {
            str += std::to_string(square[i][j]) + " ";
            if (j == size() - 1) {
                str += "\n";
            }
        }
    }
    return str;
}

bool Board::is_solvable() const {
    if (size() < 2) {
        return true;
    }
    unsigned result = 0;
    for (size_t i = 0; i < size(); i++) {
        for (size_t j = 0; j < size(); j++) {
            if (square[i][j] == 0) {
                if (size() % 2 == 0) {
                    result += i + 1;
                }
                continue;
            }
            for (size_t q = i * size() + j; q < size() * size(); q++) {
                if (square[q / size()][q % size()] < square[i][j] && square[q / size()][q % size()] != 0) {
                    result++;
                }
            }
        }
    }
    return result % 2 == 0;
}

const std::vector<unsigned> &Board::operator[](const std::size_t i) const {
    return square[i];
}

void Board::swap_zero(size_t zero_x, size_t zero_y, size_t new_x, size_t new_y) {
    std::swap(square[zero_x][zero_y], square[new_x][new_y]);
}