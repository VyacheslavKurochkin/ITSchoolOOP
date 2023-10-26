package ru.kurochkin.temperature.model;

import ru.kurochkin.temperature.model.temperature_scales.Scale;

public class Converter {
    public static double convertTemperature(Scale from, Scale to, double degrees) {
        return to.convertFromCelsius(from.convertToCelsius(degrees));
    }
}
