package ru.kurochkin.list;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SinglyLinkedList<E> {
    private ListItem<E> head;
    private int length;

    public SinglyLinkedList() {
    }

    public SinglyLinkedList(E... array) {
        for (int i = array.length - 1; i >= 0; i--) {
            addFirst(array[i]);
        }
    }

    public SinglyLinkedList(E data) {
        addFirst(data);
    }

    public int getLength() {
        return length;
    }

    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException("Список пуст.");
        }

        return head.getData();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + (length - 1) + "]");
        }
    }

    private ListItem<E> getItem(int index) {
        ListItem<E> item = head;

        for (int i = 0; i < index; i++) {
            item = item.getNext();
        }

        return item;
    }

    public E get(int index) {
        checkIndex(index);

        return getItem(index).getData();
    }

    public E set(int index, E data) {
        checkIndex(index);

        ListItem<E> item = getItem(index);

        E oldData = item.getData();
        item.setData(data);

        return oldData;
    }

    public E remove(int index) {
        checkIndex(index);

        if (index == 0) {
            return removeFirst();
        }

        ListItem<E> previousItem = getItem(index - 1);
        E removedData = previousItem.getNext().getData();
        previousItem.setNext(previousItem.getNext().getNext());

        length--;

        return removedData;
    }

    public void addFirst(E data) {
        head = new ListItem<>(data, head);

        length++;
    }

    public void add(int index, E data) {
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + length + "]");
        }

        if (index == 0) {
            addFirst(data);

            return;
        }

        ListItem<E> previousItem = getItem(index - 1);
        previousItem.setNext(new ListItem<>(data, previousItem.getNext()));

        length++;
    }

    public boolean remove(E data) {
        ListItem<E> previousItem = null;
        ListItem<E> currentItem = head;

        for (; currentItem != null; previousItem = currentItem, currentItem = currentItem.getNext()) {
            if (Objects.equals(data, currentItem.getData())) {
                break;
            }
        }

        if (currentItem == null) {
            return false;
        }

        if (previousItem == null) {
            removeFirst();
        } else {
            previousItem.setNext(currentItem.getNext());

            length--;
        }

        return true;
    }

    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("Удалить нельзя. Список элементов пуст");
        }

        E removedData = head.getData();
        head = head.getNext();
        length--;

        return removedData;
    }

    public void reverse() {
        ListItem<E> previousItem = null;

        for (ListItem<E> currentItem = head; currentItem != null; ) {
            ListItem<E> nextItem = currentItem.getNext();
            currentItem.setNext(previousItem);
            previousItem = currentItem;
            currentItem = nextItem;
        }

        head = previousItem;
    }

    public SinglyLinkedList<E> copy() {
        if (length == 0) {
            return new SinglyLinkedList<>();
        }

        SinglyLinkedList<E> newList = new SinglyLinkedList<>(head.getData());
        ListItem<E> newListCurrentItem = newList.head;

        for (ListItem<E> currentItem = head.getNext(); currentItem != null; currentItem = currentItem.getNext(),
                newListCurrentItem = newListCurrentItem.getNext()) {
            newListCurrentItem.setNext(new ListItem<>(currentItem.getData()));
        }

        newList.length = length;

        return newList;
    }

    @Override
    public String toString() {
        if (length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();

        sb.append('[');

        for (ListItem<E> currentItem = head; currentItem != null; currentItem = currentItem.getNext()) {
            sb.append(currentItem.getData()).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        return sb.toString();
    }
}
