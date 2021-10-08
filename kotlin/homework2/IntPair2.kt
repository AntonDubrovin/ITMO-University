abstract class IntPair2(var valueX: Int, var valueY: Int) {
    private constructor() : this(-1, -1)

    open fun sum(): Int {
        return valueX + valueY
    }

    fun prod(): Int {
        return valueX * valueY
    }

    abstract fun gcd(): Int
}

class DerivedIntPair2(valueX: Int, valueY: Int) : IntPair2(valueX, valueY) {
    override fun gcd(): Int {
        var first = valueX
        var second = valueY
        while (first != 0 && second != 0) {
            if (first > second) {
                first %= second
            } else {
                second %= first
            }
        }

        return first + second
    }
}