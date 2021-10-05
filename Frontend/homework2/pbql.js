'use strict';

/**
 * Телефонная книга
 */
const phoneBook = new Map()
let requestNumber = 0

/**
 * Вызывайте эту функцию, если есть синтаксическая ошибка в запросе
 * @param {number} lineNumber – номер строки с ошибкой
 * @param {number} charNumber – номер символа, с которого запрос стал ошибочным
 */
function syntaxError(lineNumber, charNumber) {
    throw new Error(`SyntaxError: Unexpected token at ${lineNumber}:${charNumber}`)
}

function createContact(request) {
    if (request[1] === 'контакт') {
        let name = ''
        for (let i = 2; i < request.length; i++) {
            if (i > 2) {
                name += ' '
            }
            name += request[i]
        }
        if (!phoneBook.has(name)) {
            phoneBook.set(name, [[], []])
        }
    } else {
        // ошибка 2 слово
        syntaxError(requestNumber, 8)
    }
}

function deleteContact(request) {
    let name = ''
    for (let i = 2; i < request.length; i++) {
        if (i > 2) {
            name += ' '
        }
        name += request[i]
    }
    phoneBook.delete(name)
}

function indexNumber(i, request) {
    let index = 0
    for (let j = 0; j < i; j++) {
        index += request[j].length + 1
    }
    index++
    return index
}

function deletePhoneNumberAndMail(request) {
    let i = 1
    let phones = []
    let mails = []
    while (request[i] === 'телефон' || request[i] === 'почту') {
        if (request[i] === 'телефон') {
            i++
            if (/^[0-9]{10}$/.test(request[i])) {
                phones.push(request[i])
                i++
                if (request[i] === 'и' || request[i] === 'для') {
                    i++
                } else {
                    syntaxError(requestNumber, indexNumber(i, request))
                }
            } else {
                syntaxError(requestNumber, indexNumber(i, request))
            }
        } else if (request[i] === 'почту') {
            i++
            mails.push(request[i])
            i++
            if (request[i] === 'и' || request[i] === 'для') {
                i++
            } else {
                syntaxError(requestNumber, indexNumber(i, request))
            }
        } else {
            syntaxError(requestNumber, indexNumber(i, request))
        }
    }

    if (request[i - 1] === 'для') {
        if (request[i] === 'контакта') {
            i++
            let name = request[i]
            for (let j = i + 1; j < request.length; j++) {
                name += ' ' + request[j]
            }
            if (phoneBook.has(name)) {
                for (let j = 0; j < phones.length; j++) {
                    if (phoneBook.get(name)[0].indexOf(phones[j]) !== -1) {
                        phoneBook.get(name)[0].splice(phoneBook.get(name)[0].indexOf(phones[j]), 1)
                    }
                }
                for (let j = 0; j < mails.length; j++) {
                    if (phoneBook.get(name)[1].indexOf(mails[j]) !== -1) {
                        phoneBook.get(name)[1].splice(phoneBook.get(name)[1].indexOf(mails[j]), 1)
                    }
                }
            }
        } else {
            syntaxError(requestNumber, indexNumber(i, request))
        }
    } else {
        syntaxError(requestNumber, indexNumber(i, request))
    }
}

function deleteContacts(request) {
    if (request[2] === 'где') {
        if (request[3] === 'есть') {
            let delRequest = request[4]
            for (let q = 5; q < request.length; q++) {
                delRequest += ' ' + request[q]
            }
            if (delRequest === '' || delRequest === ' ') {
                return
            }
            for (let entry of phoneBook) {
                let element = entry[0]
                let phones = entry[1][0]
                let mails = entry[1][1]
                if (element.includes(delRequest)) {
                    phoneBook.delete(element)
                } else {
                    if (phones !== []) {
                        for (let j = 0; j < phones.length; j++) {
                            if (phones[j].includes(delRequest)) {
                                phoneBook.delete(element)
                                break
                            }
                        }
                    }
                    if (mails !== []) {
                        for (let j = 0; j < mails.length; j++) {
                            if (mails[j].includes(delRequest)) {
                                phoneBook.delete(element)
                                break
                            }
                        }
                    }
                }
            }

        } else {
            syntaxError(requestNumber, 21)
        }
    } else {
        syntaxError(requestNumber, 17)
    }
}

function add(request) {
    let i = 1
    let phones = []
    let mails = []
    while (request[i] === 'телефон' || request[i] === 'почту') {
        if (request[i] === 'телефон') {
            i++
            if (/^[0-9]{10}$/.test(request[i])) {
                phones.push(request[i])
                i++
                if (request[i] === 'и' || request[i] === 'для') {
                    i++
                } else {
                    syntaxError(requestNumber, indexNumber(i, request))
                }
            } else {
                syntaxError(requestNumber, indexNumber(i, request))
            }
        } else if (request[i] === 'почту') {
            i++
            mails.push(request[i])
            i++
            if (request[i] === 'и' || request[i] === 'для') {
                i++
            } else {
                syntaxError(requestNumber, indexNumber(i, request))
            }
        }
    }

    if (request[i - 1] === 'для') {
        if (request[i] === 'контакта') {
            i++
            let name = request[i]
            i++
            for (let j = i; j < request.length; j++) {
                name += ' ' + request[j]
            }
            if (phoneBook.has(name)) {
                for (let j = 0; j < phones.length; j++) {
                    if (phoneBook.get(name)[0].indexOf(phones[j]) === -1) {
                        phoneBook.get(name)[0].push(phones[j])
                    }
                }
                for (let j = 0; j < mails.length; j++) {
                    if (phoneBook.get(name)[1].indexOf(mails[j]) === -1) {
                        phoneBook.get(name)[1].push(mails[j])
                    }
                }
            }
        } else {
            syntaxError(requestNumber, indexNumber(i, request))
        }
    } else {
        syntaxError(requestNumber, indexNumber(i, request))
    }
}

