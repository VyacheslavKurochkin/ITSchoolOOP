package ru.kurochkin.temperature.model.temperature_scales;

public class KelvinScale implements Scale {
    public static final double ABSOLUTE_ZERO_DEGREES_CELSIUS = -273.15;

    @Override
    public String getName() {
        return "Кельвина";
    }

    @Override
    public double convertFromCelsius(double celsiusDegrees) {
        return -ABSOLUTE_ZERO_DEGREES_CELSIUS + celsiusDegrees;
    }

    @Override
    public double convertToCelsius(double degrees) {
        return ABSOLUTE_ZERO_DEGREES_CELSIUS + degrees;
    }

    @Override
    public String toString() {
        return "Кельвина";
    }
}
