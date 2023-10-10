package ru.kurochkin.hash_table;

import java.util.*;

public class HashTable<E> implements Collection<E> {
    private final int DEFAULT_SIZE = 100;

    private LinkedList<E>[] lists;
    private int size;
    private int modCount;

    public HashTable(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Не допустимое значение " + size + " для размера таблицы. " +
                    "Допустимые значения начинаются с 0");
        }

        createLists(size);
    }

    public HashTable() {
        createLists(DEFAULT_SIZE);
    }

    public HashTable(Collection<? extends E> collection) {
        createLists(Math.max(DEFAULT_SIZE, collection.size()));

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
                    return i;
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
            return passedItemsCount < HashTable.this.size;
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

            if (lists[hashTableListsIndex].isEmpty()) {
                HashTable.this.remove(hashTableListsIndex);
            }

            originalModCount = modCount;

            isNextReturned = false;
        }
    }

    @SuppressWarnings("unchecked")
    private void createLists(int size) {
        lists = new LinkedList[size];
    }

    private int getIndex(Object listItem) {
        if (listItem == null) {
            return 0;
        }

        return Math.abs(listItem.hashCode() % (lists.length - 1));
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
    public boolean contains(Object listItem) {
        int index = getIndex(listItem);

        if (lists[index] != null) {
            return lists[index].contains(listItem);
        }

        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new HashTableIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];

        int currentIndex = 0;

        for (List<E> listItem : lists) {
            if (listItem != null) {
                for (E item : listItem) {
                    array[currentIndex] = item;

                    currentIndex++;
                }
            }
        }

        return array;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        if (array.length < size) {
            //noinspection unchecked
            return Arrays.copyOf((T[]) toArray(), size);
        }

        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(toArray(), 0, array, 0, size);

        if (array.length > size) {
            array[size] = null;
        }

        return array;
    }

    @Override
    public boolean add(E listItem) {
        int index = getIndex(listItem);

        if (lists[index] == null) {
            lists[index] = new LinkedList<>();
        }

        lists[index].add(listItem);

        modCount++;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object listItem) {
        int index = getIndex(listItem);

        if (lists[index] == null) {
            return false;
        }

        if (!lists[index].isEmpty()) {
            if (lists[index].remove(listItem)) {
                size--;
                modCount++;

                return true;
            }
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
            if (!add(item)) {
                return false;
            }
        }

        return true;
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

        for (List<E> listItem : lists) {
            if (listItem != null) {
                int listItemSize = listItem.size();

                if (listItem.retainAll(collection)) {
                    isChanged = true;

                    size -= listItemSize - listItem.size();
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

        for (LinkedList<E> listItem : lists) {
            if (listItem != null) {
                sb.append(listItem);
                sb.append(", ");
            }
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        return sb.toString();
    }
}
