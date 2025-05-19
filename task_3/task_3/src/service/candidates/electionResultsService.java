package service.candidates;

import config.databaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class electionResultsService {

    public List<String> getCandidateResults(String candidateLogin) {
        List<String> results = new ArrayList<>();
        String sql = """
            SELECT e.name, r.votes
            FROM election_results r
            JOIN elections e ON r.election_id = e.id
            WHERE r.candidate_login = ?
            ORDER BY e.start_time DESC
            LIMIT 1
        """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, candidateLogin);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String line = "Выборы: " + rs.getString("name") +
                        " | Голосов: " + rs.getInt("votes");
                results.add(line);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}

