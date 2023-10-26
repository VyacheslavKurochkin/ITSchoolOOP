package ru.kurochkin.temperature.view;

import ru.kurochkin.temperature.model.Converter;
import ru.kurochkin.temperature.model.temperature_scales.Scale;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class View {
    private final String conversionIconFileName = "Temperature\\icons\\arrow.png";

    public View(Scale[] converters) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Перевод температур");
            frame.setSize(500, 200);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            int borderOffset = 10;
            int componentsVerticalStrut = 10;

            JPanel leftPanel = new JPanel();
            leftPanel.setBorder(new CompoundBorder(new EmptyBorder(borderOffset, borderOffset, borderOffset, borderOffset)
                    , new TitledBorder("Значение температуры")));

            JTextField inputTextField = new JTextField(12);

            JComboBox<Scale> inputComboBox = new JComboBox<>(converters);

            Box leftBox = Box.createVerticalBox();
            leftBox.add(inputTextField);
            leftBox.add(Box.createVerticalStrut(componentsVerticalStrut));
            leftBox.add(inputComboBox);

            leftPanel.add(leftBox);

            frame.add(leftPanel, BorderLayout.LINE_START);

            JPanel middlePanel = new JPanel();

            JButton conversionButton = new JButton();

            ImageIcon conversionIconImage = new ImageIcon(conversionIconFileName);

            if (conversionIconImage.getIconWidth() <= 0) {
                conversionButton.setText("Перевести");
            } else {
                conversionButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
                conversionButton.setIcon(conversionIconImage);
            }

            Box middleBox = Box.createVerticalBox();
            middleBox.add(Box.createVerticalStrut(borderOffset));
            middleBox.add(conversionButton);

            middlePanel.add(middleBox);

            frame.add(middlePanel, BorderLayout.CENTER);

            JPanel rightPanel = new JPanel();
            rightPanel.setBorder(new CompoundBorder(new EmptyBorder(borderOffset, borderOffset, borderOffset, borderOffset)
                    , new TitledBorder("Результат")));

            JComboBox<Scale> resultComboBox = new JComboBox<>(converters);

            JTextField resultTextField = new JTextField(12);

            resultTextField.setEditable(false);

            Box rightBox = Box.createVerticalBox();
            rightBox.add(resultTextField);
            rightBox.add(Box.createVerticalStrut(componentsVerticalStrut));
            rightBox.add(resultComboBox);

            rightPanel.add(rightBox);

            frame.add(rightPanel, BorderLayout.LINE_END);

            frame.setVisible(true);

            conversionButton.addActionListener(actionEvent -> {
                String inputText = inputTextField.getText().replace(',', '.');

                double inputDegrees;

                try {
                    inputDegrees = Double.parseDouble(inputText);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, "Введенное значение \"" + inputText +
                            "\" не является числом", "Ошибка", JOptionPane.ERROR_MESSAGE);

                    return;
                }

                //noinspection DataFlowIssue
                resultTextField.setText(Double.toString(Converter.convertTemperature((Scale) inputComboBox.getSelectedItem()
                        , (Scale) resultComboBox.getSelectedItem(), inputDegrees)));
            });

            inputComboBox.addItemListener(e -> resultTextField.setText(""));
            resultComboBox.addItemListener(e -> resultTextField.setText(""));
        });
    }
}
