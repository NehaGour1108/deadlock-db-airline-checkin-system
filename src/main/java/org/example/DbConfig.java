package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbConfig {
    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void initializeDatabase() throws Exception {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE TableA (id INT PRIMARY KEY, dataval VARCHAR(255))");
            stmt.execute("CREATE TABLE TableB (id INT PRIMARY KEY, dataval VARCHAR(255))");

            stmt.execute("INSERT INTO TableA (id, dataval) VALUES (1, 'A1')");
            stmt.execute("INSERT INTO TableB (id, dataval) VALUES (1, 'B1')");
        }
    }
}

