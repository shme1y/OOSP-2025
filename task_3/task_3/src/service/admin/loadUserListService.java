package service.admin;

import config.databaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loadUserListService {
    public void loadUserList(DefaultListModel<String> userModel, String searchQuery) {
        userModel.clear();
        String sql = "SELECT login, full_name FROM users WHERE full_name LIKE ? OR login LIKE ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String login = rs.getString("login");
                    String fullName = rs.getString("full_name");

                    userModel.addElement(fullName + " (" + login + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
