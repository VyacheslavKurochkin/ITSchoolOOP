package ru.kurochkin.shapes_main;

import ru.kurochkin.shapes.*;
import ru.kurochkin.shape_comparators.*;

import java.util.Arrays;

public class Main {

    public static Shape getMaxAreaShape(Shape[] shapes) {
        if(shapes.length == 0){
            return null;
        }

        Arrays.sort(shapes, new ShapeAreaDescendingComparator());

        return shapes[0];
    }

    public static Shape getShapeByDescendingPerimeter(Shape[] shapes, int position) {
        if(shapes.length == 0){
            return null;
        }

        Arrays.sort(shapes, new ShapePerimeterDescendingComparator());

        return shapes[position - 1];
    }

    public static void main(String[] args) {
        Shape[] shapes = {
            new Square(23.2),
            new Square(13.2),
            new Square(3.2),
            new Rectangle(2, 5),
            new Rectangle(40, 88),
            new Rectangle(10, 15),
            new Circle(23.2),
            new Circle(13.2),
            new Circle(3.2),
            new Triangle(1, 1, 4, 4, 5, 1)
        };

        System.out.println("Фигура с максимальной площадью:");
        Shape maxAreaShape = getMaxAreaShape(shapes);
        System.out.println(maxAreaShape);

        System.out.println("Фигура со вторым по величине периметром:");
        Shape secondPerimetrShape = getShapeByDescendingPerimeter(shapes, 2);
        System.out.println(secondPerimetrShape);
    }
}