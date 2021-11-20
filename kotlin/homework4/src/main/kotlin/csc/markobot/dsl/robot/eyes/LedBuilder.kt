package csc.markobot.dsl.robot.eyes

import csc.markobot.api.LedEye
import csc.markobot.dsl.MakroBotDsl

@MakroBotDsl
class LedBuilder {
    var count = 1
    var illumination = 1

    fun build() = List(count) { LedEye(illumination) }
}