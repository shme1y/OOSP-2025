package dao;

import config.databaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class userDeletionService {
    public static void deleteUser(String login) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE login = ?")) {
            stmt.setString(1, login);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Пользователь удален успешно.");
            } else {
                JOptionPane.showMessageDialog(null, "Ошибка: пользователь не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при удалении пользователя: " + e.getMessage());
        }
    }
}
