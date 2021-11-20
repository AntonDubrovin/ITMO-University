package csc.markobot.dsl.robot.body

import csc.markobot.api.Body
import csc.markobot.api.Metal
import csc.markobot.api.Plastik
import csc.markobot.dsl.MakroBotDsl

@MakroBotDsl
class BodyBuilder {
    var inscription = mutableListOf<String>()
    var plastic = Plastik(-1)
    var metal = Metal(1)

    infix fun Plastik.thickness(thickness: Int) {
        plastic = Plastik(thickness)
    }

    infix fun Metal.thickness(thickness: Int) {
        metal = Metal(thickness)
    }

    fun inscription(lambda: InscriptionBuilder.() -> Unit) {
        InscriptionBuilder().apply(lambda).build().forEach {
            inscription.add(it)
        }
    }

    fun build() = Body(if (plastic.thickness == -1) metal else plastic, inscription)
}