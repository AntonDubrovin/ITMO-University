'use strict';


/**
 * Складывает два целых числа
 * @param {Number} a Первое целое
 * @param {Number} b Второе целое
 * @throws {TypeError} Когда в аргументы переданы не числа
 * @returns {Number} Сумма аргументов
 */
function abProblem(a, b) {
    // Ваше решение
    if (typeof a !== 'number' || typeof b !== 'number') {
        throw new TypeError(`${a} or ${b} is not a number`);
    }

    return a + b;
}

/**
 * Определяет век по году
 * @param {Number} year Год, целое положительное число
 * @throws {TypeError} Когда в качестве года передано не число
 * @throws {RangeError} Когда год – отрицательное значение
 * @returns {Number} Век, полученный из года
 */
function centuryByYearProblem(year) {
    // Ваше решение
    if (typeof year !== 'number' && !Number.isInteger(year)) {
        throw new TypeError(`${year} is not a number`)
    }
    if (year <= 0) {
        throw new RangeError(`${year} is less than zero`)
    }

    let century = Math.floor(year / 100);
    if (year % 100 === 0) {
        return century;
    } else {
        return century + 1;
    }
}

/**
 * Переводит цвет из формата HEX в формат RGB
 * @param {String} hexColor Цвет в формате HEX, например, '#FFFFFF'
 * @throws {TypeError} Когда цвет передан не строкой
 * @throws {RangeError} Когда значения цвета выходят за пределы допустимых
 * @returns {String} Цвет в формате RGB, например, '(255, 255, 255)'
 */
function colorsProblem(hexColor) {
    // Ваше решение
    if (typeof hexColor !== 'string') {
        throw new TypeError(`${hexColor} is not a string`);
    }
    if (hexColor.length > 7) {
        throw new RangeError(`length of ${hexColor} is more than 7`);
    }
    let redSlice = hexColor.slice(1, 3);
    let greenSlice = hexColor.slice(3, 5);
    let blueSlice = hexColor.slice(5, 7);
    if (redSlice.charAt(0).toUpperCase() > 'F' || redSlice.charAt(1).toUpperCase() > 'F'
        || greenSlice.charAt(0).toUpperCase() > 'F' || greenSlice.charAt(1).toUpperCase() > 'F'
        || blueSlice.charAt(0).toUpperCase() > 'F' || blueSlice.charAt(1).toUpperCase() > 'F') {
        throw new RangeError('Some letter is more than F/f');
    }

    let red = parseInt(redSlice, 16);
    let green = parseInt(greenSlice, 16);
    let blue = parseInt(blueSlice, 16);

    return '(' + red + ', ' + green + ', ' + blue + ')';
}

/**
 * Находит n-ое число Фибоначчи
 * @param {Number} n Положение числа в ряде Фибоначчи
 * @throws {TypeError} Когда в качестве положения в ряде передано не число
 * @throws {RangeError} Когда положение в ряде не является целым положительным числом
 * @returns {Number} Число Фибоначчи, находящееся на n-ой позиции
 */
function fibonacciProblem(n) {
    // Ваше решение
    if (typeof n !== 'number') {
        throw new TypeError(`${n} is not a number`);
    }
    if (n <= 0 || !Number.isInteger(n)) {
        throw new RangeError(`${n} is less than zero`);
    }

    let first = 1;
    let second = 1;
    let next;
    for (let i = 0; i < n - 2; i++) {
        next = first + second;
        first = second;
        second = next;
    }
    return second;
}


/**
 * Транспонирует матрицу
 * @param {(Any[])[]} matrix Матрица размерности MxN
 * @throws {TypeError} Когда в функцию передаётся не двумерный массив
 * @returns {(Any[])[]} Транспонированная матрица размера NxM
 */
function matrixProblem(matrix) {
    // Ваше решение
    if (!Array.isArray(matrix)) {
        throw new TypeError(`${matrix} is not a two-dimensional array`)
    }
    for (let i = 0; i < matrix.length; i++) {
        if (!Array.isArray(matrix[i])) {
            throw new TypeError(`${matrix} is not a two-dimensional array`);
        }
    }

    let result = [];
    for (let i = 0; i < matrix[0].length; i++) {
        result.push([]);
    }

    for (let i = 0; i < matrix.length; i++) {
        for (let j = 0; j < matrix[i].length; j++) {
            result[j].push(matrix[i][j]);
        }
    }

    return result;
}

