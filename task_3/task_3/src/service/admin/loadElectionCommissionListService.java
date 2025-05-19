package service.admin;

import config.databaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loadElectionCommissionListService {
    public void loadElectionCommissions(DefaultListModel<String> commissionModel) {
        commissionModel.clear();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name FROM election_commissions");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                commissionModel.addElement(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
