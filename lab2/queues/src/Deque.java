import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node<Item> {
        Item node;
        Deque<Item>.Node<Item> next;
        Deque<Item>.Node<Item> pre;

        public Node(Item item) {
            node = item;
            next = null;
            pre = null;
        }
    }

    private Node<Item> first;
    private Node<Item> tail;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        tail = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null!");
        }

        if (size == 0) {
            first = tail = new Node<>(item);
        } else {
            Node<Item> oldFirst = first;
            first = new Node<>(item);

            first.next = oldFirst;
            oldFirst.pre = first;
        }

        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null!");
        }

        if (size == 0) {
            first = tail = new Node<>(item);
        } else {
            Node<Item> oldTail = tail;
            oldTail.next = new Node<>(item);

            tail = oldTail.next;
            tail.pre = oldTail;
        }

        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("size of deque is already zero!");
        }

        Node<Item> node = first;
        if(size == 1){
            first = null;
            tail = null;
        }else {
            first = first.next;

            first.pre = null;
            node.next = null;
        }

        size--;
        return node.node;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("size of deque is already zero!");
        }
        Node<Item> node = tail;
        if(size == 1){
            first = null;
            tail = null;
        }else {
            tail = tail.pre;

            tail.next = null;
            node.pre = null;
        }

        size--;
        return node.node;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node<Item> current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("iterator is already ending!");
            }

            Item item = current.node;
            current = current.next;

            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not implement!");
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(2);
        deque.addLast(3);
        StdOut.println("dequeue: " + deque.removeLast());
        StdOut.println("dequeue: " + deque.removeLast());
        StdOut.println("dequeue: " + deque.removeLast());

        StdOut.println("iterator");
//        Iterator<Integer> it = deque.iterator();
//        while (it.hasNext()) {
//            StdOut.println(it.next());
//        }


    }

}