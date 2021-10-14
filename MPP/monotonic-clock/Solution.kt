/**
 * В теле класса решения разрешено использовать только переменные делегированные в класс RegularInt.
 * Нельзя volatile, нельзя другие типы, нельзя блокировки, нельзя лазить в глобальные переменные.
 *
 * @author Dubrovin Anton
 */
class Solution : MonotonicClock {
    private var c1 by RegularInt(0)
    private var c2 by RegularInt(0)
    private var c3 by RegularInt(0)

    private var c4 by RegularInt(0)
    private var c5 by RegularInt(0)
    private var c6 by RegularInt(0)


    override fun write(time: Time) {
        // write right-to-left
        c4 = time.d1
        c5 = time.d2
        c6 = time.d3

        c3 = time.d3
        c2 = time.d2
        c1 = time.d1
    }

    override fun read(): Time {
        // read left-to-right
        val r11 = c1
        val r12 = c2
        val r13 = c3

        val r23 = c6
        val r22 = c5
        val r21 = c4

        if (r11 == r21) {
            return if (r12 == r22) {
                Time(r21, r22, r23)
            } else {
                Time(r21, r22, 0)
            }
        }
        return Time(r21, 0, 0)
    }
}