function show(request) {
    let requests = []
    let i = 1
    while (request[i] === 'имя' || request[i] === 'почты' || request[i] === 'телефоны') {
        if (request[i] === 'имя') {
            requests.push('имя')
            i++
            if (request[i] === 'и' || request[i] === 'для') {
                i++
            } else {
                syntaxError(requestNumber, indexNumber(i, request))
            }
        } else if (request[i] === 'почты') {
            requests.push('почты')
            i++
            if (request[i] === 'и' || request[i] === 'для') {
                i++
            } else {
                syntaxError(requestNumber, indexNumber(i, request))
            }
        } else if (request[i] === 'телефоны') {
            requests.push('телефоны')
            i++
            if (request[i] === 'и' || request[i] === 'для') {
                i++
            } else {
                syntaxError(requestNumber, indexNumber(i, request))
            }
        }
    }

    let answer = []
    if (request[i - 1] === 'для') {
        if (request[i] === 'контактов,') {
            i++
            if (request[i] === 'где') {
                i++
                if (request[i] === 'есть') {
                    i++
                    let part = request[i]
                    for (let entry of phoneBook) {
                        let name = entry[0]
                        if (name === '') {
                            return
                        }
                        let phones = entry[1][0]
                        let mails = entry[1][1]
                        let flag = false
                        for (let j = 0; j < mails.length; j++) {
                            if (mails[j].includes(part)) {
                                flag = true
                            }
                        }
                        for (let j = 0; j < phones.length; j++) {
                            if (phones[j].includes(part)) {
                                flag = true
                            }
                        }
                        if (name.includes(part)) {
                            flag = true
                        }
                        if (flag) {
                            let stringAnswer = ''
                            for (let j = 0; j < requests.length; j++) {
                                if (requests[j] === 'имя') {
                                    stringAnswer += name + ';'
                                } else if (requests[j] === 'телефоны') {
                                    if (phones.length === 0) {
                                        stringAnswer += ';'
                                    } else {
                                        for (let q = 0; q < phones.length - 1; q++) {
                                            let phone = `+7 (${phones[q].slice(0, 3)}) ${phones[q].slice(3, 6)}-${phones[q].slice(6, 8)}-${phones[q].slice(8, 10)}`
                                            stringAnswer += phone + ','
                                        }
                                        let phone = `+7 (${phones[phones.length - 1].slice(0, 3)}) ${phones[phones.length - 1].slice(3, 6)}-${phones[phones.length - 1].slice(6, 8)}-${phones[phones.length - 1].slice(8, 10)}`
                                        stringAnswer += phone + ';'
                                    }
                                } else if (requests[j] === 'почты') {
                                    if (mails.length === 0) {
                                        stringAnswer += ';'
                                    } else {
                                        for (let q = 0; q < mails.length - 1; q++) {
                                            stringAnswer += mails[q] + ','
                                        }
                                        stringAnswer += mails[mails.length - 1] + ';'
                                    }
                                }
                            }
                            if (stringAnswer[stringAnswer.length - 1] === ';') {
                                stringAnswer = stringAnswer.slice(0, -1)
                            }
                            answer.push(stringAnswer)
                        }
                    }
                } else {
                    syntaxError(requestNumber, indexNumber(i, request))
                }
            } else {
                syntaxError(requestNumber, indexNumber(i, request))
            }
        } else {
            syntaxError(requestNumber, indexNumber(i, request))
        }
    } else {
        syntaxError(requestNumber, indexNumber(i, request))
    }
    return answer
}

/**
 * Выполнение запроса на языке pbQL
 * @param {string} query
 * @returns {string[]} - строки с результатами запроса
 */
function run(query) {
    let megaAnswer = []
    let newQuery = query.split(';')
    let flag = false
    if (newQuery[newQuery.length - 1] === '') {
        newQuery.splice(newQuery.length - 1, 1)
    } else {
        flag = true
    }
    newQuery.forEach((request) => {
        requestNumber++
        request = request.split(' ')
        if (request[0] === 'Создай') {
            createContact(request)
        } else if (request[0] === 'Удали') {
            if (request[1] === 'контакт') {
                deleteContact(request)
            } else if (request[1] === 'телефон' || request[1] === 'почту') {
                deletePhoneNumberAndMail(request)
            } else if (request[1] === 'контакты,') {
                deleteContacts(request)
            } else {
                // ошибка 2 слово
                syntaxError(requestNumber, 7)
            }
        } else if (request[0] === 'Добавь') {
            add(request)
        } else if (request[0] === 'Покажи') {
            let answer = show(request)
            if (answer !== undefined) {
                answer.forEach((element) => {
                    megaAnswer.push(element)
                })
            }
        } else {
            // ошибка 1 слово
            syntaxError(requestNumber, 1)
        }
    })
    if (flag) {
        syntaxError(requestNumber, newQuery[newQuery.length - 1].length + 1)
    }
    return megaAnswer;
}

module.exports = {phoneBook, run};