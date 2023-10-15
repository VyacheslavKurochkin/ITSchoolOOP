package ru.kurochkin.temperature_converters;

public class Celsius implements TemperatureConverter {
    public Celsius() {
    }

    @Override
    public String getName() {
        return "Цельсия";
    }

    @Override
    public double getDegreesFromCelsius(double degreesCelsius) {
        return degreesCelsius;
    }

    @Override
    public double getDegreesToCelsius(double degrees) {
        return degrees;
    }
}
