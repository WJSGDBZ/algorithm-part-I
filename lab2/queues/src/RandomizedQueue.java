import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[2];
        size = 0;
    }

    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[i];
        }

        items = newItems;
    }

    private void swap(Item[] items, int a, int b) {
        Item temp = items[a];
        items[a] = items[b];
        items[b] = temp;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("enqueue item is null");
        }
        if (size >= items.length) {
            resize(2 * items.length);
        }

        items[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException("number of samples is empty");
        }
        int select = StdRandom.uniformInt(size);
        swap(items, select, size - 1);

        Item item = items[--size];
        items[size] = null;
        if (size > 0 && size < items.length / 4) {
            resize(items.length / 2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException("number of samples is empty");
        }
        int select = StdRandom.uniformInt(size);

        return items[select];
    }

//    public Item sample_no_repeat(){
//        if(size == 0){
//            throw new NoSuchElementException("number of samples is empty");
//        }
//        int select = sampleIndex + StdRandom.uniformInt(size - sampleIndex);
//        Item item = items[select];
//
//        swap(items, sampleIndex, select);
//        sampleIndex = (sampleIndex+1) % size;
//
//        return item;
//    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator(items, size);
    }

    private class ArrayIterator implements Iterator<Item> {
        private Item[] samples;
        private int current;

        public ArrayIterator(Item[] items, int size) {
            int n = size;
            samples = (Item[]) new Object[n];
            current = 0;

            for (int i = 0; i < n; i++) {
                samples[i] = items[i];
            }

            StdRandom.shuffle(samples);
        }

        @Override
        public boolean hasNext() {
            return current < samples.length;
        }

        @Override
        public Item next() {
            if (current >= samples.length) {
                throw new NoSuchElementException("iterator is already ending!");
            }
            return samples[current++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not implement!");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();

        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
        }
        rq.dequeue();

        Iterator<Integer> it = rq.iterator();
        while (it.hasNext()) {
            StdOut.println(it.next());
        }

        StdOut.println("sample 10---------");
        for (int i = 0; i < 20; i++) {
            StdOut.println(rq.sample());
        }
    }

}
