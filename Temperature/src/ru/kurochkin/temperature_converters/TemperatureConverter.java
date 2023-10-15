package ru.kurochkin.temperature_converters;

public interface TemperatureConverter {
    String getName();
    double getDegreesFromCelsius(double degreesCelsius);
    double getDegreesToCelsius(double degrees);
// конвертировать из цельсия в свою шкалу
// конвертровать из своей в шкалу цельсия

}
