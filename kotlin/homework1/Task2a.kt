package com.h0tk3y.spbsu.kotlin.course.lesson1

/*
 * Реализуйте функцию factorial, вычисляющую значение факториала. Значение факториала нуля считайте едииницей.
 * Факториалом отрицательных чисел считайте -1. Целочисленное переполнение игнорируйте.
 */
fun factorial(n: Int): Int = when {
    n < 0 -> -1
    else -> {
        var answer = 1
        for (i in 2..n) {
            answer *= i
        }
        answer
    }
}