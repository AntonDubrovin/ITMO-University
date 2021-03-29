package info.kgeorgiy.ja.dubrovin.arrayset;

import java.util.*;

public class ArraySet<T> extends AbstractSet<T> implements NavigableSet<T> {
    // :NOTE: should be final
    private final List<T> elements;
    private final Comparator<? super T> comparator;
    private boolean reverse = false;

    // :NOTE: call this()
    public ArraySet() {
        this(Collections.emptyList(), null);
    }

    // :NOTE: memory overhead
    public ArraySet(Collection<? extends T> collection) {
        this(collection, null);
    }

    public ArraySet(Collection<? extends T> collection, Comparator<? super T> comparator) {
        this.comparator = comparator;
        TreeSet<T> treeSet = new TreeSet<T>(comparator);
        treeSet.addAll(collection);
        elements = new ArrayList<>(treeSet);
    }

    private int findIndex(T element, boolean fromHead, boolean including) {
        int index = Collections.binarySearch(elements, element, comparator);
        return fromHead ?
                (index < 0 ? ~index : (including ? index : index + 1))
                :
                (index < 0 ? ~index - 1 : (including ? index : index - 1));
    }

    @Override
    public T lower(T t) {
        int index = findIndex(t, false, false);
        return index >= 0 ? elements.get(index) : null;
    }

    @Override
    public T floor(T t) {
        int index = findIndex(t, false, true);
        return index >= 0 ? elements.get(index) : null;
    }

    @Override
    public T ceiling(T t) {
        int index = findIndex(t, true, true);
        return index < size() ? elements.get(index) : null;
    }

    @Override
    public T higher(T t) {
        int index = findIndex(t, true, false);
        return index < size() ? elements.get(index) : null;
    }

    @Override
    public T pollFirst() {
        throw new UnsupportedOperationException("Set is constant");
    }

    @Override
    public T pollLast() {
        throw new UnsupportedOperationException("Set is constant");
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(elements).iterator();
    }

    // :NOTE: descendingSet on reversed collection?
    // :NOTE: changed reverse
    @Override
    public NavigableSet<T> descendingSet() {
        DescendingList<T> descendingList = new DescendingList<>(elements, reverse);
        reverse = descendingList.reverse();
        return new ArraySet<>(descendingList, Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<T> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        // :NOTE: NPE?
        if (comparator != null && comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        } else if (comparator == null && fromElement instanceof Comparable && toElement instanceof Comparable) {
            if (((Comparable<T>) fromElement).compareTo(toElement) > 0) {
                throw new IllegalArgumentException();
            }
        }
        int indexFrom = findIndex(fromElement, true, fromInclusive);
        int indexTo = findIndex(toElement, false, toInclusive) + 1;
        if (indexTo + 1 == indexFrom) {
            indexTo = indexFrom;
        }
        return new ArraySet<>(elements.subList(indexFrom, indexTo), comparator);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        int index = findIndex(toElement, false, inclusive) + 1;
        return new ArraySet<>(elements.subList(0, index), comparator);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        int index = findIndex(fromElement, true, inclusive);
        return new ArraySet<>(elements.subList(index, size()), comparator);
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public T first() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        return elements.get(0);
    }

    @Override
    public T last() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("Set is empty");
        }
        return elements.get(size() - 1);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean contains(Object o) {
        return Collections.binarySearch(elements, (T) o, comparator) >= 0;
    }
}
