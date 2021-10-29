'use strict';

/**
 * @param {Object} schedule Расписание Банды
 * @param {number} duration Время на ограбление в минутах
 * @param {Object} workingHours Время работы банка
 * @param {string} workingHours.from Время открытия, например, "10:00+5"
 * @param {string} workingHours.to Время закрытия, например, "18:00+5"
 * @returns {Object}
 */
// const gangSchedule = {
//     Danny: [{from: 'ПН 12:00+5', to: 'ПН 17:00+5'}, {from: 'ВТ 13:00+5', to: 'ВТ 16:00+5'}],
//     Rusty: [{from: 'ПН 11:30+5', to: 'ПН 16:30+5'}, {from: 'ВТ 13:00+5', to: 'ВТ 16:00+5'}],
//     Linus: [
//         {from: 'ПН 09:00+3', to: 'ПН 14:00+3'},
//         {from: 'ПН 21:00+3', to: 'ВТ 09:30+3'},
//         {from: 'СР 09:30+3', to: 'СР 15:00+3'}
//     ]
// };
//
// const bankWorkingHours = {
//     from: '10:00+5',
//     to: '18:00+5'
// };
//
// // Время не существует
// const longMoment = getAppropriateMoment(gangSchedule, 121, bankWorkingHours);
//
// // Выведется `false` и `""`
// console.info(longMoment.exists());
// console.info(longMoment.format('Метим на %DD, старт в %HH:%MM!'));

