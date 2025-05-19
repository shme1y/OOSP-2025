package ui;

import service.authenticationService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginUI {
    public void displayLoginPage() {

        JFrame frame = new JFrame("Страница Входа");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Логин:");
        userLabel.setBounds(50, 50, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(150, 50, 200, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setBounds(50, 100, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Войти");
        loginButton.setBounds(150, 150, 100, 30);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = userText.getText();
                String password = new String(passwordField.getPassword());

                if (authenticationService.authenticate(login, password)) {
                    JOptionPane.showMessageDialog(null, "Авторизация успешна!");
                } else {
                    JOptionPane.showMessageDialog(null, "Неверный логин или пароль.");
                }
            }
        });
    }
}
