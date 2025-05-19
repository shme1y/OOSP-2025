package ui.candidates;

import service.candidates.candidateService;
import javax.swing.*;
import java.awt.*;

public class addBiographyUI {
    public void showBiographyEditor(String login) {
        JFrame frame = new JFrame("Редактирование биографии");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea biographyTextArea = new JTextArea( new candidateService().getBiography(login));
        biographyTextArea.setLineWrap(true);
        biographyTextArea.setWrapStyleWord(true);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            String newBiography = biographyTextArea.getText();
            new candidateService().updateBiography(login, newBiography);
            JOptionPane.showMessageDialog(frame, "Биография обновлена!");
            frame.dispose();
        });

        panel.add(new JScrollPane(biographyTextArea), BorderLayout.CENTER);
        panel.add(saveButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}
