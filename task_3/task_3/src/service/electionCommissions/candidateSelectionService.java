package service.electionCommissions;

import config.databaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class candidateSelectionService {

    public Map<String, String> getEligibleCandidates() {
        Map<String, String> candidates = new HashMap<>();
        String sql = "SELECT login, full_name FROM candidates WHERE disqualified = FALSE";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String login = rs.getString("login");
                String fullName = rs.getString("full_name");
                candidates.put(login, fullName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    public boolean addCandidatesToElection(List<String> selectedCandidates, int electionId) {
        String tableName = "elections_" + electionId;
        String insertSql = "INSERT INTO " + tableName + " (login) VALUES (?)";
        String disqualifySql = "UPDATE candidates SET disqualified = TRUE WHERE login = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement disqualifyStmt = conn.prepareStatement(disqualifySql)) {

            for (String candidate : selectedCandidates) {
                insertStmt.setString(1, candidate);
                insertStmt.addBatch();

                disqualifyStmt.setString(1, candidate);
                disqualifyStmt.addBatch();
            }

            insertStmt.executeBatch();
            disqualifyStmt.executeBatch();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

