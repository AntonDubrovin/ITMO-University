package csc.markobot.dsl

import csc.markobot.api.LoadClass
import csc.markobot.api.MakroBot
import csc.markobot.dsl.robot.RobotBuilder

fun robot(name: String, lambda: RobotBuilder.() -> Unit): MakroBot {
    return RobotBuilder().apply(lambda).build(name)
}

val robot = robot("Wall-E") {
    head {
        plastic thickness 2

        eyes {
            lamps {
                count = 2
                illumination = 10
            }
            leds {
                count = 1
                illumination = 3
            }
        }

        mouth {
            speaker {
                power = 3
            }
        }
    }

    body {
        metal thickness 1

        inscription {
            +"I don't want to survive"
            +"I want live"
        }
    }

    hands {
        plastic thickness 3
        load = LoadClass.Light - LoadClass.Medium
    }

    chassis = caterpillar width 10
}