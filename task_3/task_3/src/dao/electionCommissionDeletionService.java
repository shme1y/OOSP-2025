package dao;

import config.databaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class electionCommissionDeletionService {
    public static void deleteElectionCommission(String commissionName) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM election_commissions WHERE name = ?")) {
            stmt.setString(1, commissionName);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "ЦИК удален успешно.");
            } else {
                JOptionPane.showMessageDialog(null, "Ошибка: ЦИК не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при удалении ЦИКа: " + e.getMessage());
        }
    }
}
