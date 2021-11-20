package csc.markobot.dsl.robot.eyes

import csc.markobot.api.Eye
import csc.markobot.dsl.MakroBotDsl

@MakroBotDsl
class EyesBuilder {
    private var eyes = mutableListOf<Eye>()

    fun lamps(lambda: LampBuilder.() -> Unit) {
        LampBuilder().apply(lambda).build().forEach {
            eyes.add(it)
        }
    }

    fun leds(lambda: LedBuilder.() -> Unit) {
        LedBuilder().apply(lambda).build().forEach {
            eyes.add(it)
        }
    }

    fun build() = eyes
}