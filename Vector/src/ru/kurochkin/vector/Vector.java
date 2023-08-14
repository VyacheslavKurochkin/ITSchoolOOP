package ru.kurochkin.vector;

import java.util.Arrays;

public class Vector {
    private double[] components;

    public Vector(int dimension) {
        if (dimension <= 0) {
            throw new IllegalArgumentException(dimension + " - недопустимое значение размерности, " +
                    "допустимые значения начинаются с 1");
        }

        components = new double[dimension];
    }

    public Vector(Vector vector) {
        this(vector.components.length, vector.components);
    }

    public Vector(double[] components) {
        this(components.length, components);
    }

    public Vector(int dimension, double[] components) {
        if (dimension <= 0) {
            throw new IllegalArgumentException(dimension + " - недопустимое значение размерности, " +
                    "допустимые значения начинаются с 1");
        }

        int minDimension = Math.min(dimension, components.length);

        this.components = new double[dimension];

        System.arraycopy(components, 0, this.components, 0, minDimension);
    }

    public int getDimension() {
        return components.length;
    }

    public void add(Vector vector) {
        components = Arrays.copyOf(components, vector.components.length);

        for (int i = 0; i < vector.components.length; i++) {
            components[i] += vector.components[i];
        }
    }

    public void subtract(Vector vector) {
        components = Arrays.copyOf(components, vector.components.length);

        for (int i = 0; i < vector.components.length; i++) {
            components[i] -= vector.components[i];
        }
    }

    public void multiplyByScalar(double scalar) {
        for (int i = 0; i < components.length; i++) {
            components[i] *= scalar;
        }
    }

    public void reverse() {
        multiplyByScalar(-1);
    }

    public double getLength() {
        double sum = 0;

        for (double component : components) {
            sum += component * component;
        }

        return Math.sqrt(sum);
    }

    public double getComponent(int index) {
        if (index < 0 || index >= components.length) {
            throw new IndexOutOfBoundsException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + (components.length - 1) + "]");
        }

        return components[index];
    }

    public void setComponent(int index, double component) {
        if (index < 0 || index >= components.length) {
            throw new IndexOutOfBoundsException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + (components.length - 1) + "]");
        }

        components[index] = component;
    }

    public static Vector getSum(Vector vector1, Vector vector2) {
        Vector resultingVector = new Vector(vector1);

        resultingVector.add(vector2);

        return resultingVector;
    }

    public static Vector getDifference(Vector vector1, Vector vector2) {
        Vector resultingVector = new Vector(vector1);

        resultingVector.subtract(vector2);

        return resultingVector;
    }

    public static double getScalarProduct(Vector vector1, Vector vector2) {
        int minDimension = Math.min(vector1.components.length, vector2.components.length);
        double sum = 0;

        for (int i = 0; i < minDimension; i++) {
            sum += vector1.components[i] * vector2.components[i];
        }

        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        for (double component : components) {
            sb.append(component).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append('}');

        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        Vector vector = (Vector) object;

        return Arrays.equals(components, vector.components);
    }

    @Override
    public int hashCode() {
        return 17 + Arrays.hashCode(components);
    }
}
