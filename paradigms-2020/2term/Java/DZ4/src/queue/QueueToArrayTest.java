package queue;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class QueueToArrayTest extends QueueTest<ArrayQueueToArrayTest.ToArrayQueue> {
    public QueueToArrayTest() {
        super(ArrayQueueToArrayTest.ToArrayQueue.class, ArrayQueueToArrayTest.ReferenceToArrayQueue::new);
    }

    public static void main(final String[] args) {
        new QueueToArrayTest().test();
    }
}