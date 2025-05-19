package service.user;

import config.databaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class votesLogService {
    public List<String> getVotesByUser(String voterLogin) {
        List<String> votes = new ArrayList<>();

        String sql = "SELECT e.name AS election_name, v.candidate_login, v.vote_time " +
                "FROM votes_log v " +
                "JOIN elections e ON v.election_id = e.id " +
                "WHERE v.voter_login = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voterLogin);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String election = rs.getString("election_name");
                    String candidate = rs.getString("candidate_login");
                    Timestamp time = rs.getTimestamp("vote_time");

                    votes.add(String.format("Выборы: %s | Кандидат: %s | Время: %s", election, candidate, time));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return votes;
    }
}
