package ru.kurochkin.temperature.model.temperature_scales;

public interface Scale {
    double convertFromCelsius(double celsiusDegrees);
    double convertToCelsius(double degrees);
}
