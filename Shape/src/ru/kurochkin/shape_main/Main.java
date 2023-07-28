package ru.kurochkin.shape_main;

import ru.kurochkin.shape.*;

import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static Shape getShapeMaxArea(Shape[] shapes) {
        Comparator<Shape> shapeAreaComparator = new ShapeComparator(ShapeGeometricalDimensions.AREA, ShapeSortDirections.DESC);

        Arrays.sort(shapes, shapeAreaComparator);

        return shapes[0];
    }

    public static Shape getShapeByDescendingPerimeter(Shape[] shapes, int pos) {
        Comparator<Shape> shapePerimiterComparator = new ShapeComparator(ShapeGeometricalDimensions.PERIMETER, ShapeSortDirections.DESC);

        Arrays.sort(shapes, shapePerimiterComparator);

        return shapes[pos - 1];
    }

    public static void main(String[] args) {
        Shape[] shapes = new Shape[10];

        shapes[0] = new Square(23.2);
        shapes[1] = new Square(13.2);
        shapes[2] = new Square(3.2);
        shapes[3] = new Rectangle(2, 5);
        shapes[4] = new Rectangle(4, 8);
        shapes[5] = new Rectangle(10, 15);
        shapes[6] = new Circle(23.2);
        shapes[7] = new Circle(13.2);
        shapes[8] = new Circle(3.2);
        shapes[9] = new Triangle(1, 1, 4, 4, 5, 1);

        System.out.println("Фигура с максимальной площадью:");
        Shape maxAreaShape = getShapeMaxArea(shapes);
        System.out.printf("%s , Площадь %f\n", maxAreaShape, maxAreaShape.getArea());

        System.out.println("Фигура со вторым по величине периметром:");
        Shape secondPerimetrShape = getShapeByDescendingPerimeter(shapes, 2);
        System.out.printf("%s , Периметр %f", secondPerimetrShape, secondPerimetrShape.getPerimeter());
    }
}