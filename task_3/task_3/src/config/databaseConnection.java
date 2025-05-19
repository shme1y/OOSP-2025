package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection {
    protected static final String URL = "jdbc:postgresql://localhost:5432/electronic_voting_system";
    protected static final String USER = "username";
    protected static final String PASSWORD = "postgres";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
