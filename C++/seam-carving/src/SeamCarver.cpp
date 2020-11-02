#include "../include/SeamCarver.h"
#include <algorithm>
#include <cmath>
#include <float.h>

SeamCarver::SeamCarver(Image image)
        : m_image(std::move(image)) {
    GetEnergyTable();
    GetHorizontalSumEnergyTable();
    GetVerticalSumEnergyTable();
}

const Image &SeamCarver::GetImage() const {
    return m_image;
}

size_t SeamCarver::GetImageWidth() const {
    return m_image.getWidth();
}

size_t SeamCarver::GetImageHeight() const {
    return m_image.getHeight();
}

double SeamCarver::GetEnergyColumn(size_t column, size_t row) const {
    size_t prevColumn, nextColumn;

    if (column == 0) {
        prevColumn = GetImageWidth() - 1;
        nextColumn = column + 1;
    } else if (column == GetImageWidth() - 1) {
        prevColumn = column - 1;
        nextColumn = 0;
    } else {
        prevColumn = column - 1;
        nextColumn = column + 1;
    }

    return
            std::pow(m_image.GetPixel(nextColumn, row).m_red - m_image.GetPixel(prevColumn, row).m_red, 2) +
            std::pow(m_image.GetPixel(nextColumn, row).m_green - m_image.GetPixel(prevColumn, row).m_green, 2) +
            std::pow(m_image.GetPixel(nextColumn, row).m_blue - m_image.GetPixel(prevColumn, row).m_blue, 2);
}

double SeamCarver::GetEnergyRow(size_t column, size_t row) const {
    size_t prevRow, nextRow;

    if (row == 0) {
        prevRow = GetImageHeight() - 1;
        nextRow = row + 1;
    } else if (row == GetImageHeight() - 1) {
        prevRow = row - 1;
        nextRow = 0;
    } else {
        prevRow = row - 1;
        nextRow = row + 1;
    }

    return
            std::pow(m_image.GetPixel(column, nextRow).m_red - m_image.GetPixel(column, prevRow).m_red, 2) +
            std::pow(m_image.GetPixel(column, nextRow).m_green - m_image.GetPixel(column, prevRow).m_green, 2) +
            std::pow(m_image.GetPixel(column, nextRow).m_blue - m_image.GetPixel(column, prevRow).m_blue, 2);
}

void SeamCarver::GetEnergyTable() {
    energyTable.resize(GetImageWidth());

    for (size_t row = 0; row < GetImageWidth(); row++) {
        energyTable[row].resize(GetImageHeight());
    }

    for (size_t row = 0; row < GetImageHeight(); row++) {
        for (size_t column = 0; column < GetImageWidth(); column++) {
            energyTable[column][row] = std::sqrt(GetEnergyColumn(column, row) + GetEnergyRow(column, row));
        }
    }
}

double SeamCarver::GetPixelEnergy(size_t columnId, size_t rowId) const {
    return energyTable[columnId][rowId];
}

void SeamCarver::GetHorizontalSumEnergyTable() {
    dpHorizontal.resize(GetImageWidth());
    dpHorizontal[0].resize(GetImageHeight());

    for (size_t row = 0; row < GetImageHeight(); row++) {
        dpHorizontal[0][row].value = energyTable[0][row];
        dpHorizontal[0][row].parentColumn = 0;
        dpHorizontal[0][row].parentRow = row;
    }

    for (size_t column = 1; column < GetImageWidth(); column++) {
        dpHorizontal[column].resize(GetImageHeight());
        for (size_t row = 0; row < GetImageHeight(); row++) {
            double minEnergy = std::numeric_limits<double>::max();
            size_t parentRow = 0;

            if (row > 0 && dpHorizontal[column - 1][row - 1].value <= minEnergy) {
                minEnergy = dpHorizontal[column - 1][row - 1].value;
                parentRow = row - 1;
            }

            if (row < GetImageHeight() - 1 && dpHorizontal[column - 1][row + 1].value <= minEnergy) {
                minEnergy = dpHorizontal[column - 1][row + 1].value;
                parentRow = row + 1;
            }

            if (dpHorizontal[column - 1][row].value <= minEnergy) {
                minEnergy = dpHorizontal[column - 1][row].value;
                parentRow = row;
            }

            dpHorizontal[column][row].value = minEnergy + energyTable[column][row];
            dpHorizontal[column][row].parentColumn = column - 1;
            dpHorizontal[column][row].parentRow = parentRow;
        }
    }
}

