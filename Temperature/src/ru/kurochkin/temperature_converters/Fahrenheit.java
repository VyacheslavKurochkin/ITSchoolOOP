package ru.kurochkin.temperature_converters;

public class Fahrenheit implements TemperatureConverter {
    public static final double WATER_FREEZING_DEGREES_FAHRENHEIT = 32;
    public static final double CELSIUS_TO_FAHRENHEIT_RATE = 1.8;

    public Fahrenheit() {
    }

    @Override
    public String getName() {
        return "Фаренгейта";
    }

    @Override
    public double getDegreesFromCelsius(double degreesCelsius) {
        return WATER_FREEZING_DEGREES_FAHRENHEIT + degreesCelsius * CELSIUS_TO_FAHRENHEIT_RATE;
    }

    @Override
    public double getDegreesToCelsius(double degrees) {
        return (degrees - WATER_FREEZING_DEGREES_FAHRENHEIT) / CELSIUS_TO_FAHRENHEIT_RATE;
    }
}
