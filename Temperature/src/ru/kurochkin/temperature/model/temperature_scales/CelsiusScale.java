package ru.kurochkin.temperature.model.temperature_scales;

public class CelsiusScale implements Scale {
    @Override
    public String getName() {
        return "Цельсия";
    }

    @Override
    public double convertFromCelsius(double celsiusDegrees) {
        return celsiusDegrees;
    }

    @Override
    public double convertToCelsius(double degrees) {
        return degrees;
    }

    @Override
    public String toString() {
        return "Цельсия";
    }
}
