package ui.user;

import javax.swing.*;
import java.awt.*;

public class userUI {
    public void userPanel(String login){

        JFrame frame = new JFrame("Панель пользователя");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));


        JButton voidManagementButton = new JButton("Голосование");
        JButton allVoidManagementButton = new JButton("Все голосования в которых голосовал");

        voidManagementButton.addActionListener(e -> voidUserManagement(login));
        allVoidManagementButton.addActionListener(e -> allVoidManagement(login));

        panel.add(voidManagementButton);
        panel.add(allVoidManagementButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void voidUserManagement(String login) {
        new votesUserUI().showElectionSelection(login);
    }

    private void allVoidManagement(String login) {
        new votesLogUI().showVotesLog(login);;
    }
}
