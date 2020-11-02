package queue;

import java.util.Arrays;
import java.util.function.ObjLongConsumer;

public class ArrayQueue extends AbstractQueue {
    private int start = 0;
    public Object[] queue = new Object[16];

    public void clear() {
        queue = new Object[16];
        start = 0;
        size = 0;
    }

    public void enqueue(Object element) {
        changeSize();
        queue[(start + size()) % queue.length] = element;
        size++;
    }

    public void changeSize() {
        if (size() < queue.length) {
            return;
        }
        queue = Arrays.copyOf(toArray(), 2 * size());
        start = 0;
    }

    public Object element() {
        return queue[start];
    }

    public Object dequeue() {
        Object delElem = element();
        queue[start] = null;
        start = (start + 1) % queue.length;
        size--;
        return delElem;
    }

    @Override
    public Object[] toArray() {
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
}
