package csc.markobot.dsl.robot.body

import csc.markobot.dsl.MakroBotDsl

@MakroBotDsl
class InscriptionBuilder {
    var inscription = mutableListOf<String>()

    operator fun String.unaryPlus() {
        inscription.add(this)
    }

    fun build() = inscription
}