package service.electionCommissions;

import config.databaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class getElectionsListId {
    public List<String> getElectionIds() {
        List<String> electionIds = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM elections ORDER BY id")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                electionIds.add(String.valueOf(rs.getInt("id")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return electionIds;
    }
}
