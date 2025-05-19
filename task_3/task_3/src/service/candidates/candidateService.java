package service.candidates;

import config.databaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class candidateService {

    public String getBiography(String login) {
        String sql = "SELECT biography FROM candidates WHERE login = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("biography");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void updateBiography(String login, String newBiography) {
        String sql = "UPDATE candidates SET biography = ? WHERE login = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newBiography);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
