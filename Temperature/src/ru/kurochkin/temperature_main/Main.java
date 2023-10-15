package ru.kurochkin.temperature_main;

import ru.kurochkin.controller.Controller;
import ru.kurochkin.model.Model;
import ru.kurochkin.view.View;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controller temperatureConverterController = new Controller(new Model(), new View());
        });
    }
}