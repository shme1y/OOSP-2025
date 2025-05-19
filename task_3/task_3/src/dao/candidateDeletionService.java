package dao;

import config.databaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class candidateDeletionService {
    public static void deleteCandidate(String login) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM candidates WHERE login = ?")) {
            stmt.setString(1, login);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Кандидат успешно удалён.");
            } else {
                JOptionPane.showMessageDialog(null, "Ошибка: кандидат не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при удалении кандидата: " + e.getMessage());
        }
    }
}
