#pragma once

#include <string>
#include <vector>
#include <map>


class Board {
public:
    void swap_zero(size_t zero_x, size_t zero_y, size_t new_x, size_t new_y);

    static Board create_goal(unsigned size);

    Board() = default;

    Board(const Board &other) = default;

    Board &operator=(const Board &other) = default;

    explicit Board(unsigned size);

    explicit Board(const std::vector<std::vector<unsigned>> &data);

    std::size_t size() const;

    bool is_goal() const;

    unsigned hamming() const;

    unsigned manhattan() const;

    std::string to_string() const;

    bool is_solvable() const;

    friend bool operator==(const Board &lhs, const Board &rhs) {
        return lhs.square == rhs.square;
    }

    friend bool operator!=(const Board &lhs, const Board &rhs) {
        return !(lhs == rhs);
    }

    friend bool operator >(const Board &lhs, const Board &rhs) {
        if (lhs.size() != rhs.size()) {
            return false;
        } else {
            for (size_t i = 0; i < lhs.size(); i++) {
                for (size_t j = 0; j < lhs[i].size(); j++) {
                    if (lhs[i][j] > rhs[i][j]) {
                        return true;
                    } else if (lhs[i][j] < rhs[i][j]) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    friend bool operator <(const Board &lhs, const Board &rhs) {
        if (lhs.size() != rhs.size()) {
            return false;
        } else {
            for (size_t i = 0; i < lhs.size(); i++) {
                for (size_t j = 0; j < lhs[i].size(); j++) {
                    if (lhs[i][j] < rhs[i][j]) {
                        return true;
                    } else if (lhs[i][j] > rhs[i][j]) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    const std::vector<unsigned> &operator[](std::size_t i) const;

    friend std::ostream &operator<<(std::ostream &out, const Board &board) { return out << board.to_string(); }

private:
    std::vector<std::vector<unsigned>> square;
};