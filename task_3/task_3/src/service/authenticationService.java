package service;

import config.databaseConnection;
import ui.admin.adminUI;
import ui.candidates.candidatesUI;
import ui.user.userUI;
import ui.electionCommissions.electionCommissionsUI;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class authenticationService {
    public static boolean authenticate(String login, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = databaseConnection.getConnection();

            if (checkCredentials(conn, "administrators", login, password)) {
                adminUI adminUI = new adminUI();
                adminUI.adminPanel();
                return true;
            }

            if (checkCredentials(conn, "users", login, password)) {
                userUI userUI = new userUI();
                userUI.userPanel(login);
                return true;
            }

            if (checkCredentials(conn, "election_commissions", login, password)) {
                electionCommissionsUI electionCommissionUI = new electionCommissionsUI();
                electionCommissionUI.electionCommissionsPanel();
                return true;
            }

            if (checkCredentials(conn, "candidates", login, password)) {
                candidatesUI candidate = new candidatesUI();
                candidate.candidatePanel(login);
                return true;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static boolean checkCredentials(Connection conn, String tableName, String login, String password) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE login = ? AND password_hash = ?", tableName);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        }
        return false;
    }
}
