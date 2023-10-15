package ru.kurochkin.model;

import ru.kurochkin.temperature_converters.Celsius;
import ru.kurochkin.temperature_converters.Fahrenheit;
import ru.kurochkin.temperature_converters.Kelvin;
import ru.kurochkin.temperature_converters.TemperatureConverter;

import java.util.Arrays;
import java.util.Optional;

public class Model {
    private final TemperatureConverter[] converters;

    public Model() {
        converters = new TemperatureConverter[]{new Celsius(), new Fahrenheit(), new Kelvin()};
    }

    public String[] getConvertersNames() {
        return Arrays.stream(converters).map(TemperatureConverter::getName).toList().toArray(new String[]{""});
    }

    public double convertTemperature(String fromDegreesName, String toDegreesName, double degrees) {
        Optional<TemperatureConverter> inputConverter = Arrays.stream(converters)
                .filter(converter -> converter.getName().equals(fromDegreesName))
                .findFirst();

        Optional<TemperatureConverter> resultConverter = Arrays.stream(converters)
                .filter(converter -> converter.getName().equals(toDegreesName))
                .findFirst();

        //noinspection OptionalGetWithoutIsPresent
        return resultConverter.get().getDegreesFromCelsius(inputConverter.get().getDegreesToCelsius(degrees));
    }

}
