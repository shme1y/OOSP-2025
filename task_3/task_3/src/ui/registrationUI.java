package ui;

import service.registrationService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registrationUI {
    public void displayRegistrationPage() {
        JFrame frame = new JFrame("Страница Регистрации");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Окно будет по центру экрана

        JPanel panel = new JPanel();
        frame.add(panel);

        placeComponents(panel);

        frame.setVisible(true);
    }


    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel fullNameLabel = new JLabel("Полное имя:");
        fullNameLabel.setBounds(50, 50, 100, 25);
        panel.add(fullNameLabel);

        JTextField fullNameText = new JTextField(20);
        fullNameText.setBounds(150, 50, 200, 25);
        panel.add(fullNameText);

        JLabel birthDateLabel = new JLabel("Дата рождения:");
        birthDateLabel.setBounds(50, 100, 100, 25);
        panel.add(birthDateLabel);

        JTextField birthDateText = new JTextField(20);
        birthDateText.setBounds(150, 100, 200, 25);
        panel.add(birthDateText);

        JLabel passportDataLabel = new JLabel("Паспортные данные:");
        passportDataLabel.setBounds(50, 150, 120, 25);
        panel.add(passportDataLabel);

        JTextField passportDataText = new JTextField(20);
        passportDataText.setBounds(150, 150, 200, 25);
        panel.add(passportDataText);

        JLabel emailLabel = new JLabel("Логин:");
        emailLabel.setBounds(50, 200, 100, 25);
        panel.add(emailLabel);

        JTextField emailText = new JTextField(20);
        emailText.setBounds(150, 200, 200, 25);
        panel.add(emailText);

        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setBounds(50, 250, 100, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 250, 200, 25);
        panel.add(passwordField);

        JButton registerButton = new JButton("Зарегистрироваться");
        registerButton.setBounds(150, 300, 150, 30);
        panel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullName = fullNameText.getText();
                String birthDate = birthDateText.getText();
                String passportData = passportDataText.getText();
                String email = emailText.getText();
                String password = new String(passwordField.getPassword());

                registrationService.registerUser(fullName, birthDate, passportData, email, password);
            }
        });
    }
}
