package ru.kurochkin.shape;

import java.util.Comparator;

public class ShapeComparator implements Comparator<Shape> {
    ShapeGeometricalDimensions compareDimension;
    ShapeSortDirections direction;

    public ShapeComparator() {
        compareDimension = ShapeGeometricalDimensions.AREA;
        direction = ShapeSortDirections.ASC;
    }

    public ShapeComparator(ShapeGeometricalDimensions dimension, ShapeSortDirections direction) {
        this.direction = direction;
        this.compareDimension = dimension;
    }

    @Override
    public int compare(Shape first, Shape second) {
        int sortingValue = (direction == ShapeSortDirections.ASC) ? 1 : -1;

        if(compareDimension == ShapeGeometricalDimensions.AREA) {
            return sortingValue * Double.compare(first.getArea(), second.getArea());
        }

        return sortingValue * Double.compare(first.getPerimeter(), second.getPerimeter());
    }
}
