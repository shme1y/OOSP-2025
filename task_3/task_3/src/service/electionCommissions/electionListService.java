package service.electionCommissions;

import java.sql.*;
import javax.swing.*;
import config.databaseConnection;

public class electionListService {

    public void loadElectionList(DefaultListModel<String> electionModel) {
        electionModel.clear();

        String sql = "SELECT * FROM elections";

        try (Connection conn = databaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            while (rs.next()) {
                int id = rs.getInt("id");
                String electionName = rs.getString("name");
                int commissionId = rs.getInt("commission_id");
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp endTime = rs.getTimestamp("end_time");


                String electionDetails = String.format("id %d| Выборы: %s | Комиссия ID: %d | Начало: %s | Окончание: %s",
                        id, electionName, commissionId, startTime.toString(), endTime.toString());


                electionModel.addElement(electionDetails);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при загрузке выборов из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

