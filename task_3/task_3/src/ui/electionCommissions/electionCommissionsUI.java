package ui.electionCommissions;

import javax.swing.*;
import java.awt.*;

public class electionCommissionsUI {
    public void electionCommissionsPanel() {

        JFrame frame = new JFrame("Панель ЦИК");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Окно будет по центру экрана

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton creatElectionButton = new JButton("Создание голосования");
        JButton addCandidatesButton = new JButton("Добавление кандидата");
        JButton workOfResultsButton = new JButton("Экспорт в PDF");

        creatElectionButton.addActionListener(e -> creatElectionManagement());
        addCandidatesButton.addActionListener(e -> addCandidatesManagement());
        workOfResultsButton.addActionListener(e -> workOfResults());

        panel.add(creatElectionButton);
        panel.add(addCandidatesButton);
        panel.add(workOfResultsButton);

        frame.add(panel);
        frame.setVisible(true);
    }


    private void creatElectionManagement() {
        new electionUI().showElectionManagementPanel();
    }

    private void addCandidatesManagement() {
        new candidateAddUI().showCandidateManagementPanel();
    }

    private void workOfResults() {
        new workOfPDF().workOfPDFPanel();
    }

}