function getAppropriateMoment(schedule, duration, workingHours) {
    let freeTimeDanny = []
    let freeTimeRusty = []
    let freeTimeLinus = []

    let result = findAnswer()

    function findAnswer() {
        timetableFree()
        let bankTimes = bankTimetable()
        let allFreeTimes = []
        for (let danny = 0; danny < freeTimeDanny.length; danny++) {
            for (let rusty = 0; rusty < freeTimeRusty.length; rusty++) {
                for (let linus = 0; linus < freeTimeLinus.length; linus++) {
                    if (Math.max(freeTimeDanny[danny].from, Math.max(freeTimeRusty[rusty].from, freeTimeLinus[linus].from)) <
                        Math.min(freeTimeDanny[danny].to, Math.min(freeTimeRusty[rusty].to, freeTimeLinus[linus].to))) {
                        allFreeTimes.push({
                            from: Math.max(freeTimeDanny[danny].from, Math.max(freeTimeRusty[rusty].from, freeTimeLinus[linus].from)),
                            to: Math.min(freeTimeDanny[danny].to, Math.min(freeTimeRusty[rusty].to, freeTimeLinus[linus].to))
                        })
                    }
                }
            }
        }

        for (let i = 0; i < allFreeTimes.length; i++) {
            let time = allFreeTimes[i]
            for (let j = 0; j < bankTimes.length; j++) {
                let bankTime = bankTimes[j]
                let from = Math.max(time.from, bankTime.from)
                let to = Math.min(time.to, bankTime.to)
                if (to - from >= duration) {
                    return {
                        from: from,
                        to: to
                    }
                }
            }
        }

        return ({
            from: -1000,
            to: -1000
        })
    }

    function bankTimetable() {
        let result = []
        result.push(getFromToMinutesZero({
            from: "ПН " + workingHours.from,
            to: "ПН " + workingHours.to
        }))
        result.push(getFromToMinutesZero({
            from: "ВТ " + workingHours.from,
            to: "ВТ " + workingHours.to
        }))
        result.push(getFromToMinutesZero({
            from: "СР " + workingHours.from,
            to: "СР " + workingHours.to
        }))
        return result
    }


    function convertDayToMinutes(day) {
        let result
        switch (day) {
            case 'ПН': {
                result = 0
                break
            }
            case 'ВТ': {
                result = 24 * 60
                break
            }
            case 'СР': {
                result = 2 * 24 * 60
                break
            }
            default:
                throw Error("День может быть только ПН ВТ или СР")
        }
        return result
    }

    function getFromToMinutesZero(busy) {
        let splitBusyFrom = busy.from.split(' ')
        let dayFrom = convertDayToMinutes(splitBusyFrom[0])
        let parseTimeFrom = splitBusyFrom[1].split(':')
        let fromTimeMinutes = dayFrom + (parseInt(parseTimeFrom[0]) - parseInt(parseTimeFrom[1].split('+')[1])) * 60 +
            parseInt(parseTimeFrom[1].split('+')[0])

        let splitBusyTo = busy.to.split(' ')
        let dayTo = convertDayToMinutes(splitBusyTo[0])
        let parseTimeTo = splitBusyTo[1].split(':')
        let toTimeMinutes = dayTo + (parseInt(parseTimeTo[0]) - parseInt(parseTimeTo[1].split('+')[1])) * 60 +
            parseInt(parseTimeTo[1].split('+')[0])
        return {
            from: fromTimeMinutes,
            to: toTimeMinutes
        }
    }

    function timetableFree() {
        // ДЛЯ ДАНИ
        for (let i = 0; i <= schedule.Danny.length; i++) {
            let time = schedule.Danny[i]
            if (i === 0) {
                freeTimeDanny.push({
                    from: -840,
                    to: getFromToMinutesZero(time).from
                })
            } else if (i !== schedule.Danny.length) {
                freeTimeDanny.push({
                    from: getFromToMinutesZero(schedule.Danny[i - 1]).to,
                    to: getFromToMinutesZero(time).from
                })
            } else {
                freeTimeDanny.push({
                    from: getFromToMinutesZero(schedule.Danny[i - 1]).to,
                    to: 3 * 24 * 60 - 1
                })
            }
        }

        // ДЛЯ РАСТИ
        for (let i = 0; i <= schedule.Rusty.length; i++) {
            let time = schedule.Rusty[i]
            if (i === 0) {
                freeTimeRusty.push({
                    from: -840,
                    to: getFromToMinutesZero(time).from
                })
            } else if (i !== schedule.Rusty.length) {
                freeTimeRusty.push({
                    from: getFromToMinutesZero(schedule.Rusty[i - 1]).to,
                    to: getFromToMinutesZero(time).from
                })
            } else {
                freeTimeRusty.push({
                    from: getFromToMinutesZero(schedule.Rusty[i - 1]).to,
                    to: 3 * 24 * 60 - 1
                })
            }
        }

        // ДЛЯ ЛАЙНУСА
        for (let i = 0; i <= schedule.Linus.length; i++) {
            let time = schedule.Linus[i]
            if (i === 0) {
                freeTimeLinus.push({
                    from: -840,
                    to: getFromToMinutesZero(time).from
                })
            } else if (i !== schedule.Linus.length) {
                freeTimeLinus.push({
                    from: getFromToMinutesZero(schedule.Linus[i - 1]).to,
                    to: getFromToMinutesZero(time).from
                })
            } else {
                freeTimeLinus.push({
                    from: getFromToMinutesZero(schedule.Linus[i - 1]).to,
                    to: 3 * 24 * 60 - 1
                })
            }
        }
        return {
            Danny: freeTimeDanny,
            Rusty: freeTimeRusty,
            Linus: freeTimeLinus
        }
    }

    return {
        /**
         * Найдено ли время
         * @returns {boolean}
         */
        exists() {
            return result.from !== -1000;
        },

        /**
         * Возвращает отформатированную строку с часами
         * для ограбления во временной зоне банка
         *
         * @param {string} template
         * @returns {string}
         *
         * @example
         * ```js
         * getAppropriateMoment(...).format('Начинаем в %HH:%MM (%DD)') // => Начинаем в 14:59 (СР)
         * ```
         */
        format(template) {
            if (this.exists()) {
                let from = result.from
                let timezoneBank = parseInt(workingHours.from.split('+')[1])
                let minutes = (from + 60 * timezoneBank)
                let day = Math.floor(minutes / 24 / 60)
                let resultDay
                switch (day) {
                    case 0: {
                        resultDay = 'ПН'
                        break
                    }
                    case 1: {
                        resultDay = 'ВТ'
                        break
                    }
                    case 2: {
                        resultDay = 'СР'
                        break
                    }
                    default:
                        throw Error("Плохой день")
                }
                let dayMinutes = (minutes - day * 24 * 60) % 60
                let dayHours = Math.floor((minutes - day * 24 * 60) / 60)
                return template
                    .replace(/%DD/gi, resultDay.toString())
                    .replace(/%HH/gi, dayHours.toString().padStart(2, "0"))
                    .replace(/%MM/gi, dayMinutes.toString().padStart(2, "0"))
            } else {
                return ""
            }
        },

        /**
         * Попробовать найти часы для ограбления позже [*]
         * @note Не забудь при реализации выставить флаг `isExtraTaskSolved`
         * @returns {boolean}
         */
        tryLater() {
            return false;
        }
    };
}

module.exports = {
    getAppropriateMoment
};
