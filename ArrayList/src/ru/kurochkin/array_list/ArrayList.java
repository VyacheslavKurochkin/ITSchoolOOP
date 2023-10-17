package ru.kurochkin.array_list;

import java.util.*;
import java.util.function.UnaryOperator;

public class ArrayList<E> implements List<E> {
    private static final int DEFAULT_CAPACITY = 10;

    private E[] items;
    private int size;
    private int modCount;

    public ArrayList() {
        createNewItems(DEFAULT_CAPACITY);
    }

    public ArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Не допустимое значение " + capacity + " для размера списка. " +
                    "Допустимые значения начинаются с 0");
        }

        createNewItems(capacity);
    }

    @SuppressWarnings("unchecked")
    public ArrayList(List<E> list) {
        size = list.size();

        items = (E[]) list.toArray();
    }

    private class ArrayListIterator implements Iterator<E> {
        private int originalModCount;
        private int currentIndex = -1;
        private boolean isNextReturned;

        public ArrayListIterator() {
            originalModCount = modCount;
        }

        private void checkModifications() {
            if (originalModCount != modCount) {
                throw new ConcurrentModificationException("Список изменен, во время обхода");
            }
        }

        @Override
        public boolean hasNext() {
            return currentIndex + 1 < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Невозможно получить следующий элемент, достигнут конец списка");
            }

            checkModifications();

            currentIndex++;

            isNextReturned = true;

            return items[currentIndex];
        }

        public void remove() {
            if (!isNextReturned) {
                throw new IllegalStateException("Не получен текущий элемент");
            }

            checkModifications();

            ArrayList.this.remove(currentIndex);

            currentIndex--;
            originalModCount = ArrayList.this.modCount;
            isNextReturned = false;
        }
    }

    private void increaseCapacity() {
        if (items.length == 0) {
            createNewItems(DEFAULT_CAPACITY);
        } else {
            setCapacity(items.length * 2);
        }
    }

    @SuppressWarnings("unchecked")
    private void createNewItems(int capacity) {
        items = (E[]) new Object[capacity];
    }

    private void setCapacity(int capacity) {
        items = Arrays.copyOf(items, capacity);
    }

    public void ensureCapacity(int capacity) {
        if (items.length == 0) {
            createNewItems(capacity);

            return;
        }

        if (items.length < capacity) {
            setCapacity(capacity);
        }
    }

    public void trimToSize() {
        if (size == items.length) {
            return;
        }

        setCapacity(size);
    }

    private void checkIndex(int index, int maxIndex) {
        if (index < 0 || index > maxIndex) {
            throw new IndexOutOfBoundsException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + maxIndex + "]");
        }
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
        return indexOf(object) >= 0;
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
    public Object[] toArray() {
        return Arrays.copyOf(items, size);
    }

    @Override
    public <T> T[] toArray(T[] array) {
        if (array.length < size) {
            //noinspection unchecked
            return (T[]) Arrays.copyOf(items, size);
        }

        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(items, 0, array, 0, size);

        if (array.length > size) {
            array[size] = null;
        }

        return array;
    }

    @Override
    public boolean add(E item) {
        add(size, item);

        return true;
    }

    @Override
    public void add(int index, E item) {
        if (items.length == size) {
            increaseCapacity();
        }

        checkIndex(index, size);

        if (index != size) {
            System.arraycopy(items, index, items, index + 1, size - index);
        }

        items[index] = item;
        size++;
        modCount++;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return addAll(size, collection);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }

        checkIndex(index, size);

        ensureCapacity(size + collection.size());

        if (index != size) {
            System.arraycopy(items, index, items, index + collection.size(), size - index);
        }

        int i = index;

        for (E item : collection) {
            items[i] = item;
            i++;
        }

        size += collection.size();
        modCount++;

        return true;
    }

    @Override
    public boolean remove(Object object) {
        if (size == 0) {
            return false;
        }

        int index = indexOf(object);

        if (index == -1) {
            return false;
        }

        remove(index);

        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index, size - 1);

        E removedItem = items[index];

        if (index != size - 1) {
            System.arraycopy(items, index + 1, items, index, size - index - 1);
        }

        size--;
        items[size] = null;
        modCount++;

        return removedItem;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (collection.isEmpty() || size == 0) {
            return false;
        }

        boolean isRemoved = false;

        for (int i = 0; i < size; ) {
            if (collection.contains(items[i])) {
                remove(i);
                isRemoved = true;
            } else {
                i++;
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

        for (int i = 0; i < size; ) {
            if (!collection.contains(items[i])) {
                remove(i);
                isChanged = true;
            } else {
                i++;
            }
        }

        return isChanged;
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        for (int i = 0; i < size; i++) {
            items[i] = operator.apply(items[i]);
        }

        modCount++;
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        Arrays.sort(items, 0, size, comparator);

        modCount++;
    }

    @Override
    public void clear() {
        if (size == 0) {
            return;
        }

        Arrays.fill(items, 0, size - 1, null);
        size = 0;
        modCount++;
    }

    @Override
    public E get(int index) {
        checkIndex(index, size - 1);

        return items[index];
    }

    @Override
    public E set(int index, E newItem) {
        checkIndex(index, size - 1);

        E oldItem = items[index];
        items[index] = newItem;

        return oldItem;
    }

    @Override
    public int indexOf(Object item) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(items[i], item)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object item) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(items[i], item)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }

    @Override
    public Spliterator<E> spliterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            sb.append(items[i]).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        return sb.toString();
    }
}