SeamCarver::Seam SeamCarver::FindHorizontalSeam() const {
    Seam horizontalSeam(GetImageWidth());
    double minEnergy = std::numeric_limits<double>::max();
    size_t curRow = 0, curColumn = GetImageWidth() - 1;

    for (size_t row = 0; row < GetImageHeight(); row++) {
        if (dpHorizontal[GetImageWidth() - 1][row].value < minEnergy) {
            minEnergy = dpHorizontal[GetImageWidth() - 1][row].value;
            curRow = row;
        }
    }

    horizontalSeam[curColumn] = curRow;

    size_t newRow, newColumn;
    while (!(dpHorizontal[curColumn][curRow].parentColumn == curColumn &&
             dpHorizontal[curColumn][curRow].parentRow == curRow)) {
        newColumn = dpHorizontal[curColumn][curRow].parentColumn;
        newRow = dpHorizontal[curColumn][curRow].parentRow;
        curColumn = newColumn;
        curRow = newRow;
        horizontalSeam[curColumn] = curRow;
    }
    horizontalSeam[curColumn] = curRow;

    return horizontalSeam;
}


void SeamCarver::GetVerticalSumEnergyTable() {
    dpVertical.resize(GetImageWidth());

    for (size_t column = 0; column < GetImageWidth(); column++) {
        dpVertical[column].resize(GetImageHeight());
        dpVertical[column][0].value = energyTable[column][0];
        dpVertical[column][0].parentColumn = column;
        dpVertical[column][0].parentRow = 0;
    }

    for (size_t row = 1; row < GetImageHeight(); row++) {
        for (size_t column = 0; column < GetImageWidth(); column++) {
            double minEnergy = std::numeric_limits<double>::max();
            size_t parentColumn = 0;

            if (column > 0 && dpVertical[column - 1][row - 1].value <= minEnergy) {
                minEnergy = dpVertical[column - 1][row - 1].value;
                parentColumn = column - 1;
            }

            if (column < GetImageWidth() - 1 && dpVertical[column + 1][row - 1].value <= minEnergy) {
                minEnergy = dpVertical[column + 1][row - 1].value;
                parentColumn = column + 1;
            }

            if (dpVertical[column][row - 1].value <= minEnergy) {
                minEnergy = dpVertical[column][row - 1].value;
                parentColumn = column;
            }

            dpVertical[column][row].value = minEnergy + energyTable[column][row];
            dpVertical[column][row].parentColumn = parentColumn;
            dpVertical[column][row].parentRow = row - 1;
        }
    }
}

SeamCarver::Seam SeamCarver::FindVerticalSeam() const {
    Seam verticalSeam(GetImageHeight());
    double minEnergy = std::numeric_limits<double>::max();
    size_t curColumn = 0, curRow = GetImageHeight() - 1;

    for (size_t column = 0; column < GetImageWidth(); column++) {
        if (dpVertical[column][GetImageHeight() - 1].value < minEnergy) {
            minEnergy = dpVertical[column][GetImageHeight() - 1].value;
            curColumn = column;
        }
    }

    verticalSeam[curRow] = curColumn;

    size_t newRow, newColumn;
    while (!(dpVertical[curColumn][curRow].parentColumn == curColumn &&
             dpVertical[curColumn][curRow].parentRow == curRow)) {
        newColumn = dpVertical[curColumn][curRow].parentColumn;
        newRow = dpVertical[curColumn][curRow].parentRow;
        curColumn = newColumn;
        curRow = newRow;
        verticalSeam[curRow] = curColumn;
    }
    verticalSeam[curRow] = curColumn;

    return verticalSeam;
}

void SeamCarver::RemoveHorizontalSeam(const Seam &seam) {
    size_t column, row;

    for (size_t i = 0; i < seam.size(); i++) {
        column = i;
        row = seam[i];
        for (size_t j = row; j < GetImageHeight() - 1; j++) {
            m_image.m_table[column][j] = m_image.m_table[column][j + 1];
        }
    }

    for (size_t i = 0; i < GetImageWidth(); i++) {
        m_image.m_table[i].resize(GetImageHeight() - 1);
    }

    GetEnergyTable();
    GetHorizontalSumEnergyTable();
    GetVerticalSumEnergyTable();
}

void SeamCarver::RemoveVerticalSeam(const Seam &seam) {
    size_t column, row;

    for (size_t i = 0; i < seam.size(); i++) {
        row = i;
        column = seam[i];
        for (size_t j = column; j < GetImageWidth() - 1; j++) {
            m_image.m_table[j][row] = m_image.m_table[j + 1][row];
        }
    }
    m_image.m_table.pop_back();

    GetEnergyTable();
    GetHorizontalSumEnergyTable();
    GetVerticalSumEnergyTable();
}