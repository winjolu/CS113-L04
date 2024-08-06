import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Test the DoubleLinkedList implementation here
    }
}

class DoubleLinkedList<E> implements List<E> {
    private Node<E> head; // Reference to the first node in the list
    private Node<E> tail; // Reference to the last node in the list
    private int size; // Number of elements in the list

    // Static inner class representing a node in the doubly linked list
    private static class Node<E> {
        E data; // Data stored in the node
        Node<E> next; // Reference to the next node
        Node<E> prev; // Reference to the previous node

        // Constructor to create a new node with the given data
        Node(E data) {
            this.data = data;
        }
    }

    // Default constructor to initialize an empty list
    public DoubleLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // Returns the number of elements in the list
    @Override
    public int size() {
        return size;
    }

    // Returns true if the list is empty, false otherwise
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns true if the list contains the specified element
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // Returns an iterator over the elements in the list
    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    // Returns an array containing all of the elements in the list
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next)
            result[i++] = x.data;
        return result;
    }

    // Returns an array containing all of the elements in the list; the runtime type of the returned array is that of the specified array
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (Node<E> x = head; x != null; x = x.next)
            result[i++] = x.data;

        if (a.length > size)
            a[size] = null;

        return a;
    }

    // Appends the specified element to the end of the list
    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    // Removes the first occurrence of the specified element from the list, if it is present
    @Override
    public boolean remove(Object o) {
        for (Node<E> x = head; x != null; x = x.next) {
            if (Objects.equals(o, x.data)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    // Removes all of the elements from the list
    @Override
    public void clear() {
        for (Node<E> x = head; x != null; ) {
            Node<E> next = x.next;
            x.data = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        head = tail = null;
        size = 0;
    }

    // Returns the element at the specified position in the list
    @Override
    public E get(int index) {
        checkElementIndex(index);
        return node(index).data;
    }

    // Replaces the element at the specified position in the list with the specified element
    @Override
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(index);
        E oldVal = x.data;
        x.data = element;
        return oldVal;
    }

    // Inserts the specified element at the specified position in the list
    @Override
    public void add(int index, E element) {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));
    }

    // Removes the element at the specified position in the list
    @Override
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    // Returns the index of the first occurrence of the specified element in the list, or -1 if the list does not contain the element
    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            if (Objects.equals(o, x.data))
                return index;
            index++;
        }
        return -1;
    }

    // Returns the index of the last occurrence of the specified element in the list, or -1 if the list does not contain the element
    @Override
    public int lastIndexOf(Object o) {
        int index = size;
        for (Node<E> x = tail; x != null; x = x.prev) {
            index--;
            if (Objects.equals(o, x.data))
                return index;
        }
        return -1;
    }

    // Returns a list iterator over the elements in the list
    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    // Returns a list iterator over the elements in the list (in proper sequence), starting at the specified position in the list
    @Override
    public ListIterator<E> listIterator(int index) {
        checkPositionIndex(index);
        return new ListItr(index);
    }

    // Inner class implementing ListIterator for the doubly linked list
    private class ListItr implements ListIterator<E> {
        private Node<E> lastReturned; // Last node returned by next() or previous()
        private Node<E> next; // Next node to be returned by next()
        private int nextIndex; // Index of the next element

        // Constructor to create a new iterator starting at the specified index
        ListItr(int index) {
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        // Returns true if this list iterator has more elements when traversing the list in the forward direction
        public boolean hasNext() {
            return nextIndex < size;
        }

        // Returns the next element in the list and advances the cursor position
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.data;
        }

        // Returns true if this list iterator has more elements when traversing the list in the reverse direction
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        // Returns the previous element in the list and moves the cursor position backwards
        public E previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();

            lastReturned = next = (next == null) ? tail : next.prev;
            nextIndex--;
            return lastReturned.data;
        }

        // Returns the index of the element that would be returned by a subsequent call to next()
        public int nextIndex() {
            return nextIndex;
        }

        // Returns the index of the element that would be returned by a subsequent call to previous()
        public int previousIndex() {
            return nextIndex - 1;
        }

        // Removes from the list the last element that was returned by next() or previous()
        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();

            Node<E> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
        }

        // Replaces the last element returned by next() or previous() with the specified element
        public void set(E e) {
            if (lastReturned == null)
                throw new IllegalStateException();
            lastReturned.data = e;
        }

        // Inserts the specified element into the list
        public void add(E e) {
            lastReturned = null;
            if (next == null)
                linkLast(e);
            else
                linkBefore(e, next);
            nextIndex++;
        }
    }

    // Links the specified element as the last element of the list
    private void linkLast(E e) {
        final Node<E> l = tail;
        final Node<E> newNode = new Node<>(e);
        tail = newNode;
        if (l == null)
            head = newNode;
        else {
            l.next = newNode;
            newNode.prev = l;
        }
        size++;
    }

    // Inserts the specified element before the specified non-null node in the list
    private void linkBefore(E e, Node<E> succ) {
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(e);
        newNode.next = succ;
        succ.prev = newNode;
        if (pred == null)
            head = newNode;
        else {
            pred.next = newNode;
            newNode.prev = pred;
        }
        size++;
    }

    // Unlinks the specified non-null node from the list
    private E unlink(Node<E> x) {
        final E element = x.data;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.data = null;
        size--;
        return element;
    }

    // Returns the node at the specified index
    Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = head;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = tail;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    // Checks if the specified index is within the range of existing elements
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    // Checks if the specified index is within the range of valid positions for operations like add
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    // Throws an exception if the specified index is not an existing element index
    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // Throws an exception if the specified index is not a valid position index
    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    // Appends all of the elements in the specified collection to the end of this list
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    // Inserts all of the elements in the specified collection into this list at the specified position
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    // Returns true if this list contains all of the elements of the specified collection
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    // Removes from this list all of its elements that are contained in the specified collection
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    // Retains only the elements in this list that are contained in the specified collection
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    // Returns a string representation of the list
    @Override
    public String toString() {
        if (head == null) return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Node<E> x = head; x != null; x = x.next) {
            sb.append(x.data);
            if (x.next != null)
                sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }
}