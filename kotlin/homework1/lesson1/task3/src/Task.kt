package com.h0tk3y.spbsu.kotlin.course.lesson1

import java.lang.Integer.max
import java.lang.Integer.min


/*
 * Реализуйте функцию intersectRanges, возвращающую диапазон, являющийся пересечением двух
 * данных целочисленных диапазонов. Если данные диапазоны не пересекаются, возвращайте null.
 *
 * Например, пересечением дипазонов `1..10` и `5..20` является `5..10`,
 * а диапазоны `0..0` и `1..1` не пересекаются.
 * 
 * Для получения границ диапазонов используйте свойства `first` и `last`.
 */
fun intersectRanges(rangeA: IntRange, rangeB: IntRange): IntRange? {
    val leftRange = max(rangeA.first, rangeB.first)
    val rightRange = min(rangeA.last, rangeB.last)
    return if (rightRange < leftRange) {
        null
    } else {
        IntRange(leftRange, rightRange)
    }
}

/*
 * Реализуйте функцию `cut`, которая возвращает список, содержащий от нуля до двух непрерывных
 * диапазонов, которые получаются из диапазона `outerRange`, если из него "вырезать" диапазон `cutout`.
 * (на самом деле, конечно же, "вырезать" из диапазона можно только его пересечение с другим диапазоном,
 *  а остальную часть другого диапазона следует отбросить)
 * 
 * Например, если из диапазона `0..10` вырезать `1..7`, получится два диапазона, `0..0` и `8..10`.
 * Если же из `0..10` вырезать `0..3`, то результатом будет только один диапазон `4..10`.
 */
fun cut(outerRange: IntRange, cutout: IntRange): List<IntRange> {
    val result = mutableListOf<IntRange>()
    if (outerRange.first < cutout.first) {
        result.add(IntRange(outerRange.first, cutout.first - 1))
    }
    if (outerRange.last > cutout.last) {
        result.add(IntRange(cutout.last + 1, outerRange.last))
    }
    return result
}