package csc.markobot.dsl.robot

import csc.markobot.api.*
import csc.markobot.dsl.MakroBotDsl
import csc.markobot.dsl.robot.arms.ArmsBuilder
import csc.markobot.dsl.robot.body.BodyBuilder
import csc.markobot.dsl.robot.chassis.CaterpillarBuilder
import csc.markobot.dsl.robot.chassis.WheelBuilder
import csc.markobot.dsl.robot.head.HeadBuilder
@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class MakroBotDsl

@MakroBotDsl
class RobotBuilder {
    var head = Head(Plastik(1), listOf(), Mouth(null))
    var body = Body(Plastik(1), listOf())
    var arms = Hands(Plastik(1), LoadClass.Light, LoadClass.Enormous)
    var chassis: Chassis = Chassis.Caterpillar(4)
    val caterpillar = CaterpillarBuilder()
    val legs = Chassis.Legs

    fun head(lambda: HeadBuilder.() -> Unit) {
        head = HeadBuilder().apply(lambda).build()
    }

    fun body(lambda: BodyBuilder.() -> Unit) {
        body = BodyBuilder().apply(lambda).build()
    }

    fun hands(lambda: ArmsBuilder.() -> Unit) {
        arms = ArmsBuilder().apply(lambda).build()
    }

    infix fun CaterpillarBuilder.width(width: Int): Chassis {
        return this.apply {
            this.width = width
        }.build()
    }

    fun wheel(lambda: WheelBuilder.() -> Unit): Chassis {
        return WheelBuilder().apply(lambda).build()
    }

    fun build(name: String) = MakroBot(name, head, body, arms, chassis)
}