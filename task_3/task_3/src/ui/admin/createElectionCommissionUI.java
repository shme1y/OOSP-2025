package ui.admin;

import service.admin.electionCommissionCreationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class createElectionCommissionUI {
    public void showCreateElectionCommissionForm() {
        JFrame frame = new JFrame("Создание ЦИКа");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 5, 5));

        JLabel nameLabel = new JLabel("Название:");
        JTextField nameField = new JTextField();

        JLabel regionLabel = new JLabel("Регион:");
        JTextField regionField = new JTextField();

        JLabel addressLabel = new JLabel("Адрес:");
        JTextField addressField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel phoneLabel = new JLabel("Телефон:");
        JTextField phoneField = new JTextField();

        JLabel loginLabel = new JLabel("Логин:");
        JTextField loginField = new JTextField();

        JLabel passwordLabel = new JLabel("Пароль:");
        JPasswordField passwordField = new JPasswordField();

        JButton submitButton = new JButton("Создать ЦИК");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(regionLabel);
        panel.add(regionField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(loginLabel);
        panel.add(loginField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String region = regionField.getText();
                String address = addressField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());

                if (name.isEmpty() || region.isEmpty() || address.isEmpty() || email.isEmpty() ||
                        phone.isEmpty() || login.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Все поля должны быть заполнены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                electionCommissionCreationService.createElectionCommission(name, region, address, email, phone, login, password);
            }
        });
    }
}
