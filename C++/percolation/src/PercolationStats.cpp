#include <vector>
#include <cmath>
#include "../include/Percolation.h"
#include "../include/PercolationStats.h"
#include <random>
#include <ctime>


PercolationStats::PercolationStats(size_t dimension, size_t trials) : size(dimension) {
    xs.resize(trials);
    execute();
}

double PercolationStats::get_mean() const {
    // Write your logic here
    double x = std::accumulate(xs.begin(), xs.end(), 0.0);
    return x / xs.size();
}

double PercolationStats::get_standard_deviation() const {
    // Write your logic here
    double x = get_mean();
    double s = 0;
    for (size_t i = 0; i < xs.size(); i++) {
        s += pow(xs[i] - x, 2);
    }
    s /= (xs.size() - 1);
    return sqrt(s);
}

double PercolationStats::get_confidence_low() const {
    // Write your logic here
    double x = get_mean();
    double s = get_standard_deviation();
    return (x - (1.96 * s / std::sqrt(xs.size())));
}

double PercolationStats::get_confidence_high() const {
    // Write your logic here
    double x = get_mean();
    double s = get_standard_deviation();
    return (x + (1.96 * s / std::sqrt(xs.size())));
}

void PercolationStats::execute() {
    // Write your logic here
    std::random_device rd;
    std::mt19937 gen(rd());
    for (size_t i = 0; i < xs.size(); i++) {
        Percolation percolation(size);
        while (!percolation.has_percolation()) {
            int row = gen() % size;
            int column = gen() % size;
            if (!percolation.is_open(row, column)) {
                percolation.open(row, column);
            }
        }
        xs[i] = percolation.get_numbet_of_open_cells() / static_cast<double>(size * size);
    }
}