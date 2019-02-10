/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int count;

    public RandomizedQueue() {
        this.q = (Item[]) new Object[1];
        this.count = 0;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public int size() {
        return this.count;
    }

    public void enqueue(Item item) {
        checkValidArguement(item);
        if (this.count == q.length) {
            resize(q.length * 2);
        }
        q[count] = item;
        count++;
    }

    private void resize(int newCapacity) {
        int currentLastItemIndex = this.count;
        Item[] newQ = (Item[]) new Object[newCapacity];
        for (int i = 0; i < currentLastItemIndex; i++) {
            newQ[i] = q[i];
        }
        q = newQ;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(this.count);
        Item item = q[index];
        q[index] = q[this.count - 1];
        q[this.count - 1] = null;
        this.count--;
        if (this.count == 0 || (this.q.length / this.count == 4)) {
            resize(this.q.length / 2);
        }
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(this.count);
        return q[index];
    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {

        private int currentIndex;
        private Item[] shuffledQ;

        public ArrayIterator() {
            Item[] shuffledQ = (Item[]) new Object[count];
            for (int i = 0; i < count; i++) shuffledQ[i] = q[i];
            StdRandom.shuffle(shuffledQ);
            this.currentIndex = 0;
            this.shuffledQ = shuffledQ;
        }

        public boolean hasNext() {
            return currentIndex < shuffledQ.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = shuffledQ[currentIndex];
            currentIndex++;
            return item;
        }
    }

    private void checkValidArguement(Item item) {
        if (item == null) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
    }

}
