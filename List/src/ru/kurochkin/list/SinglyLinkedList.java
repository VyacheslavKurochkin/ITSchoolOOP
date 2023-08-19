package ru.kurochkin.list;

import ru.kurochkin.list_item.ListItem;

public class SinglyLinkedList<T> {
    private ListItem<T> head;
    private int count;

    public SinglyLinkedList(T[] arrayData) {
        count = 0;

        if (arrayData.length == 0) {
            return;
        }

        for (int i = arrayData.length - 1; i >= 0; i--) {
            addHead(arrayData[i]);
        }
    }

    public SinglyLinkedList(ListItem<T> head) {
        this.head = head;
        count = 1;
    }

    public int getSize() {
        return count;
    }

    public T getHeadData() {
        return head.getData();
    }

    private void checkArgumentIndex(int index) {
        if (index < 0 || index > count - 1) {
            throw new IllegalArgumentException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + (count - 1) + "]");
        }
    }

    public ListItem<T> getItem(int index) {
        checkArgumentIndex(index);

        ListItem<T> listItem = head;

        for (int i = 0; i < index; i++) {
            listItem = listItem.getNext();
        }

        return listItem;
    }

    public T getData(int index) {
        checkArgumentIndex(index);

        return getItem(index).getData();
    }

    public T setData(int index, T data) {
        checkArgumentIndex(index);

        ListItem<T> listItem = getItem(index);

        T previousData = listItem.getData();
        listItem.setData(data);

        return previousData;
    }

    public T remove(int index) {
        checkArgumentIndex(index);

        if (index == 0) {
            return removeHead();
        }

        ListItem<T> previous = head;
        ListItem<T> next = head.getNext();

        for (int i = 1; i < index; i++) {
            previous = next;
            next = next.getNext();
        }

        previous.setNext(next.getNext());

        count--;

        return next.getData();
    }

    public void addHead(T data) {
        head = new ListItem<>(data, head);
        count++;
    }

    public void add(int index, T data) {
        checkArgumentIndex(index);

        if (index == 0) {
            addHead(data);

            return;
        }

        ListItem<T> previous = getItem(index - 1);
        ListItem<T> newItem = new ListItem<>(data, previous.getNext());
        previous.setNext(newItem);

        count++;
    }

    public boolean remove(T data) {
        ListItem<T> previous = head;

        for (ListItem<T> current = head; current != null; previous = current, current = current.getNext()) {
            if (current.getData().equals(data)) {
                previous.setNext(current.getNext());

                count--;

                return true;
            }
        }

        return false;
    }

    public T removeHead() {
        if (count < 1) {
            throw new IndexOutOfBoundsException("Удалить нельзя. Список элементов пуст");
        }

        T previousData = head.getData();
        head = head.getNext();
        count--;

        return previousData;
    }

    public void reverse() {
        ListItem<T> previous = null;
        ListItem<T> next;

        for (ListItem<T> current = head; current != null; previous = current, current = next) {
            next = current.getNext();
            current.setNext(previous);
        }

        head = previous;
    }

    public SinglyLinkedList<T> copy() {
        SinglyLinkedList<T> list = new SinglyLinkedList<>(new ListItem<>(head.getData()));

        for (ListItem<T> current = head.getNext(); current != null; current = current.getNext()) {
            list.addHead(current.getData());
        }

        list.reverse();

        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('{');

        for (ListItem<T> current = head; current != null; current = current.getNext()) {
            sb.append(current.getData()).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append('}');

        return sb.toString();
    }
}
