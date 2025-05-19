package service.electionCommissions;

import config.databaseConnection;
import dao.checkLoginPassword;
import ui.registrationUI;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class candidateAddService {

    public boolean addCandidate(String fullName, String birthDate, String party, String biography, String login, String password) {

        String sql = "INSERT INTO candidates (full_name, birth_date, party, biography, login, password_hash) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (checkLoginPassword.isLoginExistsInAnyTable(login)) {
                JOptionPane.showMessageDialog(null, "Логин уже существует. Попробуйте другой.");
                new registrationUI().displayRegistrationPage();
                return false;
            }


            if (checkLoginPassword.isPasswordExistsInAnyTable(password)) {
                JOptionPane.showMessageDialog(null, "Этот пароль уже используется. Выберите другой.");
                new registrationUI().displayRegistrationPage();
                return false;
            }

            pstmt.setString(1, fullName);
            pstmt.setDate(2, convertToDate(birthDate));
            pstmt.setString(3, party);
            pstmt.setString(4, biography);
            pstmt.setString(5, login);
            pstmt.setString(6, password);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Date convertToDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date utilDate = sdf.parse(dateStr);
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Ошибка в формате даты. Используйте DD.MM.YYYY.");
        }
    }
}
