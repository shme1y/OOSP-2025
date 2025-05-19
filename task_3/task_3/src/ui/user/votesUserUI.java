package ui.user;

import service.user.votesUserService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class votesUserUI {
    public void showElectionSelection(String voterLogin) {
        JFrame frame = new JFrame("Выбор голосования");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        DefaultListModel<String> model = new DefaultListModel<>();
        List<Integer> ids = new ArrayList<>();
        List<String> elections = new votesUserService().getActiveElections(ids);

        for (String election : elections) {
            model.addElement(election);
        }

        JList<String> list = new JList<>(model);
        frame.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton voteButton = new JButton("Перейти к голосованию");
        frame.add(voteButton, BorderLayout.SOUTH);

        voteButton.addActionListener(e -> {
            int index = list.getSelectedIndex();
            if (index != -1) {
                int electionId = ids.get(index);
                frame.dispose();
                showVotingWindow(electionId, voterLogin);
            } else {
                JOptionPane.showMessageDialog(frame, "Выберите голосование");
            }
        });

        frame.setVisible(true);
    }

    public void showVotingWindow(int electionId, String voterLogin) {
        JFrame frame = new JFrame("Голосование");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        List<String> candidates = new votesUserService().getCandidatesForElection(electionId);
        DefaultListModel<String> model = new DefaultListModel<>();
        candidates.forEach(model::addElement);

        JList<String> list = new JList<>(model);
        frame.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton voteButton = new JButton("Проголосовать");
        frame.add(voteButton, BorderLayout.SOUTH);

        voteButton.addActionListener(e -> {
            String selected = list.getSelectedValue();
            if (selected != null) {
                boolean success = new votesUserService().vote(electionId, selected, voterLogin);
                if (success) frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Выберите кандидата");
            }
        });

        frame.setVisible(true);
    }
}
