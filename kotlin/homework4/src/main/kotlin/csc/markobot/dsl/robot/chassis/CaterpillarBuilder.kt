package csc.markobot.dsl.robot.chassis

import csc.markobot.api.Chassis
import csc.markobot.dsl.MakroBotDsl

@MakroBotDsl
class CaterpillarBuilder {
    var width = 1

    fun build() = Chassis.Caterpillar(width)
}