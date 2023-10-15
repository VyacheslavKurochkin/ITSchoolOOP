package ru.kurochkin.temperature_converters;

public class Kelvin implements TemperatureConverter {
    public static final double ABSOLUTE_ZERO_DEGREES_CELSIUS = -273.15;

    public Kelvin() {
    }

    @Override
    public String getName() {
        return "Кельвина";
    }

    @Override
    public double getDegreesFromCelsius(double degreesCelsius) {
        return -ABSOLUTE_ZERO_DEGREES_CELSIUS + degreesCelsius;
    }

    @Override
    public double getDegreesToCelsius(double degrees) {
        return ABSOLUTE_ZERO_DEGREES_CELSIUS + degrees;
    }
}
