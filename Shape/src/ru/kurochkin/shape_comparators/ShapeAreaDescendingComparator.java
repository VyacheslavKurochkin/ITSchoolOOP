package ru.kurochkin.shape_comparators;

import java.util.Comparator;

import ru.kurochkin.shapes.*;

public class ShapeAreaDescendingComparator implements Comparator<Shape> {
    @Override
    public int compare(Shape shape1, Shape shape2) {
        return -1 * Double.compare(shape1.getArea(), shape2.getArea());
    }
}