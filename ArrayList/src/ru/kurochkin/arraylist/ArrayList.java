package ru.kurochkin.arraylist;

import java.util.*;
import java.util.function.UnaryOperator;

public class ArrayList<E> implements List<E> {
    private final int DEFAULT_CAPACITY = 10;
    private E[] items;
    private int size;
    private int modCount;

    private class ArrayListIterator implements Iterator<E> {
        int originalModCount;
        int currentIndex = -1;

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
                throw new NoSuchElementException("Не возможно получить следующий элемент, достигнут конец списка");
            }

            checkModifications();

            currentIndex++;

            return items[currentIndex];
        }

        public void remove() {
            if (currentIndex < 0) {
                throw new NoSuchElementException("Не получен текущий элемент списка");
            }

            checkModifications();

            ArrayList.this.remove(currentIndex);
            originalModCount++;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList() {
        items = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Не допустимое значение " + capacity + " для размера списка. " +
                    "Допустимые значения начинаются с 0");
        }

        items = (E[]) new Object[(capacity > 0) ? capacity : DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public ArrayList(List<E> list) {
        size = list.size();

        if (size > 0) {
            items = (E[]) list.toArray();
        } else {
            items = (E[]) new Object[DEFAULT_CAPACITY];
        }
    }

    private void increaseCapacity() {
        setCapacity(items.length * 2);
    }

    private void setCapacity(int capacity) {
        items = Arrays.copyOf(items, capacity);
    }

    public void ensureCapacity(int capacity) {
        if (items.length < capacity) {
            setCapacity(capacity);
        }
    }

    public void trimToSize() {
        if (size == items.length) {
            return;
        }

        if (size < DEFAULT_CAPACITY) {
            return;
        }

        setCapacity(size);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + (size - 1) + "]");
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
    public boolean contains(Object item) {
        return indexOf(item) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection.isEmpty()) {
            return false;
        }

        for (Object item : collection) {
            if (indexOf(item) == -1) {
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
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array) {
        if (array.length < size) {
            return (T[]) Arrays.copyOfRange(items, 0, size, array.getClass());
        }

        T[] itemsCopy = (T[]) Arrays.copyOfRange(items, 0, size, array.getClass());
        System.arraycopy(itemsCopy, 0, array, 0, size);

        if (array.length > size) {
            array[size] = null;
        }

        return array;
    }

    @Override
    public boolean add(E element) {
        if (size == items.length) {
            increaseCapacity();
        }

        items[size] = element;
        size++;

        modCount++;

        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index == size) {
            add(element);
            return;
        }

        checkIndex(index);
        ensureCapacity(size + 1);
        System.arraycopy(items, index, items, index + 1, size - index - 1);

        items[index] = element;
        size++;

        modCount++;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return addAll(size, collection);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }

        if (index != size) {
            checkIndex(index);
        }

        E[] newItems = (E[]) collection.toArray();
        ensureCapacity(size + newItems.length);

        if (index != size) {
            System.arraycopy(items, index, items, index + newItems.length, size - index - 1);
        }

        System.arraycopy(newItems, 0, items, Math.min(index, size), newItems.length);
        size += newItems.length;

        modCount++;

        return true;
    }

    @Override
    public boolean remove(Object item) {
        if (size == 0) {
            return false;
        }

        int index = indexOf(item);

        if (index == -1) {
            return false;
        }

        remove(index);

        modCount++;

        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);

        E deletedItem = items[index];

        if (index != size - 1) {
            System.arraycopy(items, index + 1, items, index, items.length - index - 1);
        }

        size--;
        items[size] = null;

        modCount++;

        return deletedItem;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (collection.isEmpty() || size == 0) {
            return false;
        }

        boolean isDeleted = false;

        for (Object item : collection) {
            if (remove(item)) {
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
            if (items[i] != null) {
                items[i] = operator.apply(items[i]);

                modCount++;
            }
        }
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        Arrays.sort(items, 0, size, comparator);
    }

    @Override
    public void clear() {
        items = null;
        size = 0;
    }

    @Override
    public E get(int index) {
        checkIndex(index);

        return items[index];
    }

    @Override
    public E set(int index, E newItem) {
        checkIndex(index);

        E oldItem = items[index];
        items[index] = newItem;

        modCount++;

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
        return List.super.spliterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListIterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public E next() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public E previous() {
                return null;
            }

            @Override
            public int nextIndex() {
                return 0;
            }

            @Override
            public int previousIndex() {
                return 0;
            }

            @Override
            public void remove() {

            }

            @Override
            public void set(E e) {

            }

            @Override
            public void add(E e) {

            }
        };
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public E next() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public E previous() {
                return null;
            }

            @Override
            public int nextIndex() {
                return 0;
            }

            @Override
            public int previousIndex() {
                return 0;
            }

            @Override
            public void remove() {

            }

            @Override
            public void set(E e) {

            }

            @Override
            public void add(E e) {

            }
        };
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return this;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            if (items[i] == null) {
                sb.append("null, ");
            } else {
                sb.append(items[i].toString()).append(", ");
            }
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        return sb.toString();
    }
}
