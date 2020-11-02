#include <vector>
#include <queue>
#include "../include/Percolation.h"


Percolation::Percolation(size_t dimension) : open_cells(0) {
    // Write your logic here
    table.resize(dimension);
    for (size_t i = 0; i < dimension; i++) {
        table[i].resize(dimension, Cell::CLOSE);
    }
}

bool Percolation::is_percolate(size_t row, size_t column) const {
    std::vector<std::vector<bool>> used(table.size(), std::vector<bool>(table.size(), false));
    std::queue<std::pair<size_t, size_t>> q;
    q.push(std::make_pair(row, column));
    used[row][column] = true;
    while (!q.empty()) {
        std::pair<size_t, size_t> x = q.front();
        q.pop();
        used[x.first][x.second] = true;
        if (x.first == 0) {
            return true;
        }
        if (x.first < table.size() - 1 && !used[x.first + 1][x.second] && table[x.first + 1][x.second] == Cell::OPEN) {
            q.push(std::make_pair(x.first + 1, x.second));
        }
        if (x.second > 0 && !used[x.first][x.second - 1] && table[x.first][x.second - 1] == Cell::OPEN) {
            q.push(std::make_pair(x.first, x.second - 1));
        }
        if (x.second < table.size() - 1 && !used[x.first][x.second + 1] && table[x.first][x.second + 1] == Cell::OPEN) {
            q.push(std::make_pair(x.first, x.second + 1));
        }
        if (!used[x.first - 1][x.second] && table[x.first - 1][x.second] == Cell::OPEN) {
            q.push(std::make_pair(x.first - 1, x.second));
        }
    }
    return false;
}

void Percolation::open(size_t row, size_t column) {
    // Write your logic here
    if (!is_open(row, column)) {
        table[row][column] = Cell::OPEN;
        open_cells++;
    }
}

bool Percolation::is_open(size_t row, size_t column) const {
    // Write your logic here
    return table[row][column] == Cell::OPEN;
}

bool Percolation::is_full(size_t row, size_t column) const {
    // Write your logic here
    return table[row][column] == Cell::FULL;
}

size_t Percolation::get_numbet_of_open_cells() const {
    // Write your logic here
    return open_cells;
}

bool Percolation::has_percolation() const {
    // Write your logic here
    for (size_t column = 0; column < table.size(); column++) {
        if (is_open(table.size() - 1, column)) {
            if (is_percolate(table.size() - 1, column)) {
                return true;
            }
        }
    }
    return false;
}