package ui.electionCommissions;

import service.electionCommissions.candidateAddService;

import javax.swing.*;
import java.awt.*;

public class candidateAddUI {
    public void showCandidateManagementPanel() {
        JFrame frame = new JFrame("Управление кандидатами");
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));

        JTextField fullNameField = new JTextField();
        JTextField birthDateField = new JTextField("DD.MM.YYYY");
        JTextField partyField = new JTextField();
        JTextField biographyField = new JTextField();
        JTextField loginField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("ФИО:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Дата рождения:"));
        panel.add(birthDateField);
        panel.add(new JLabel("Партия (Не обязательно):"));
        panel.add(partyField);
        panel.add(new JLabel("Биография:"));
        panel.add(biographyField);
        panel.add(new JLabel("Логин:"));
        panel.add(loginField);
        panel.add(new JLabel("Пароль:"));
        panel.add(passwordField);

        JButton submitButton = new JButton("Добавить кандидата");
        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);

        submitButton.addActionListener(e -> {
            String fullName = fullNameField.getText().trim();
            String birthDate = birthDateField.getText().trim();
            String party = partyField.getText().trim().isEmpty() ? "Самовыдвиженец" : partyField.getText().trim();
            String biography = biographyField.getText().trim();
            String login = loginField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (fullName.isEmpty() || birthDate.isEmpty() || login.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Заполните обязательные поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = new candidateAddService().addCandidate(fullName, birthDate, party, biography, login, password);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Кандидат успешно добавлен!");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Ошибка при добавлении кандидата.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
