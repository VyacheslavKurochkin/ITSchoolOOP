package ru.kurochkin.vector;

import java.util.Arrays;

public class Vector {
    private double[] vertex;

    public Vector(int dimension) {
        if (dimension <= 0) {
            throw new IllegalArgumentException(dimension + " - недопустимое значение рзмерности, " +
                    "допустимые значения начинаются с 1");
        }

        vertex = new double[dimension];
    }

    public Vector(Vector vector) {
        this(vector.vertex.length, vector.vertex);
    }

    public Vector(double[] components) {
        this(components.length, components);
    }

    public Vector(int dimension, double[] components) {
        if (dimension <= 0) {
            throw new IllegalArgumentException(dimension + " - недопустимое значение рзмерности, " +
                    "допустимые значения начинаются с 1");
        }

        int minLength = Math.min(dimension, components.length);

        vertex = new double[dimension];

        for (int i = 0; i < minLength; i++) {
            vertex[i] = components[i];
        }
    }

    public int getDimension() {
        return vertex.length;
    }

    public void add(Vector vector) {
        double[] tempVertex;

        if (vertex.length < vector.vertex.length) {
            tempVertex = Arrays.copyOf(vertex, vector.vertex.length);
        } else {
            tempVertex = vertex;
        }

        int minDimension = Math.min(vertex.length, vector.vertex.length);

        for (int i = 0; i < minDimension; i++) {
            tempVertex[i] += vector.vertex[i];
        }

        vertex = tempVertex;
    }

    public void subtract(Vector vector) {
        double[] tempVertex;

        if (vertex.length < vector.vertex.length) {
            tempVertex = Arrays.copyOf(vertex, vector.vertex.length);
        } else {
            tempVertex = vertex;
        }

        int minDimension = Math.min(vertex.length, vector.vertex.length);

        for (int i = 0; i < minDimension; i++) {
            vertex[i] -= vector.vertex[i];
        }

        vertex = tempVertex;
    }

    public void multiplyByScalar(double scalar) {
        for (int i = 0; i < vertex.length; i++) {
            vertex[i] *= scalar;
        }
    }

    public void reverse() {
        multiplyByScalar(-1);
    }

    public double getLength() {
        double sum = 0;

        for (double component : vertex) {
            sum += component * component;
        }

        return Math.sqrt(sum);
    }

    public double getComponent(int index) {
        if (index < 0 || index >= vertex.length) {
            throw new IndexOutOfBoundsException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + (vertex.length - 1) + "]");
        }

        return vertex[index];
    }

    public void setComponent(int index, double component) {
        if (index < 0 || index >= vertex.length) {
            throw new IndexOutOfBoundsException("Индекс " + index + " за пределами диапазона допустимых значений " +
                    "[0.." + (vertex.length - 1) + "]");
        }

        vertex[index] = component;
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
        int minDimension = Math.min(vector1.vertex.length, vector2.vertex.length);
        double sum = 0;

        for (int i = 0; i < minDimension; i++) {
            sum += vector1.vertex[i] * vector2.vertex[i];
        }

        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (int i = 0; i < vertex.length - 1; i++) {
            sb.append(vertex[i]).append(",");
        }

        sb.append(vertex[vertex.length - 1]).append("}");

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

        return vertex.length == vector.vertex.length && Arrays.equals(vertex, vector.vertex);
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int hash = 1;

        hash = prime * hash + vertex.length;
        hash = prime * hash + Arrays.hashCode(vertex);

        return hash;
    }
}
