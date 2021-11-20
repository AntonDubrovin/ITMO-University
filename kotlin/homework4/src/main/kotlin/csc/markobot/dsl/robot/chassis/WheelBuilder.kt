package csc.markobot.dsl.robot.chassis

import csc.markobot.api.Chassis
import csc.markobot.dsl.MakroBotDsl

@MakroBotDsl
class WheelBuilder {
    var diameter = 1
    var count = 2

    fun build() = Chassis.Wheel(count, diameter)
}