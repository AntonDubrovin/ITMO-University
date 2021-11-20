@file:Suppress("LocalVariableName", "NonAsciiCharacters")

package csc.makrobot.dsl

import csc.markobot.api.*
import csc.markobot.api.WeekDay.*
import csc.markobot.dsl.*

fun main() {

    val волли = MakroBot("Wall-E",
        Head(Plastik(2), listOf(LampEye(10), LampEye(10)), Mouth(Speaker(3))),
        Body(Metal(1), listOf("I don't want to survive.", "I want live.")),
        Hands(Plastik(3), LoadClass.Light, LoadClass.Medium),
        Chassis.Caterpillar(10)
    )

/*    val воллиЧерезDSL = робот("Wall-E") {
        голова {
            пластик толщиной 2

            глаза {
                диоды {
                    количество = 2
                    яркость = 10
                }
                лампы {
                    количество = 1
                    яркость = 40
                }
            }

            рот {
                динамик {
                    мощность = 3
                }
            }
        }

        туловище {
            металл толщиной 1

            надпись {
                +"I don't want to survive."
                +"I want live."
            }
        }

        руки {
            пластик толщиной 3
            нагрузка = легкая - средняя
        }

        // шасси = ноги
        шасси = гусеницы шириной 4
        *//*шасси = колеса {
            диаметр = 4
            количество = 2
        }*//*
    }*/

    сценарий {
        волли {                             // invoke operator overload
            speed = 2                       // initialization DSL
            power = 3
        }

        волли вперед 3                      // infix functions
        волли воспроизвести {
            +"Во поле береза стояла"
            +"Во поле кудрявая стояла"
        }
        волли.развернуться()
        волли назад 3

        расписание {                        // context-based high level function with context-lambda

            // волли вперед 3                // control methods availability with @DslMarker

            повторять(пн в 10, вт в 12)     // typealias, infix functions, vararg
            кроме(13)
            повторять(ср..пт в 11)
        }

    }.запуститьСейчас()
        .сброситьРасписание()               // calls chaining
        .расписание {
            повторять(пт в 23)
        }

    val (name, speed) = волли               // destructuring declarations
}
