package csc.markobot.dsl.robot.head

import csc.markobot.api.*
import csc.markobot.dsl.MakroBotDsl
import csc.markobot.dsl.robot.eyes.EyesBuilder
import csc.markobot.dsl.robot.mouth.MouthBuilder

@MakroBotDsl
class HeadBuilder {
    var plastic: Plastik = Plastik(1)
    var metal = Metal(1)
    var eyes = mutableListOf<Eye>()
    var mouth = Mouth(null)

    infix fun Plastik.thickness(thickness: Int) {
        plastic = Plastik(thickness)
    }

    infix fun Metal.thickness(thickness: Int) {
        metal = Metal(thickness)
    }

    fun eyes(lambda: EyesBuilder.() -> Unit) {
        eyes = EyesBuilder().apply(lambda).build()
    }

    fun mouth(lambda: MouthBuilder.() -> Unit) {
        mouth = MouthBuilder().apply(lambda).build()
    }

    fun build() = Head(if (plastic.thickness == -1) metal else plastic, eyes, mouth)
}