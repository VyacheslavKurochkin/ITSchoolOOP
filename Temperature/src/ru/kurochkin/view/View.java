package ru.kurochkin.view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class View {
    private final JFrame frame;
    private final JTextField inputTextField;
    private final JTextField resultTextField;
    private final JComboBox<String> inputComboBox;
    private final JComboBox<String> resultComboBox;
    private final JButton conversionButton;

    public View() {
        frame = new JFrame("Первод температур");
        frame.setSize(500, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int borderOffset = 10;
        int componentsVerticalStrut = 10;

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new CompoundBorder(new EmptyBorder(borderOffset, borderOffset, borderOffset, borderOffset)
                , new TitledBorder("Значение температуры")));

        inputTextField = new JTextField(12);

        inputComboBox = new JComboBox<>();

        Box leftBox = Box.createVerticalBox();
        leftBox.add(inputTextField);
        leftBox.add(Box.createVerticalStrut(componentsVerticalStrut));
        leftBox.add(inputComboBox);

        leftPanel.add(leftBox);

        frame.add(leftPanel, BorderLayout.LINE_START);

        JPanel middlePanel = new JPanel();

        conversionButton = new JButton();

        Box middleBox = Box.createVerticalBox();
        middleBox.add(Box.createVerticalStrut(borderOffset));
        middleBox.add(conversionButton);

        middlePanel.add(middleBox);

        frame.add(middlePanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(new CompoundBorder(new EmptyBorder(borderOffset, borderOffset, borderOffset, borderOffset)
                , new TitledBorder("Результат")));

        resultComboBox = new JComboBox<>();

        resultTextField = new JTextField(12);
        resultTextField.setEditable(false);

        Box rightBox = Box.createVerticalBox();
        rightBox.add(resultTextField);
        rightBox.add(Box.createVerticalStrut(componentsVerticalStrut));
        rightBox.add(resultComboBox);

        rightPanel.add(rightBox);

        frame.add(rightPanel, BorderLayout.LINE_END);

        frame.setVisible(true);
    }

    public void setComboBoxesItems(String[] degreesNames) {
        for (String degreesName : degreesNames) {
            inputComboBox.addItem(degreesName);
            resultComboBox.addItem(degreesName);
        }
    }

    public JComboBox<String> getInputComboBox() {
        return inputComboBox;
    }

    public String getInputComboBoxSelectedItem() {
        return (String) inputComboBox.getSelectedItem();
    }

    public JComboBox<String> getResultComboBox() {
        return resultComboBox;
    }

    public String getResultComboBoxSelectedItem() {
        return (String) resultComboBox.getSelectedItem();
    }

    public JButton getConversionButton() {
        return conversionButton;
    }

    public JTextField getInputTextField() {
        return inputTextField;
    }

    public JTextField getResultTextField() {
        return resultTextField;
    }

    public void showErrorText(String errorText) {
        JOptionPane.showMessageDialog(frame, errorText, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
