package ui.electionCommissions;

import service.electionCommissions.electionListService;
import service.electionCommissions.electionService;
import service.electionCommissions.candidateSelectionService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class electionUI {

    public void showElectionManagementPanel() {
        JFrame frame = new JFrame("Управление выборами");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<String> electionModel = new DefaultListModel<>();
        JList<String> electionList = new JList<>(electionModel);
        panel.add(new JScrollPane(electionList), BorderLayout.CENTER);

        JButton createElectionButton = new JButton("Создать выборы");
        JButton loadElectionListButton = new JButton("Загрузить выборы");

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.add(createElectionButton);
        buttonPanel.add(loadElectionListButton);


        panel.add(buttonPanel, BorderLayout.SOUTH);

        createElectionButton.addActionListener(e -> createElection(frame, electionModel));
        loadElectionListButton.addActionListener(e -> new electionListService().loadElectionList(electionModel));


        frame.add(panel);
        frame.setVisible(true);
    }

    private void createElection(JFrame frame, DefaultListModel<String> electionModel) {
        //Проаверка наличия кандидатов для создания голосования
        Map<String, String> candidatesMap = new candidateSelectionService().getEligibleCandidates();
        if (candidatesMap.size() < 2) {
            JOptionPane.showMessageDialog(frame, "Нет доступных кандидатов!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JTextField nameField = new JTextField();
        JTextField commissionIdField = new JTextField();
        JTextField endTimeField = new JTextField();

        Object[] message = {
                "Название выборов:", nameField,
                "ID комиссии:", commissionIdField,
                "Время окончания (формат DD.MM.YYYY, можно оставить пустым):", endTimeField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Создать выборы", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            int commissionId;

            try {
                commissionId = Integer.parseInt(commissionIdField.getText());
                int electionId = new electionService().createElectionWithCandidates(name, commissionId, null);
                JOptionPane.showMessageDialog(frame, "Выборы успешно созданы!");
                new electionListService().loadElectionList(electionModel);

                addCandidatesToElection(frame, electionId, candidatesMap);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Некорректные данные!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void addCandidatesToElection(JFrame frame, int electionId, Map<String, String> candidatesMap) {

        // Получаем список полных имен для отображения в UI
        List<String> candidateNames = new ArrayList<>(candidatesMap.values());

        JList<String> candidateList = new JList<>(candidateNames.toArray(new String[0]));
        candidateList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(candidateList);
        scrollPane.setPreferredSize(new Dimension(250, 150));

        Object[] message = {"Выберите от 2 до 10 кандидатов:", scrollPane};

        int option = JOptionPane.showConfirmDialog(frame, message, "Добавить кандидатов", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            List<String> selectedCandidates = candidateList.getSelectedValuesList();

            if (selectedCandidates.size() < 2 || selectedCandidates.size() > 10) {
                JOptionPane.showMessageDialog(frame, "Выберите от 2 до 10 кандидатов!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }


            List<String> selectedLogins = selectedCandidates.stream()
                    .map(fullName -> candidatesMap.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(fullName))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse(null))
                    .collect(Collectors.toList());


            boolean success = new candidateSelectionService().addCandidatesToElection(selectedLogins, electionId);

            if (success) {
                JOptionPane.showMessageDialog(frame, "Кандидаты успешно добавлены!");
            } else {
                JOptionPane.showMessageDialog(frame, "Ошибка при добавлении кандидатов.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
