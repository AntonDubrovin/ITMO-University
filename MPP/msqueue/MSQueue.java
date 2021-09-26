package msqueue;

import kotlinx.atomicfu.AtomicRef;

public class MSQueue implements Queue {
    private final AtomicRef<Node> head;
    private final AtomicRef<Node> tail;

    public MSQueue() {
        Node dummy = new Node(0);
        this.head = new AtomicRef<>(dummy);
        this.tail = new AtomicRef<>(dummy);
    }

    @Override
    public void enqueue(int x) {
        final Node newNode = new Node(x);
        Node curTail;
        while (true) {
            curTail = tail.getValue();
            final Node next = curTail.next.getValue();
            if (tail.getValue() == curTail) {
                if (next == null) {
                    if (curTail.next.compareAndSet(null, newNode)) {
                        break;
                    }
                } else {
                    tail.compareAndSet(curTail, next);
                }
            }
        }
        tail.compareAndSet(curTail, newNode);
    }

    @Override
    public int dequeue() {
        while (true) {
            final Node curHead = head.getValue();
            final Node curTail = tail.getValue();
            final Node next = curHead.next.getValue();
            if (curHead == head.getValue()) {
                if (curHead == curTail) {
                    if (next == null) {
                        return Integer.MIN_VALUE;
                    }
                    tail.compareAndSet(curTail, next);
                } else {
                    if (head.compareAndSet(curHead, next)) {
                        return next.x;
                    }
                }
            }
        }
    }

    @Override
    public int peek() {
        Node next = head.getValue().next.getValue();
        if (next == null)
            return Integer.MIN_VALUE;
        return next.x;
    }

    private class Node {
        final int x;
        AtomicRef<Node> next;

        Node(int x) {
            this.next = new AtomicRef<>(null);
            this.x = x;
        }
    }
}