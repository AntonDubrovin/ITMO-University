package queue;

import java.util.Arrays;
import java.util.function.ObjLongConsumer;

//Pre: none
public class ArrayQueueModule {
    private static int start = 0, size = 0;
    public static Object[] queue = new Object[16];
    // Post : queue.length = 0;

    // Pre: none
    public static int size() {
        return size;
    }
    // Post: R = queue.length

    // Pre: none
    public static boolean isEmpty() {
        return size() == 0;
    }
    //Post: R = (queue.length == 0)

    // Pre: none
    public static void clear() {
        queue = new Object[16];
        start = 0;
        size = 0;
    }
    // Post: queue.length = 0

    // Pre: element != null
    public static void enqueue(Object element) {
        changeSize();
        queue[(start + size()) % queue.length] = element;
        size++;
    }
    //Post: queue'.length = queue.length + 1 && queue[n + 1] = element

    // Pre: none
    public static void changeSize() {
        if (size() < queue.length) {
            return;
        }
        queue = Arrays.copyOf(toArray(), 2 * size());
        start = 0;
    }
    // Post: queue'.length = 2 * queue.length

    // Pre: queue.length > 0
    public static Object element() {
        return queue[start];
    }
    // Post: R = queue[0]

    // Pre: queue.length > 0
    public static Object dequeue() {
        Object delElem = element();
        queue[start] = null;
        start = (start + 1) % queue.length;
        size--;
        return delElem;
    }
    // Post: R = queue[0] && queue'.length = queue.length - 1

    // Pre: none
    public static Object[] toArray() {
        Object[] returnedArray = new Object[size];
        if (size() != 0){
            int end = (start + size()) % queue.length;
            if (start < end) {
                System.arraycopy(queue, start, returnedArray, 0, size());
            } else {
                System.arraycopy(queue, start, returnedArray, 0, queue.length - start);
                System.arraycopy(queue, 0, returnedArray, queue.length - start, end);
            }
        }
        return returnedArray;
    }
    // Post: R = queue
}
