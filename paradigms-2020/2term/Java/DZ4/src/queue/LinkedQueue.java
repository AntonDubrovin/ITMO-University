package queue;


public class LinkedQueue extends AbstractQueue implements Queue {
    private Node end = new Node(null, null);
    private Node start = new Node(null, end);

    @Override
    public void clear() {
        end = new Node(null ,null);
        start = new Node(null, end);
        size = 0;
    }

    @Override
    public void enqueue(Object element) {
        Node tmp = new Node(element, end);
        if (size == 0) {
            start = tmp;
        } else {
            end.setNext(tmp);
        }
        end = tmp;
        size++;
    }

    @Override
    public Object element() {
        return start.getValue();
    }

    @Override
    public Object dequeue() {
        size--;
        Object peek = element();
        start = start.getNext();
        return peek;
    }
    
    @Override
    public Object[] toArray() {
        Object[] returnedArray = new Object[size];
        Node curNode = start;
        int ind = 0;
        while (curNode != end) {
            returnedArray[ind] = curNode.getValue();
            curNode.setNext(curNode.getNext());
        }
        return returnedArray;
    }
}
