package ui.admin;

import dao.userDeletionService;
import service.admin.loadUserListService;

import javax.swing.*;
import java.awt.*;

public class createUserPanelUI {
    public void createUserListPanel() {
        JFrame frame = new JFrame("Управление пользователями");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Только закрывает текущее окно

        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> userModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userModel);
        panel.add(new JScrollPane(userList), BorderLayout.CENTER);

        // Добавляем панель для поиска
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        searchPanel.add(new JLabel("Поиск:"));
        searchPanel.add(searchField);
        panel.add(searchPanel, BorderLayout.NORTH);

        JButton deleteButton = new JButton("Удалить пользователя");
        panel.add(deleteButton, BorderLayout.SOUTH);

        // Загружаем список пользователей
        new loadUserListService().loadUserList(userModel, "");

        // Обработчик для поиска
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                String searchQuery = searchField.getText();
                new loadUserListService().loadUserList(userModel, searchQuery);
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                String searchQuery = searchField.getText();
                new loadUserListService().loadUserList(userModel, searchQuery);
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                String searchQuery = searchField.getText();
                new loadUserListService().loadUserList(userModel, searchQuery);
            }
        });

        deleteButton.addActionListener(e -> {
            String selectedUser = userList.getSelectedValue();
            if (selectedUser != null) {
                String login = selectedUser.split("\\(")[1].replace(")", ""); // Извлекаем логин из строки
                int confirm = JOptionPane.showConfirmDialog(frame, "Вы уверены, что хотите удалить " + selectedUser + "?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    userDeletionService.deleteUser(login); // Удаляем по логину
                    new loadUserListService().loadUserList(userModel, ""); // Обновляем список
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Выберите пользователя для удаления", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
