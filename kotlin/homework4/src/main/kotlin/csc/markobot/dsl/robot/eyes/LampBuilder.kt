package csc.markobot.dsl.robot.eyes

import csc.markobot.api.LampEye
import csc.markobot.dsl.MakroBotDsl

@MakroBotDsl
class LampBuilder {
    var count = 1
    var illumination = 1

    fun build() = List(count) { LampEye(illumination) }
}