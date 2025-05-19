package service;

import config.databaseConnection;
import dao.checkLoginPassword;
import ui.registrationUI;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class registrationService {
    public static void registerUser(String fullName, String birthDate, String passportData, String login, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            if (checkLoginPassword.isLoginExistsInAnyTable(login)) {
                JOptionPane.showMessageDialog(null, "Логин уже существует. Попробуйте другой.");
                new registrationUI().displayRegistrationPage();
                return;
            }

            if (checkLoginPassword.isPasswordExistsInAnyTable(password)) {
                JOptionPane.showMessageDialog(null, "Этот пароль уже используется. Выберите другой.");
                new registrationUI().displayRegistrationPage();
                return;
            }

            conn = databaseConnection.getConnection();

            String sql = "INSERT INTO users (full_name, birth_date, passport_data, login, password_hash) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fullName);
            pstmt.setString(2, birthDate);
            pstmt.setString(3, passportData);
            pstmt.setString(4, login);
            pstmt.setString(5, password);


            pstmt.executeUpdate();


            JOptionPane.showMessageDialog(null, "Регистрация успешна!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при регистрации: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
