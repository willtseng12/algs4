/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int count;

    public Deque() {
        this.first = null;
        this.last = null;
        this.count = 0;
    }

    private class Node {
        Item item;
        Node next;
        Node prev;

        Node(Item item) {
            this.item = item;
            this.next = null;
            this.prev = null;
        }
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public void addFirst(Item item) {
        checkValidArguement(item);
        if (this.count == 0) {
            this.first = new Node(item);
            this.last = this.first;
        }
        else {
            Node oldFirst = first;
            this.first = new Node(item);
            this.first.next = oldFirst;
            oldFirst.prev = this.first;
        }
        count++;
    }

    public void addLast(Item item) {
        checkValidArguement(item);
        if (this.count == 0) {
            this.last = new Node(item);
            this.first = this.last;
        }
        else {
            Node oldLast = last;
            this.last = new Node(item);
            oldLast.next = this.last;
            this.last.prev = oldLast;
        }
        count++;
    }

    public Item removeFirst() {
        if (this.count == 0) throw new NoSuchElementException();
        Node oldFirst = first;
        if (this.count == 1) {
            this.first = null;
            this.last = null;
        }
        else {
            this.first = this.first.next;
            this.first.prev = null;
        }
        count--;
        return oldFirst.item;
    }

    public Item removeLast() {
        if (this.count == 0) throw new NoSuchElementException();
        Node oldLast = last;
        if (this.count == 1) {
            this.first = null;
            this.last = null;
        }
        else {
            this.last = this.last.prev;
            this.last.next = null;
        }
        count--;
        return oldLast.item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current;

        public DequeIterator() {
            this.current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    private void checkValidArguement(Item item) {
        if (item == null) throw new IllegalArgumentException();
    }

    private void checkItemsRemaining(Node node) {
        if (node == null) {
            throw new NoSuchElementException();
        }
    }

    public static void main(String[] args) {
    }
}
