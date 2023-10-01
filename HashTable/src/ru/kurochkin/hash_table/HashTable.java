package ru.kurochkin.hash_table;

import java.util.*;

public class HashTable<E> implements Collection<E> {
    private final int DEFAULT_SIZE = 1000;

    private LinkedList<E>[] items;
    private int size;
    private int modCount;

    public HashTable(int size) {
        createItems(size);
    }

    public HashTable() {
        createItems(DEFAULT_SIZE);
    }

    public HashTable(List<E> list) {
        createItems(Math.max(DEFAULT_SIZE, list.size()));

        addAll(list);
    }

    private class HashTableIterator implements Iterator<E> {
        private int originalModCount;
        private int currentIndex = -1;
        private boolean isNextReturned;
        private ListIterator<E> itemIterator;

        public HashTableIterator() {
            originalModCount = modCount;
            isNextReturned = false;
        }

        private int getNextIndex() {
            for (int i = currentIndex + 1; i < items.length; i++) {
                if (items[i] != null) {
                    return i;
                }
            }

            return -1;
        }

        private void setNext() {
            currentIndex = getNextIndex();
            itemIterator = items[currentIndex].listIterator();
        }

        private void checkModifications() {
            if (originalModCount != modCount) {
                throw new ConcurrentModificationException("Список изменен, во время обхода");
            }
        }

        @Override
        public boolean hasNext() {
            if (itemIterator != null) {
                if (itemIterator.hasNext()) {
                    return true;
                }
            }

            return getNextIndex() >= 0;
        }

        @Override
        public E next() {
            checkModifications();

            if (!hasNext()) {
                throw new NoSuchElementException("Невозможно получить следующий элемент, достигнут конец списка");
            }

            if (currentIndex < 0) {
                setNext();
            }

            if (!itemIterator.hasNext()) {
                setNext();
            }

            isNextReturned = true;

            return itemIterator.next();
        }

        @Override
        public void remove() {
            if (currentIndex < 0) {
                throw new IndexOutOfBoundsException("Не получен текущий элемент списка");
            }

            if (!isNextReturned) {
                throw new IllegalStateException("Текущий элемент был удален");
            }

            itemIterator.remove();

            if (items[currentIndex].isEmpty()) {
                HashTable.this.remove(currentIndex);
            }

            originalModCount = modCount;

            isNextReturned = false;
        }
    }

    @SuppressWarnings("unchecked")
    private void createItems(int size) {
        items = new LinkedList[size];
    }

    private int getIndex(E item) {
        if (item == null) {
            return 0;
        }

        return Math.abs(item.hashCode() % items.length - 1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object item) {
        if (size == 0) {
            return false;
        }

        return items[getIndex((E) item)] != null;
    }

    @Override
    public Iterator<E> iterator() {
        return new HashTableIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];

        int currentIndex = 0;

        for (List<E> item : items) {
            if (item != null) {
                System.arraycopy(item.toArray(), 0, array, currentIndex, item.size());

                currentIndex += item.size();
            }
        }

        return array;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        if (array.length < size) {
            //noinspection unchecked
            return (T[]) Arrays.copyOfRange(toArray(), 0, size, array.getClass());
        }

        //noinspection unchecked
        System.arraycopy((T[]) toArray(), 0, array, 0, size);

        if (array.length > size) {
            array[size] = null;
        }

        return array;
    }

    @Override
    public boolean add(E item) {
        int index = getIndex(item);

        if (items[index] == null) {
            items[index] = new LinkedList<>();
        }

        items[index].add(item);

        modCount++;
        size++;

        return true;
    }

    private void remove(int index) {
        items[index] = null;
        modCount++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object item) {
        int index = getIndex((E) item);

        if (items[index] == null) {
            return false;
        }

        if (items[index].size() > 1) {
            items[index].remove(item);
        } else {
            items[index] = null;
        }

        modCount++;

        return true;
    }

    public E get(int index) {
        if (index < 0 || index >= items.length) {
            throw new IndexOutOfBoundsException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + (items.length - 1) + "]");
        }

        if (items[index] == null) {
            throw new NoSuchElementException("По индексу " + index + " нет значения");
        }

        return items[index].getFirst();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection.isEmpty()) {
            return false;
        }

        for (Object item : collection) {
            if (!contains(item)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }

        for (E item : collection) {
            if (!add(item)) {
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (collection.isEmpty()) {
            return false;
        }

        boolean isDeleted = false;

        for (Object item : collection) {
            while (remove(item)) {
                isDeleted = true;
            }
        }

        return isDeleted;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        if (size == 0) {
            return false;
        }

        boolean isChanged = false;

        for (List<E> item : items) {
            if (item != null) {
                if (item.retainAll(collection)) {
                    isChanged = true;

                    modCount++;
                }
            }
        }

        return isChanged;
    }

    @Override
    public void clear() {
        Arrays.fill(items, null);

        size = 0;
        modCount = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('[');

        for (LinkedList<E> item : items) {
            if (item != null) {
                if (item.size() > 1) {
                    sb.append(Arrays.toString(item.toArray()));
                } else {
                    sb.append(item.getFirst());
                }

                sb.append(", ");
            }
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        return sb.toString();
    }
}
