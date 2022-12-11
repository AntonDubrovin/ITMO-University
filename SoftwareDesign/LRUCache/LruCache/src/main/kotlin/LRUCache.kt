import java.util.*
import kotlin.collections.HashMap

class LRUCache<K, V>(private val size: Int) {
    private val cacheMap = HashMap<K?, V?>()
    private val cacheList = LinkedList<K?>()
    private var cacheSize = 0

    init {
        assert(size > 0) { "Incorrect initial size $size" }
    }

    fun add(key: K?, value: V?) {
        // assert key and value must be not null
        assert(key != null) { "Incorrect add key $key" }
        assert(value != null) { "Incorrect add value $value" }
        val sizeBeforeAdd = cacheSize
        // if map contains key just replace the value and add key to head of linked list
        if (cacheMap.containsKey(key)) {
            cacheMap[key] = value
            cacheList.addFirst(key)
            cacheList.remove(key)
        } else {
            // if not contains add value to hash map and add to head of linked list
            cacheMap[key] = value
            cacheList.addFirst(key)
            // if maximum size reached, remove last LL element
            if (cacheSize == size) {
                cacheMap.remove(cacheList.last)
                cacheList.removeLast()
            } else {
                // else increment size
                cacheSize++
            }
        }

        // assert cacheSize must be in 0 to max size and must be greater or eq to size before added
        assert(cacheSize in 0..size && cacheSize >= sizeBeforeAdd) { "Incorrect cache size $cacheSize" }
    }

    fun get(key: K?): V? {
        // assert key must be not null
        assert(key != null) { "Incorrect add key $key" }
        val value = cacheMap[key]
        // if no such element - return null
        return if (value == null) {
            null
        } else {
            // else return value and move key to head of LL
            cacheList.remove(key)
            cacheList.addFirst(key)
            value
        }
    }

    fun exists(key: K?, value: V?): Boolean {
        // assert key and value must be not null
        assert(key != null) { "Incorrect add key $key" }
        assert(value != null) { "Incorrect add value $value" }
        // get element by key and check is it null or value element
        return get(key) == value
    }

    fun getSize(): Int {
        // return current cache size
        return cacheSize
    }
}