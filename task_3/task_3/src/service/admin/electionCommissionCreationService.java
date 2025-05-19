package service.admin;

import config.databaseConnection;
import dao.checkLoginPassword;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class electionCommissionCreationService {
    public static void createElectionCommission(
            String name, String region, String address,
            String contactEmail, String contactPhone,
            String login, String passwordHash) {


        String sql = "INSERT INTO election_commissions (name, region, address, contact_email, contact_phone, login, password_hash) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (checkLoginPassword.isLoginExistsInAnyTable(login)) {
                JOptionPane.showMessageDialog(null, "Логин уже существует. Попробуйте другой.");
                return;
            }


            if (checkLoginPassword.isPasswordExistsInAnyTable(passwordHash)) {
                JOptionPane.showMessageDialog(null, "Этот пароль уже используется. Выберите другой.");
                return;
            }

            stmt.setString(1, name);
            stmt.setString(2, region);
            stmt.setString(3, address);
            stmt.setString(4, contactEmail);
            stmt.setString(5, contactPhone);
            stmt.setString(6, login);
            stmt.setString(7, passwordHash);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "ЦИК успешно создан!");
            } else {
                JOptionPane.showMessageDialog(null, "Ошибка при создании ЦИКа.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при добавлении ЦИКа: " + e.getMessage());
        }
    }
}
