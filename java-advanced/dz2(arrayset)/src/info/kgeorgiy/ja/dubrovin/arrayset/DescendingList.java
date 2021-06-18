package info.kgeorgiy.ja.dubrovin.arrayset;

import java.util.AbstractList;
import java.util.RandomAccess;
import java.util.List;

public class DescendingList<T> extends AbstractList<T> implements RandomAccess {
    private final List<T> elements;
    private boolean reverse;

    public DescendingList(List<T> list, boolean reverse) {
        elements = list;
        this.reverse = reverse;
    }

    public boolean reverse() {
        return reverse = !reverse;
    }

    @Override
    public T get(int index) {
        return reverse ? elements.get(size() - index - 1) : elements.get(index);
    }

    @Override
    public int size() {
        return elements.size();
    }
}

