package service;

import config.databaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class checkAndAddResultElections {
    public void restoreDisqualifiedForExpiredElections() {
        String sql = "SELECT id FROM elections WHERE end_time <= NOW()";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int electionId = rs.getInt("id");

                restoreDisqualifiedCandidatesIfLost(electionId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restoreDisqualifiedCandidatesIfLost(int electionId) {
        String tableName = "elections_" + electionId;


        String getMaxVotesSql = "SELECT MAX(votes) FROM " + tableName;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement maxVotesStmt = conn.prepareStatement(getMaxVotesSql);
             ResultSet rs = maxVotesStmt.executeQuery()) {

            int maxVotes = 0;
            if (rs.next()) {
                maxVotes = rs.getInt(1);
            }

            String checkTieSql = "SELECT COUNT(*) FROM " + tableName + " WHERE votes = ?";
            try (PreparedStatement tieStmt = conn.prepareStatement(checkTieSql)) {
                tieStmt.setInt(1, maxVotes);
                ResultSet tieRs = tieStmt.executeQuery();
                if (tieRs.next() && tieRs.getInt(1) > 1) {
                    System.out.println("Имеется ничья! Для всех кандидатов устанавливается disqualified = FALSE.");
                    String resetDisqualifiedSql = "UPDATE candidates SET disqualified = FALSE " +
                            "WHERE login IN (SELECT login FROM " + tableName + ")";
                    try (PreparedStatement resetStmt = conn.prepareStatement(resetDisqualifiedSql)) {
                        resetStmt.executeUpdate();
                        System.out.println("Статус 'disqualified' для всех кандидатов восстановлен.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    recordElectionResults(electionId, conn, tableName);

                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sql = "UPDATE candidates SET disqualified = FALSE " +
                    "WHERE login IN (SELECT login FROM " + tableName + ") " +
                    "AND login NOT IN ( " +
                    "    SELECT login FROM " + tableName + " " +
                    "    WHERE votes = ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, maxVotes);
                stmt.executeUpdate();
            }

            recordElectionResults(electionId, conn, tableName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recordElectionResults(int electionId, Connection conn, String tableName) {
        String checkExistsSql = "SELECT COUNT(*) FROM election_results WHERE election_id = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkExistsSql)) {
            checkStmt.setInt(1, electionId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Результаты для выборов ID " + electionId + " уже записаны. Пропуск.");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        String insertResultsSql = "INSERT INTO election_results (election_id, candidate_login, votes) " +
                "SELECT ?, login, votes FROM " + tableName;

        try (PreparedStatement stmt = conn.prepareStatement(insertResultsSql)) {
            stmt.setInt(1, electionId);
            stmt.executeUpdate();
            System.out.println("Результаты выборов для выбора ID " + electionId + " записаны в таблицу election_results.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
