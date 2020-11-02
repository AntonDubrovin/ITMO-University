package queue;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    // Pre: none
    public int size() {
        return size;
    }
    // Post: R = queue.length

    // Pre: none
    public boolean isEmpty() {
        return size == 0;
    }
    //Post: R = (queue.length == 0)
}
