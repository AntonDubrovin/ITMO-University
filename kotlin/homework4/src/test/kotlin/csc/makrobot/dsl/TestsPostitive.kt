@file:Suppress("LocalVariableName", "NonAsciiCharacters")

package csc.makrobot.dsl

import csc.markobot.api.*
import csc.markobot.dsl.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestsPostitive {

    @Test
    fun testNonDSL() {
        val робот = MakroBot("Wall-E",
            Head(Plastik(2), listOf(LampEye(10), LampEye(10), LedEye(3)), Mouth(Speaker(3))),
            Body(Metal(1), listOf("I don't want to survive.", "I want live.")),
            Hands(Plastik(3), LoadClass.Light, LoadClass.Medium),
            Chassis.Caterpillar(10)
        )
        verify(робот)
    }

    @Test
    fun testDSL() {
        val робот = robot("Wall-E") {
            head {
                plastic thickness  2

                eyes {
                    lamps {
                        count = 2
                        illumination = 10
                    }
                    leds {
                        count = 1
                        illumination = 3
                    }
                }

                mouth {
                    speaker {
                        power = 3
                    }
                }
            }

            body {
                metal thickness  1

                inscription {
                    +"I don't want to survive."
                    +"I want live."
                }
            }

            hands {
                plastic thickness  3
                load = LoadClass.Light - LoadClass.Medium
            }

            chassis = caterpillar width  10
        }

        verify(робот)
    }

    @Test
    fun testDSLOtherChassis() {
        val роботНаКолесах = robot("Wall-E") {
            head {
                plastic thickness  2

                eyes {
                    lamps {
                        count = 2
                        illumination = 10
                    }
                }

                mouth {
                    speaker {
                        power = 3
                    }
                }
            }

            body {
                metal thickness  1
            }

            hands {
                plastic thickness  3
                load = LoadClass.Light - LoadClass.Medium
            }
            chassis = wheel {
                diameter = 4
                count = 2
            }
        }

        Assertions.assertEquals(Chassis.Wheel(2, 4), роботНаКолесах.chassis)

        val роботНаНогах = robot("Wall-E") {
            head {
                plastic thickness  2

                eyes {
                    lamps {
                        count = 2
                        illumination = 10
                    }
                }

                mouth {
                    speaker {
                        power = 3
                    }
                }
            }

            body {
                metal thickness  1
            }

            hands {
                plastic thickness  3
                load = LoadClass.Light - LoadClass.Medium
            }
            chassis = legs
        }

        Assertions.assertEquals(Chassis.Legs, роботНаНогах.chassis)
    }

    private fun verify(робот: MakroBot) {
        Assertions.assertEquals("Wall-E", робот.name)
        Assertions.assertEquals(Plastik(2), робот.head.material)
        Assertions.assertArrayEquals(arrayOf(LampEye(10), LampEye(10), LedEye(3)), робот.head.eyes.toTypedArray())
        Assertions.assertEquals(Mouth(Speaker(3)), робот.head.mouth)

        Assertions.assertEquals(Metal(1), робот.body.material)
        Assertions.assertArrayEquals(arrayOf("I don't want to survive.", "I want live."), робот.body.strings.toTypedArray())

        Assertions.assertEquals(Plastik(3), робот.hands.material)
        Assertions.assertEquals(LoadClass.Light, робот.hands.minLoad)
        Assertions.assertEquals(LoadClass.Medium, робот.hands.maxLoad)

        Assertions.assertEquals(Chassis.Caterpillar(10), робот.chassis)
    }
}
