package ui.candidates;

import service.candidates.candidateParticipationService;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class candidateParticipationUI {
    public void showCandidateElections(String candidateLogin) {
        JFrame frame = new JFrame("Участие в голосованиях");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        List<String> elections = new candidateParticipationService().getElectionsByCandidate(candidateLogin);

        if (elections.isEmpty()) {
            model.addElement("Кандидат не участвовал в голосованиях.");
        } else {
            elections.forEach(model::addElement);
        }

        JList<String> list = new JList<>(model);
        frame.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
