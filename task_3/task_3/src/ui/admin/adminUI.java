package ui.admin;

import javax.swing.*;
import java.awt.*;

public class adminUI {
    public void adminPanel() {

        JFrame frame = new JFrame("Панель администратора");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Окно будет по центру экрана


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));


        JButton userManagementButton = new JButton("Просмотр и удаление пользователей");
        JButton cikManagementButton = new JButton("Просмотр и удаление ЦИК");
        JButton createCikButton = new JButton("Создание ЦИК");
        JButton candidateManagementButton = new JButton("Просмотр и удаление кандидатов");


        userManagementButton.addActionListener(e -> openUserManagement());
        cikManagementButton.addActionListener(e -> openCikManagement());
        createCikButton.addActionListener(e -> createCik());
        candidateManagementButton.addActionListener(e -> openCandidateManagement());


        panel.add(userManagementButton);
        panel.add(cikManagementButton);
        panel.add(createCikButton);
        panel.add(candidateManagementButton);


        frame.add(panel);
        frame.setVisible(true);
    }


    private void openUserManagement() {
        new createUserPanelUI().createUserListPanel();
    }

    private void openCikManagement() {
        new electionCommissionManagementUI().openElectionCommissionManagementWindow();
    }

    private void createCik() {
        new createElectionCommissionUI().showCreateElectionCommissionForm();
    }

    private void openCandidateManagement() {
        new candidateManagementUI().showCandidateManagementPanel();
    }
}
