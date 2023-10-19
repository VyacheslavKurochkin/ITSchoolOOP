package ru.kurochkin.hash_table;

import java.util.*;

public class HashTable<E> implements Collection<E> {
    private static final int DEFAULT_ARRAY_LENGTH = 100;

    private LinkedList<E>[] lists;
    private int size;
    private int modCount;

    public HashTable(int arrayLength) {
        if (arrayLength < 1) {
            throw new IllegalArgumentException("Не допустимое значение " + arrayLength + " для размера таблицы. " +
                    "Допустимые значения начинаются с 0");
        }

        createLists(arrayLength);
    }

    public HashTable() {
        createLists(DEFAULT_ARRAY_LENGTH);
    }

    public HashTable(Collection<? extends E> collection) {
        createLists(Math.max(DEFAULT_ARRAY_LENGTH, collection.size()));

        addAll(collection);
    }

    private class HashTableIterator implements Iterator<E> {
        private int originalModCount;
        private int hashTableListsIndex = -1;
        private boolean isNextReturned;
        private int passedItemsCount;
        private ListIterator<E> listIterator;

        public HashTableIterator() {
            originalModCount = modCount;
        }

        private int getNextIndex() {
            for (int i = hashTableListsIndex + 1; i < lists.length; i++) {
                if (lists[i] != null) {
                    if (!lists[i].isEmpty()) {
                        return i;
                    }
                }
            }

            return -1;
        }

        private void setNext() {
            hashTableListsIndex = getNextIndex();
            listIterator = lists[hashTableListsIndex].listIterator();
        }

        private void checkModifications() {
            if (originalModCount != modCount) {
                throw new ConcurrentModificationException("Таблица изменена во время обхода");
            }
        }

        @Override
        public boolean hasNext() {
            return passedItemsCount < size;
        }

        @Override
        public E next() {
            checkModifications();

            if (!hasNext()) {
                throw new NoSuchElementException("Невозможно получить следующий элемент, достигнут конец списка");
            }

            if (hashTableListsIndex < 0) {
                setNext();
            }

            if (!listIterator.hasNext()) {
                setNext();
            }

            isNextReturned = true;
            passedItemsCount++;

            return listIterator.next();
        }

        @Override
        public void remove() {
            if (!isNextReturned) {
                throw new IllegalStateException("Не получен текущий элемент списка");
            }

            listIterator.remove();
            modCount++;

            isNextReturned = false;
        }
    }

    @SuppressWarnings("unchecked")
    private void createLists(int size) {
        lists = new LinkedList[size];
    }

    private int getIndex(Object object) {
        if (object == null) {
            return 0;
        }

        return Math.abs(object.hashCode() % lists.length);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object object) {
        int index = getIndex(object);

        return lists[index] != null && lists[index].contains(object);
    }

    @Override
    public Iterator<E> iterator() {
        return new HashTableIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];

        int i = 0;

        for (List<E> list : lists) {
            if (list != null) {
                for (E item : list) {
                    array[i] = item;

                    i++;
                }
            }
        }

        return array;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        if (array.length < size) {
            //noinspection unchecked
            return (T[]) Arrays.copyOf(toArray(), size, array.getClass());
        }

        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(toArray(), 0, array, 0, size);

        if (array.length > size) {
            array[size] = null;
        }

        return array;
    }

    @Override
    public boolean add(E data) {
        int index = getIndex(data);

        if (lists[index] == null) {
            lists[index] = new LinkedList<>();
        }

        lists[index].add(data);

        modCount++;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object object) {
        int index = getIndex(object);

        if (lists[index] == null) {
            return false;
        }

        if (lists[index].remove(object)) {
            size--;
            modCount++;

            return true;
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
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
            add(item);
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (collection.isEmpty()) {
            return false;
        }

        if (size == 0) {
            return false;
        }

        boolean isRemoved = false;

        for (Object item : collection) {
            while (remove(item)) {
                isRemoved = true;
            }
        }

        return isRemoved;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        if (size == 0) {
            return false;
        }

        boolean isChanged = false;

        for (List<E> list : lists) {
            if (list != null) {
                int listItemSize = list.size();

                if (list.retainAll(collection)) {
                    isChanged = true;

                    size -= listItemSize - list.size();
                }
            }
        }

        if (isChanged) {
            modCount++;
        }

        return isChanged;
    }

    @Override
    public void clear() {
        if (size == 0) {
            return;
        }

        Arrays.fill(lists, null);

        size = 0;
        modCount++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('[');

        for (LinkedList<E> list : lists) {
            sb.append(list);
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        return sb.toString();
    }
}
