package service.user;

import config.databaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class votesUserService {

    public List<String> getActiveElections(List<Integer> ids) {
        List<String> elections = new ArrayList<>();
        String sql = "SELECT id, name FROM elections WHERE end_time > NOW()";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
                elections.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return elections;
    }


    public List<String> getCandidatesForElection(int electionId) {
        List<String> candidates = new ArrayList<>();
        String tableName = "elections_" + electionId;
        String sql = "SELECT login FROM " + tableName;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                candidates.add(rs.getString("login"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidates;
    }


    public boolean hasUserVoted(int electionId, String voterLogin) {
        String tableName = "elections_" + electionId;
        String sql = "SELECT * FROM " + tableName + " WHERE login = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voterLogin);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean vote(int electionId, String candidateLogin, String voterLogin) {
        String tableName = "elections_" + electionId;
        String updateVotesSql = "UPDATE " + tableName + " SET votes = votes + 1 WHERE login = ?";

        try (Connection conn = databaseConnection.getConnection()) {
            if (hasUserVoted(electionId, voterLogin)) {
                JOptionPane.showMessageDialog(null, "Вы уже проголосовали.");
                return false;
            }


            try (PreparedStatement stmt = conn.prepareStatement(updateVotesSql)) {
                stmt.setString(1, candidateLogin);
                stmt.executeUpdate();
            }

            String logVoteSql = "INSERT INTO votes_log (election_id, voter_login, candidate_login) VALUES (?, ?, ?)";
            try (PreparedStatement logStmt = conn.prepareStatement(logVoteSql)) {
                logStmt.setInt(1, electionId);
                logStmt.setString(2, voterLogin);
                logStmt.setString(3, candidateLogin);
                logStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Голос успешно учтён!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при голосовании: Вы уже отдали свой голос в этом голосовании");
        }

        return false;
    }
}
