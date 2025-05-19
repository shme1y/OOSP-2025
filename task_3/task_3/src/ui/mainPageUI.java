package ui;

import service.checkAndAddResultElections;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class mainPageUI {

    public void displayMainPage() {


        JFrame frame = new JFrame("Главная страница");
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

        JButton loginButton = new JButton("Вход");
        loginButton.setBounds(150, 50, 100, 30);
        panel.add(loginButton);

        JButton registerButton = new JButton("Регистрация");
        registerButton.setBounds(150, 100, 100, 30);
        panel.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginPage();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationPage();
            }
        });
    }

    private void openLoginPage() {
        new checkAndAddResultElections().restoreDisqualifiedForExpiredElections();
        loginUI loginUI = new loginUI();
        loginUI.displayLoginPage();
    }

    private void openRegistrationPage() {
        new checkAndAddResultElections().restoreDisqualifiedForExpiredElections();
        registrationUI registrationUI = new registrationUI();
        registrationUI.displayRegistrationPage();
    }
}

