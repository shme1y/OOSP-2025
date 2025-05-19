package service.candidates;

import config.databaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class candidateParticipationService {
    public List<String> getElectionsByCandidate(String candidateLogin) {
        List<String> elections = new ArrayList<>();

        String sql = """
            SELECT e.name, e.start_time, e.end_time, r.votes
            FROM election_results r
            JOIN elections e ON r.election_id = e.id
            WHERE r.candidate_login = ?
            ORDER BY e.start_time DESC
        """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, candidateLogin);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String line = String.format(
                        "Выборы: %s | Период: %s - %s | Голосов: %d",
                        rs.getString("name"),
                        rs.getTimestamp("start_time"),
                        rs.getTimestamp("end_time"),
                        rs.getInt("votes")
                );
                elections.add(line);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return elections;
    }
}
