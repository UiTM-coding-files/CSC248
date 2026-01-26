public class SampleLinkedList<E> {

    private Node<E> head, current, tail;

    public SampleLinkedList() {
        head = current = tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void addFirst(E data) {
        Node<E> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;

        if (tail == null) {
            tail = head;
        }
    }

    public void addLast(E data) {
        Node<E> newNode = new Node<>(data);

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    public E getFirst() {
        if (isEmpty()) {
            return null;
        }
        current = head;
        return current.data;
    }

    public E getLast() {
        if (isEmpty()) {
            return null;
        }
        return tail.data;
    }

    public E getNext() {
        if (current == null || current == tail) {
            return null;
        }
        current = current.next;
        return current.data;
    }

    public void clear() {
        head = current = tail = null;
    }

    public boolean contains(E data) {
        current = head;

        while (current != null) {
            if (data.equals(current.data)) {
                return true;
            }
            current = current.next; // ✅ FIX
        }
        return false;
    }

    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }

        current = head;
        head = head.next;

        if (head == null) {
            tail = null;
        }
        return current.data;
    }

    public E removeLast() {
        if (isEmpty()) {
            return null;
        }

        if (head == tail) {
            current = head;
            head = tail = null;
            return current.data;
        }

        current = head;
        while (current.next != tail) {
            current = current.next;
        }

        Node<E> temp = tail;
        tail = current;
        tail.next = null;
        return temp.data;
    }

    public E removeAfter(E data) {
        if (isEmpty() || head == tail) {
            return null;
        }

        Node<E> previous = head;
        while (previous != null && !data.equals(previous.data)) {
            previous = previous.next;
        }

        if (previous == null || previous.next == null) {
            return null;
        }

        current = previous.next;
        previous.next = current.next;

        if (current == tail) {
            tail = previous;
        }

        return current.data;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");

        current = head;
        while (current != null) {
            result.append(current.data);
            current = current.next;
            if (current != null) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
}