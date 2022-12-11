package stack;

import kotlinx.atomicfu.AtomicIntArray;
import kotlinx.atomicfu.AtomicRef;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StackImpl implements Stack {
    private final int ARRAY_SIZE = 10;
    private final int WAITING_TIME = 100;
    private final int MIN_VALUE = Integer.MIN_VALUE;
    private final int DONE = Integer.MAX_VALUE;

    private final AtomicIntArray eliminationArray = new AtomicIntArray(ARRAY_SIZE);
    private final AtomicRef<Node> head = new AtomicRef<>(null);
    private final Random r = ThreadLocalRandom.current();

    public StackImpl() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            eliminationArray.get(i).setValue(MIN_VALUE);
        }
    }

    private boolean pushIndices(final int index, final int x) {
        if (eliminationArray.get(index).compareAndSet(MIN_VALUE, x)
                || eliminationArray.get(index).compareAndSet(DONE, x)) {
            for (int i = 0; i < WAITING_TIME; i++) {
                if (eliminationArray.get(index).getValue() == DONE) {
                    return true;
                }
            }

            return !eliminationArray.get(index).compareAndSet(x, MIN_VALUE);
        }
        return false;
    }

    private void noOptimizationPush(final int x) {
        while (true) {
            final Node curHead = head.getValue();
            final Node newNode = new Node(x, curHead);
            if (head.compareAndSet(curHead, newNode)) {
                return;
            }
        }
    }

    @Override
    public void push(final int x) {
        final int index = r.nextInt(ARRAY_SIZE);

        if (pushIndices(index, x)) {
            return;
        }

        if (index - 1 >= 0 && pushIndices(index - 1, x)) {
            return;
        }

        if (index + 1 < ARRAY_SIZE && pushIndices(index + 1, x)) {
            return;
        }

        noOptimizationPush(x);
    }

    private Integer popIndices(final int index) {
        final int element = eliminationArray.get(index).getValue();
        if (element != MIN_VALUE && element != DONE) {
            if (eliminationArray.get(index).compareAndSet(element, DONE)) {
                return element;
            }
        }
        return null;
    }

    private int noOptimizationPop() {
        while (true) {
            final Node curHead = head.getValue();
            if (curHead != null) {
                if (head.compareAndSet(curHead, curHead.next.getValue())) {
                    return curHead.x;
                }
            } else {
                return MIN_VALUE;
            }
        }
    }

    @Override
    public int pop() {
        final int index = r.nextInt(ARRAY_SIZE);
        Integer element = popIndices(index);

        if (element != null) {
            return element;
        }
        if (index - 1 >= 0) {
            element = popIndices(index - 1);
            if (element != null) {
                return element;
            }
        }
        if (index + 1 < ARRAY_SIZE) {
            element = popIndices(index + 1);
            if (element != null) {
                return element;
            }
        }

        return noOptimizationPop();
    }

    private static class Node {
        final int x;
        final AtomicRef<Node> next;

        Node(final int x, final Node next) {
            this.next = new AtomicRef<>(next);
            this.x = x;
        }
    }
}
