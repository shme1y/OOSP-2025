package ui.admin;

import dao.candidateDeletionService;
import service.admin.loadCandidateListService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class candidateManagementUI {
    public void showCandidateManagementPanel() {
        JFrame frame = new JFrame("Управление кандидатами");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<String> candidateModel = new DefaultListModel<>();
        JList<String> candidateList = new JList<>(candidateModel);
        panel.add(new JScrollPane(candidateList), BorderLayout.CENTER);

        // Добавляем панель для поиска
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        searchPanel.add(new JLabel("Поиск:"));
        searchPanel.add(searchField);
        panel.add(searchPanel, BorderLayout.NORTH);

        JButton deleteButton = new JButton("Удалить кандидата");
        panel.add(deleteButton, BorderLayout.SOUTH);

        new loadCandidateListService().loadCandidateList(candidateModel, "");

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                String searchQuery = searchField.getText();
                new loadCandidateListService().loadCandidateList(candidateModel, searchQuery);
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                String searchQuery = searchField.getText();
                new loadCandidateListService().loadCandidateList(candidateModel, searchQuery);
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                String searchQuery = searchField.getText();
                new loadCandidateListService().loadCandidateList(candidateModel, searchQuery);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCandidate = candidateList.getSelectedValue();
                if (selectedCandidate != null) {
                    String login = selectedCandidate.split("\\(")[1].replace(")", ""); // Извлекаем логин из строки
                    int confirm = JOptionPane.showConfirmDialog(frame, "Удалить " + selectedCandidate + "?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        candidateDeletionService.deleteCandidate(login); // Удаляем по логину
                        new loadCandidateListService().loadCandidateList(candidateModel, ""); // Обновляем список
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Выберите кандидата для удаления", "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
