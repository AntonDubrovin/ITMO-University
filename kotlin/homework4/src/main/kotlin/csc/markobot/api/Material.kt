package csc.markobot.api

interface Material {
    val thickness: Int
}

data class Metal(override val thickness: Int) : Material

data class Plastik(override val thickness: Int) : Material
