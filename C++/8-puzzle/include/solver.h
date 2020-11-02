#pragma once

#include "board.h"
#include <map>
#include <set>


class Solver
{
public:
    explicit Solver(const Board & board);

    Solver(const Solver & other) = default;

    Solver & operator = (const Solver & other) = default;

    std::size_t moves() const;

    auto begin() const
    { return m_moves.begin(); }

    auto end() const
    { return m_moves.end(); }

private:
    // FIXME
    std::vector<Board> m_moves;
    std::map<Board, unsigned> range;
    std::set<std::pair<unsigned, Board>> queue;
    void table_execute(size_t zero_x, size_t zero_y, size_t new_x, size_t new_y);
    std::map<Board, Board> parent;
    Board current;
};