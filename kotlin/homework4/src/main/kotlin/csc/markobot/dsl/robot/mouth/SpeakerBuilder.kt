package csc.markobot.dsl.robot.mouth

import csc.markobot.api.Speaker
import csc.markobot.dsl.MakroBotDsl

@MakroBotDsl
class SpeakerBuilder {
    var power = 1

    fun build() = Speaker(power)
}