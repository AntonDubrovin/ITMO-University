#include "../include/solver.h"
#include <algorithm>

Solver::Solver(const Board &board) {
    if (board.size() < 2 || board.is_goal()) {
        m_moves.push_back(board);
    } else {
        if (board.is_solvable()) {
            queue.emplace(0, board);
            range[board] = 0;
            while (!queue.empty()) {
                current = queue.begin()->second;
                queue.erase(queue.begin());

                if (current.is_goal()) {
                    while (current != board) {
                        m_moves.push_back(current);
                        current = parent[current];
                    }
                    m_moves.push_back(current);
                    reverse(m_moves.begin(), m_moves.end());
                    break;
                }

                size_t zero_x = 0, zero_y = 0;
                for (size_t i = 0; i < current.size(); i++) {
                    for (size_t j = 0; j < current.size(); j++) {
                        if (current[i][j] == 0) {
                            zero_x = i;
                            zero_y = j;
                            break;
                        }
                    }
                }

                if (zero_x > 0) {
                    table_execute(zero_x, zero_y, zero_x - 1, zero_y);
                }

                if (zero_y > 0) {
                    table_execute(zero_x, zero_y, zero_x, zero_y - 1);
                }

                if (zero_x < current.size() - 1) {
                    table_execute(zero_x, zero_y, zero_x + 1, zero_y);
                }

                if (zero_y < current.size() - 1) {
                    table_execute(zero_x, zero_y, zero_x , zero_y + 1);
                }
            }
        }
    }
}

void Solver::table_execute(size_t zero_x, size_t zero_y, size_t new_x, size_t new_y) {
    Board tmp(current);
    tmp.swap_zero(zero_x, zero_y, new_x, new_y);
    unsigned *range_tmp = &range[tmp];
    unsigned range_cur = range[current];
    if (*range_tmp == 0 || *range_tmp > range_cur + 1) {
        parent[tmp] = current;
        *range_tmp = range_cur + 1;
        queue.emplace(range_cur + 1 + current.manhattan(), tmp);
    }
}


std::size_t Solver::moves() const {
    return std::max(static_cast<int>(m_moves.size() - 1), 0);
}