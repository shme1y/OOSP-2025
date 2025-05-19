package dao;

import config.databaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class checkLoginPassword {

    public static boolean isLoginExistsInAnyTable(String login) throws SQLException {
        return checkIfExistsInAnyTable("login", login);
    }


    public static boolean isPasswordExistsInAnyTable(String password) throws SQLException {
        return checkIfExistsInAnyTable("password_hash", password);
    }


    private static boolean checkIfExistsInAnyTable(String columnName, String value) throws SQLException {
        String[] tables = {"administrators", "users", "election_commissions", "candidates"};

        try (Connection conn = databaseConnection.getConnection()) {
            for (String table : tables) {
                String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", table, columnName);
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, value);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
