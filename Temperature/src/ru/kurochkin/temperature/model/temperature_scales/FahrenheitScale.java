package ru.kurochkin.temperature.model.temperature_scales;

public class FahrenheitScale implements Scale {
    public static final double WATER_FREEZING_DEGREES_FAHRENHEIT = 32;
    public static final double CELSIUS_TO_FAHRENHEIT_RATE = 1.8;

    @Override
    public double convertFromCelsius(double celsiusDegrees) {
        return WATER_FREEZING_DEGREES_FAHRENHEIT + celsiusDegrees * CELSIUS_TO_FAHRENHEIT_RATE;
    }

    @Override
    public double convertToCelsius(double degrees) {
        return (degrees - WATER_FREEZING_DEGREES_FAHRENHEIT) / CELSIUS_TO_FAHRENHEIT_RATE;
    }

    @Override
    public String toString() {
        return "Фаренгейта";
    }
}
