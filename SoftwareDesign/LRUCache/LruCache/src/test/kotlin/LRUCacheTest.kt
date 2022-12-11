import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.Integer.min
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertNull

class LRUCacheTest {
    @Test
    fun testOneElement() {
        val cache = LRUCache<String, String>(2)
        val key = "testOneElementKey"
        val value = "testOneElementKeyValue"
        cache.add(key, value)

        assertEquals(cache.getSize(), 1)
        assertEquals(cache.get(key), value)
        assertTrue(cache.exists(key, value))
    }

    @Test
    fun testManyElements() {
        val cache = LRUCache<String, String>(10)
        for (i in 0 until 10) {
            val key = "testManyElements${i}key"
            val value = "testManyElements${i}value"
            cache.add(key, value)

            assertEquals(cache.getSize(), i + 1)
            assertEquals(cache.get(key), value)
            assertTrue(cache.exists(key, value))
        }
    }

    @Test
    fun testManyElementsWhenElementMoreThenSize() {
        val cache = LRUCache<String, String>(10)
        for (i in 0..20) {
            val key = "testManyElements${i}key"
            val value = "testManyElements${i}value"
            cache.add(key, value)

            assertEquals(cache.getSize(), min(10, i + 1))
            assertEquals(cache.get(key), value)
            assertTrue(cache.exists(key, value))
        }

        for (i in 0..10) {
            val key = "testManyElements${i}key"
            val value = "testManyElements${i}value"

            assertNull(cache.get(key))
            assertFalse(cache.exists(key, value))
        }

        for (i in 11..20) {
            val key = "testManyElements${i}key"
            val value = "testManyElements${i}value"

            assertNotNull(cache.get(key))
            assertTrue(cache.exists(key, value))
        }
    }

    @Test
    fun testAddNullKey() {
        val cache = LRUCache<String, String>(2)
        val key = null
        val value = "testNullKeyValue"

        assertFails { cache.add(key, value) }
    }

    @Test
    fun testAddNullValue() {
        val cache = LRUCache<String, String>(2)
        val key = "testNullKeyKey"
        val value = null

        assertFails { cache.add(key, value) }
    }

    @Test
    fun testGetNullKey() {
        val cache = LRUCache<String, String>(2)
        val key = null

        assertFails { cache.get(key) }
    }
}