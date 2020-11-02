#pragma once

#include <cstdio>
#include <vector>

struct PercolationStats {
public:
    /**
     * Construct a new Percolation Stats object
     * @param dimension dimension of percolation grid
     * @param trials amount of experiments
     */
    PercolationStats(size_t dimension, size_t trials);

    /**
     * Returns mean of percolation threshold (x¯ from description)
     */
    double get_mean() const;

    /**
     * Returns standard deviation of percolation threshold (s from description)
     */
    double get_standard_deviation() const;

    /**
     * Returns log edge of condidence interval
     */
    double get_confidence_low() const;

    /**
     * Returns high edge of confidence interval
     */
    double get_confidence_high() const;

    /**
     * Makes all experiments, calculates statistic values
     */
    void execute();

private:
    std::vector<double> xs;
    int size;
};
