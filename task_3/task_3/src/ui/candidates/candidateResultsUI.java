package ui.candidates;

import service.candidates.electionResultsService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class candidateResultsUI {
    public void showResults(String candidateLogin) {
        JFrame frame = new JFrame("Результаты кандидата");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        List<String> results = new electionResultsService().getCandidateResults(candidateLogin);

        if (results.isEmpty()) {
            model.addElement("Нет данных о предыдущих голосованиях.");
        } else {
            results.forEach(model::addElement);
        }

        JList<String> list = new JList<>(model);
        frame.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