/**
 * Переводит число в другую систему счисления
 * @param {Number} n Число для перевода в другую систему счисления
 * @param {Number} targetNs Система счисления, в которую нужно перевести (Число от 2 до 36)
 * @throws {TypeError} Когда переданы аргументы некорректного типа
 * @throws {RangeError} Когда система счисления выходит за пределы значений [2, 36]
 * @returns {String} Число n в системе счисления targetNs
 */
function numberSystemProblem(n, targetNs) {
    // Ваше решение
    if (typeof n !== 'number' || typeof targetNs !== 'number') {
        throw new TypeError(`${n} or ${targetNs} is not a number`)
    }
    if (targetNs < 2 || targetNs > 36) {
        throw new RangeError(`${targetNs} is less than 2 or more than 36`)
    }

    return n.toString(targetNs);
}

/**
 * Проверяет соответствие телефонного номера формату
 * @param {String} phoneNumber Номер телефона в формате '8–800–xxx–xx–xx'
 * @throws {TypeError} Когда в качестве аргумента передаётся не строка
 * @returns {Boolean} Если соответствует формату, то true, а иначе false
 */
function phoneProblem(phoneNumber) {
    //Ваше решение
    if (typeof phoneNumber !== 'string') {
        throw new TypeError(`${phoneNumber} is not a string`);
    }

    let regexp = /^8-800-\d{3}-\d{2}-\d{2}$/;
    return regexp.test(phoneNumber);
}

/**
 * Определяет количество улыбающихся смайликов в строке
 * @param {String} text Строка в которой производится поиск
 * @throws {TypeError} Когда в качестве аргумента передаётся не строка
 * @returns {Number} Количество улыбающихся смайликов в строке
 */
function smilesProblem(text) {
    // Ваше решение
    if (typeof text !== 'string') {
        throw new TypeError(`${text} is not a string`);
    }

    let count = 0;
    for (let i = 0; i < text.length; i++) {
        if (text.charAt(i) === ')') {
            if (i >= 2) {
                if (text.charAt(i - 1) === '-' && text.charAt(i - 2) === ':') {
                    count++;
                }
            }
        } else if (text.charAt(i) === '(') {
            if (i <= text.length - 3) {
                if (text.charAt(i + 1) === '-' && text.charAt(i + 2) === ':') {
                    count++;
                }
            }
        }
    }

    return count;
}

/**
 * Определяет победителя в игре "Крестики-нолики"
 * Тестами гарантируются корректные аргументы.
 * @param {(('x' | 'o')[])[]} field Игровое поле 3x3 завершённой игры
 * @returns {'x' | 'o' | 'draw'} Результат игры
 */
function ticTacToeProblem(field) {
    // Ваше решение
    let rowFlag;
    let win;
    for (let i = 0; i < 3; i++) {
        rowFlag = true;
        win = field[i][0];
        for (let j = 1; j < 3; j++) {
            if (field[i][j - 1] !== field[i][j]) {
                rowFlag = false;
            }
        }
        if (rowFlag === true) {
            return win;
        }
    }

    let columnFlag;
    for (let i = 0; i < 3; i++) {
        columnFlag = true;
        win = field[0][i];
        for (let j = 1; j < 3; j++) {
            if (field[j - 1][i] !== field[j][i]) {
                columnFlag = false;
            }
        }
        if (columnFlag === true) {
            return win;
        }
    }

    win = field[0][0]
    if (field[1][1] === win && field[2][2] === win) {
        return win;
    }

    win = field[0][2]
    if (field[1][1] === win && field[2][0] === win) {
        return win;
    }

    return 'draw'
}

module.exports = {
    abProblem,
    centuryByYearProblem,
    colorsProblem,
    fibonacciProblem,
    matrixProblem,
    numberSystemProblem,
    phoneProblem,
    smilesProblem,
    ticTacToeProblem
};