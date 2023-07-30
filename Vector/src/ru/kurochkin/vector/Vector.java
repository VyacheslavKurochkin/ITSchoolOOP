package ru.kurochkin.vector;

import java.util.Arrays;

public class Vector {
    public double[] vector;

    private int dimension;

    public Vector(int dimension) {
        if (dimension <= 0) {
            throw new IllegalArgumentException("Размерность не может быть меньше 1");
        }

        vector = new double[dimension];
        this.dimension = dimension;

        for (int i = 0; i < dimension; i++) {
            vector[i] = 0;
        }
    }

    public Vector(Vector inputVector) {
        this(inputVector.dimension, inputVector.vector);
    }

    public Vector(double[] components) {
        this(components.length, components);
    }

    public Vector(int dimension, double[] components) {
        if (dimension <= 0) {
            throw new IllegalArgumentException("Размерность не может быть меньше 1");
        }

        this.dimension = dimension;
        vector = new double[dimension];

        int minLength = Math.min(dimension, components.length);

        for (int i = 0; i < minLength; i++) {
            this.vector[i] = components[i];
        }

        for (int i = minLength; i < dimension; i++) {
            this.vector[i] = 0;
        }
    }

    public int getSize() {
        return dimension;
    }

    public void addVector(Vector inputVector) {
        int minDimension = Math.min(dimension, inputVector.dimension);

        for (int i = 0; i < minDimension; i++) {
            vector[i] += inputVector.vector[i];
        }
    }

    public void subtractVector(Vector inputVector) {
        int minDimension = Math.min(dimension, inputVector.dimension);

        for (int i = 0; i < minDimension; i++) {
            vector[i] -= inputVector.vector[i];
        }
    }

    public void multiplyByScalar(double scalar) {
        for (int i = 0; i < dimension; i++) {
            vector[i] *= scalar;
        }
    }

    public void reverse() {
        multiplyByScalar(-1);
    }

    public double getLength() {
        double sum = 0;

        for (double component : vector) {
            sum += component * component;
        }

        return Math.sqrt(sum);
    }

    public double getComponent(int index) {
        if (index < 0 || index >= dimension) {
            throw new IllegalArgumentException("Не допустимое значения индекса компоненты ветора");
        }

        return vector[index];
    }

    public void setComponent(double component, int index) {
        vector[index] = component;
    }

    public static Vector getAddition(Vector vector1, Vector vector2) {
        int maxDimension = Math.max(vector1.dimension, vector2.dimension);
        int minDimension = Math.min(vector1.dimension, vector2.dimension);

        Vector newVector = new Vector(maxDimension);

        for (int i = 0; i < minDimension; i++) {
            newVector.setComponent(vector1.vector[i] + vector2.vector[i], i);
        }

        for (int i = minDimension; i < maxDimension; i++) {
            newVector.setComponent(vector1.dimension == maxDimension ? vector1.vector[i] : vector2.vector[i], i);
        }

        return newVector;
    }

    public static Vector getSubtraction(Vector vector1, Vector vector2) {
        int maxDimension = Math.max(vector1.dimension, vector2.dimension);
        int minDimension = Math.min(vector1.dimension, vector2.dimension);

        Vector newVector = new Vector(maxDimension);

        for (int i = 0; i < minDimension; i++) {
            newVector.setComponent(vector1.vector[i] - vector2.vector[i], i);
        }

        for (int i = minDimension; i < maxDimension; i++) {
            newVector.setComponent(vector1.dimension == maxDimension ? vector1.vector[i] : -vector2.vector[i], i);
        }

        return newVector;
    }

    public static double getScalarMultiplying(Vector vector1, Vector vector2) {
        int minDimension = Math.min(vector1.dimension, vector2.dimension);
        double sum = 0;

        for (int i = 0; i < minDimension; i++) {
            sum += vector1.vector[i] * vector2.vector[i];
        }

        return sum;
    }

    @Override
    public String toString() {
        return Arrays.toString(vector).replace("[", "{").replace("]", "}");
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

        return dimension == vector.dimension && Arrays.equals(this.vector, vector.vector);
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int hash = 1;

        hash = prime * hash + dimension;
        hash = prime * hash + Arrays.hashCode(vector);

        return hash;
    }
}
