package csc.markobot.dsl.robot.arms

import csc.markobot.api.Hands
import csc.markobot.api.LoadClass
import csc.markobot.api.Metal
import csc.markobot.api.Plastik

class ArmsBuilder {
    var plastic = Plastik(1)
    var metal = Metal(1)
    var load = LoadClass.Light to LoadClass.Light

    operator fun LoadClass.minus(maxLoad: LoadClass): Pair<LoadClass, LoadClass> {
        return this to maxLoad
    }

    infix fun Plastik.thickness(thickness: Int) {
        plastic = Plastik(thickness)
    }

    infix fun Metal.thickness(thickness: Int) {
        metal = Metal(thickness)
    }

    fun build() = Hands(if (plastic.thickness == -1) metal else plastic, load.first, load.second)
}