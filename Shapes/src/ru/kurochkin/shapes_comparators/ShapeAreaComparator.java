package ru.kurochkin.shapes_comparators;

import java.util.Comparator;

import ru.kurochkin.shapes.*;

public class ShapeAreaComparator implements Comparator<Shape> {
    @Override
    public int compare(Shape shape1, Shape shape2) {
        return Double.compare(shape1.getArea(), shape2.getArea());
    }
}