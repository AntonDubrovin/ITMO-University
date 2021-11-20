package csc.markobot.dsl.robot.mouth

import csc.markobot.api.Mouth
import csc.markobot.api.Speaker
import csc.markobot.dsl.MakroBotDsl

@MakroBotDsl
class MouthBuilder {
    private var speaker = Speaker(0)

    fun speaker(lambda: SpeakerBuilder.() -> Unit) {
        speaker = SpeakerBuilder().apply(lambda).build()
    }

    fun build() = Mouth(speaker)
}