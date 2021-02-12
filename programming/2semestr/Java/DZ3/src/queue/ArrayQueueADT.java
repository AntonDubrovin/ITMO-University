package queue;

import java.util.Arrays;
import java.util.function.ObjLongConsumer;

//Pre: none
public class ArrayQueueADT {
    private int start = 0, size = 0;
    public Object[] queue = new Object[16];
    // Post : queue.length = 0;

    // Pre: none
    public static int size(ArrayQueueADT transmittedQueue) {
        return transmittedQueue.size;
    }
    // Post: R = queue.length

    // Pre: none
    public static boolean isEmpty(ArrayQueueADT transmittedQueue) {
        return size(transmittedQueue) == 0;
    }
    //Post: R = (queue.length == 0)

    // Pre: none
    public static void clear(ArrayQueueADT transmittedQueue) {
        transmittedQueue.queue = new Object[16];
        transmittedQueue.start = 0;
        transmittedQueue.size = 0;
    }
    // Post: queue.length = 0

    // Pre: element != null
    public static void enqueue(ArrayQueueADT transmittedQueue, Object element) {
        changeSize(transmittedQueue);
        transmittedQueue.queue[(transmittedQueue.start + size(transmittedQueue)) % transmittedQueue.queue.length] = element;
        transmittedQueue.size++;
    }
    //Post: queue'.length = queue.length + 1 && queue[n + 1] = element

    // Pre: none
    public static void changeSize(ArrayQueueADT transmittedQueue) {
        if (size(transmittedQueue) < transmittedQueue.queue.length) {
            return;
        }
        transmittedQueue.queue = Arrays.copyOf(toArray(transmittedQueue), 2 * size(transmittedQueue));
        transmittedQueue.start = 0;
    }
    // Post: queue'.length = 2 * queue.length

    // Pre: queue.length > 0
    public static Object element(ArrayQueueADT transmittedQueue) {
        return transmittedQueue.queue[transmittedQueue.start];
    }
    // Post: R = queue[0]

    // Pre: queue.length > 0
    public static Object dequeue(ArrayQueueADT transmittedQueue) {
        Object tmp = element(transmittedQueue);
        transmittedQueue.queue[transmittedQueue.start] = null;
        transmittedQueue.start = (transmittedQueue.start + 1) % transmittedQueue.queue.length;
        transmittedQueue.size--;
        return tmp;
    }
    // Post: R = queue[0] && queue'.length = queue.length - 1

    // Pre: none
    public static Object[] toArray(ArrayQueueADT transmittedQueue) {
        Object[] returnedArray = new Object[size(transmittedQueue)];
        if (size(transmittedQueue) != 0){
            int end = (transmittedQueue.start + size(transmittedQueue)) % transmittedQueue.queue.length;
            if (transmittedQueue.start < end) {
                System.arraycopy(transmittedQueue.queue, transmittedQueue.start, returnedArray, 0, size(transmittedQueue));
            } else {
                System.arraycopy(transmittedQueue.queue, transmittedQueue.start, returnedArray, 0,
                        transmittedQueue.queue.length - transmittedQueue.start);
                System.arraycopy(transmittedQueue.queue, 0, returnedArray, transmittedQueue.queue.length - transmittedQueue.start, end);
            }
        }
        return returnedArray;
    }
    // Post: R = queue
}
