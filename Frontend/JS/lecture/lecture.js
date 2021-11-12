/**
 * Возвращает новый emitter
 * @returns {Object}
 */
function getEmitter() {
    return {
        subscribeMap: new Map(),
        /**
         * Подписаться на событие
         * @param {String} event
         * @param {Object} context
         * @param {Function} handler
         */
        on: function (event, context, handler) {
            this.subscribeMap.has(event)
                ? this.subscribeMap.get(event).push({context: context, handler: handler})
                : this.subscribeMap.set(event, [{context: context, handler: handler}])
            return this
        },

        /**
         * Отписаться от события
         * @param {String} event
         * @param {Object} context
         */
        off: function (event, context) {
            for (let curEvent of this.subscribeMap.keys()) {
                if (curEvent === event || curEvent.startsWith(event + '.')) {
                    let newValues = []
                    for (let values of this.subscribeMap.get(curEvent)) {
                        if (values.context !== context) {
                            newValues.push(values)
                        }
                    }
                    this.subscribeMap.set(curEvent, newValues)
                }
            }
            return this
        },

        /**
         * Уведомить о событии
         * @param {String} event
         */
        emit: function (event) {
            while (event !== '') {
                for (let curEvent of this.subscribeMap.keys()) {
                    if (curEvent === event) {
                        for (let values of this.subscribeMap.get(curEvent)) {
                            values.handler.call(values.context)
                        }
                    }
                }
                if (event.indexOf('.') !== -1) {
                    event = event.substr(0, event.lastIndexOf('.'))
                } else {
                    event = ''
                }
            }
            return this
        },

        /**
         * Подписаться на событие с ограничением по количеству полученных уведомлений
         * @star
         * @param {String} event
         * @param {Object} context
         * @param {Function} handler
         * @param {Number} times – сколько раз получить уведомление
         */
        several: function (event, context, handler, times) {
            console.info(event, context, handler, times);
        },

        /**
         * Подписаться на событие с ограничением по частоте получения уведомлений
         * @star
         * @param {String} event
         * @param {Object} context
         * @param {Function} handler
         * @param {Number} frequency – как часто уведомлять
         */
        through: function (event, context, handler, frequency) {
            console.info(event, context, handler, frequency);
        }
    };
}

module.exports = {
    getEmitter
};


let students = {
    Sam: {
        focus: 100,
        wisdom: 50
    },
    Sally: {
        focus: 100,
        wisdom: 60
    },
    Bill: {
        focus: 90,
        wisdom: 50
    },
    Sharon: {
        focus: 110,
        wisdom: 40
    }
};
