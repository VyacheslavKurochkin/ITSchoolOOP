package ru.kurochkin.controller;

import ru.kurochkin.model.Model;
import ru.kurochkin.view.View;

import javax.swing.*;

public class Controller {
    private final View view;
    private final Model model;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        initController();
        initView();
    }

    private void initController() {
        view.getConversionButton().addActionListener(e -> convertTemperature());
        view.getInputComboBox().addItemListener(e -> clearResult());
        view.getResultComboBox().addItemListener(e -> clearResult());
    }

    private void initView() {
        view.setComboBoxesItems(model.getConvertersNames());

        String conversionIconFileName = "Temperature\\icons\\arrow.png";
        ImageIcon conversionIconImage = new ImageIcon(conversionIconFileName);

        if (conversionIconImage.getIconWidth() <= 0) {
            view.getConversionButton().setText("Преобразовать");
        } else {
            view.getConversionButton().setAlignmentY(JComponent.CENTER_ALIGNMENT);
            view.getConversionButton().setIcon(conversionIconImage);
        }
    }

    public void convertTemperature() {
        String inputText = view.getInputTextField().getText().trim().replace(",", ".");
        if (inputText.isEmpty()) {
            view.showErrorText("Пустое значение температуры");
            return;
        }

        if (!inputText.matches("[-+]?\\d*\\.?\\d+")) {
            view.showErrorText("Введенное значение не является числом");
            return;
        }

        double inputDegrees;

        try {
            inputDegrees = Double.parseDouble(inputText);
        } catch (NumberFormatException e) {
            view.showErrorText("Введенное значение не является числом");
            return;
        }

        double resultDegrees = model.convertTemperature(view.getInputComboBoxSelectedItem(),
                view.getResultComboBoxSelectedItem(), inputDegrees);
        view.getResultTextField().setText(String.format("%.2f", resultDegrees));
    }

    private void clearResult() {
        view.getResultTextField().setText("");
    }
}
