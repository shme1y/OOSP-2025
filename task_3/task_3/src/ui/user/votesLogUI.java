package ui.user;

import service.user.votesLogService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class votesLogUI {
    public void showVotesLog(String voterLogin) {
        JFrame frame = new JFrame("История голосований");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        List<String> records = new votesLogService().getVotesByUser(voterLogin);

        if (records.isEmpty()) {
            model.addElement("Вы пока не участвовали в голосованиях.");
        } else {
            for (String record : records) {
                model.addElement(record);
            }
        }

        JList<String> list = new JList<>(model);
        frame.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
