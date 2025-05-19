package service.electionCommissions;

import config.databaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class electionService {

    public int createElectionWithCandidates(String name, int commissionId, String endTime) {
        String insertElectionSQL = "INSERT INTO elections (name, commission_id, start_time, end_time) " +
                "VALUES (?, ?, NOW(), ?::timestamp)";


        if (endTime == null || endTime.isEmpty()) {
            endTime = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        int electionId = 0;
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertElectionSQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, commissionId);
            pstmt.setString(3, endTime);

            pstmt.executeUpdate();


            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                electionId = rs.getInt(1);
                System.out.println("Выборы созданы с ID: " + electionId);


                createCandidatesTable(electionId);
                System.out.println("Кандидаты должны быть добавлены вручную через ЦИК.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return electionId;
    }


    private void createCandidatesTable(int electionId) {
        String tableName = "elections_" + electionId;
        String createTableSQL = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "id SERIAL PRIMARY KEY, " +
                        "login VARCHAR(100) NOT NULL, " +
                        "votes INT NOT NULL DEFAULT 0, " +
                        "FOREIGN KEY (login) REFERENCES candidates(login) ON DELETE CASCADE)",
                tableName
        );

        try (Connection conn = databaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);
            System.out.println("Таблица кандидатов для выборов " + electionId + " создана успешно.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
