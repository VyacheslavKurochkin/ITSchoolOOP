package ru.kurochkin.temperature_main;

import ru.kurochkin.temperature.model.temperature_scales.CelsiusScale;
import ru.kurochkin.temperature.model.temperature_scales.FahrenheitScale;
import ru.kurochkin.temperature.model.temperature_scales.KelvinScale;
import ru.kurochkin.temperature.model.temperature_scales.Scale;
import ru.kurochkin.temperature.view.View;

public class Main {
    public static void main(String[] args) {
        Scale[] converters = new Scale[]{new CelsiusScale(), new FahrenheitScale(), new KelvinScale()};

        new View(converters);
    }
}