import com.h0tk3y.spbsu.kotlin.course.lesson1.cut
import com.h0tk3y.spbsu.kotlin.course.lesson1.intersectRanges
import com.h0tk3y.spbsu.kotlin.course.lesson1.test.cut as referenceCut
import com.h0tk3y.spbsu.kotlin.course.lesson1.test.intersectRanges as referenceIntersect
import org.junit.Assert
import org.junit.Test

class Test {
    @Test
    fun testIntersect() {
        fun doTest(rangeA: IntRange, rangeB: IntRange) {
            val expected = referenceIntersect(rangeA, rangeB)
            val actual = intersectRanges(rangeA, rangeB)
            Assert.assertEquals(
                    "Intersection of $rangeA and $rangeB should be $expected, but was $actual.",
                    expected,
                    actual
            )
        }

        doTest(0..0, 1..1)
        doTest(1..1, 0..0)
        doTest(0..5, 1..5)
        doTest(0..5, 0..4)
        doTest(0..5, 5..10)
        doTest(0..5, -10..0)
        doTest(0..5, 3..10)
        doTest(0..5, -5..3)
    }

    @Test
    fun testCut() {
        fun doTest(rangeA: IntRange, rangeB: IntRange) {
            val expected = referenceCut(rangeA, rangeB)
            val actual = cut(rangeA, rangeB)
            Assert.assertEquals(
                    "cut($rangeA, $rangeB) should be $expected, but was $actual.",
                    expected,
                    actual
            )
        }

        doTest(0..0, 1..1)
        doTest(1..1, 0..0)
        doTest(0..5, 1..5)
        doTest(0..5, 0..4)
        doTest(0..5, 5..10)
        doTest(0..5, -10..0)
        doTest(0..5, 3..10)
        doTest(0..5, -5..3)
    }


}