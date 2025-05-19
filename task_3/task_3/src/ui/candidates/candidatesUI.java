package ui.candidates;

import javax.swing.*;
import java.awt.*;

public class candidatesUI {
    public void candidatePanel(String login) {
        JFrame frame = new JFrame("Панель администратора");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Окно будет по центру экрана


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));


        JButton biographyButton = new JButton("Заполнить данные о себе");
        JButton historyElectionButton = new JButton("История выборов");
        JButton resultLastButton = new JButton("Результаты последних выборов");


        biographyButton.addActionListener(e -> biographyAdd(login));
        historyElectionButton.addActionListener(e -> historyElection(login));
        resultLastButton.addActionListener(e -> resultLast(login));

        panel.add(biographyButton);
        panel.add(historyElectionButton);
        panel.add(resultLastButton);

        frame.add(panel);
        frame.setVisible(true);
    }


    private void biographyAdd(String login) {
        new addBiographyUI().showBiographyEditor(login);
    }

    private void historyElection(String login) {
        new candidateParticipationUI().showCandidateElections(login);
    }

    private void resultLast(String login) {
        new candidateResultsUI().showResults(login);
    }

}
