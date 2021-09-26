package com.h0tk3y.spbsu.kotlin.course.lesson1.test

fun intersectRanges(rangeA: IntRange, rangeB: IntRange): IntRange? {
    val left = if (rangeA.first <= rangeB.first) rangeA else rangeB
    val right = if (left === rangeA) rangeB else rangeA

    if (left.last < right.first)
        return null

    if (left.last > right.last)
        return right

    return right.first..left.last
}

fun cut(outerRange: IntRange, cutout: IntRange): List<IntRange> {
    val intersection = intersectRanges(outerRange, cutout)
    return when {
        intersection == null -> listOf(outerRange)
        intersection == outerRange -> emptyList()
        else -> listOfNotNull(
            outerRange.first until intersection.first,
            (intersection.last + 1)..outerRange.last
        ).filter { !it.isEmpty() }
    }
}