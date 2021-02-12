package queue;

public interface Queue {
    // Pre: none
    void clear();
    // Post: size = 0

    // Pre: element != null
    void enqueue(Object element);
    // Post: queue[size] = element && size' = size + 1

    // Pre: size > 0
    Object element();
    // Post: R = queue[0]

    // Pre: size > 0
    Object dequeue();
    // Post: R = queue[0] && size' = size - 1

    // Pre: none
    Object[] toArray();
    // Post: R = queue
}
